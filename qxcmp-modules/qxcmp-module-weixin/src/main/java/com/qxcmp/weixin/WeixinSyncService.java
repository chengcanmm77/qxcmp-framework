package com.qxcmp.weixin;

import com.google.common.collect.Lists;
import com.qxcmp.config.SiteService;
import com.qxcmp.image.Image;
import com.qxcmp.image.ImageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.weixin.event.AdminWeixinMaterialSyncFinishEvent;
import com.qxcmp.weixin.event.AdminWeixinMaterialSyncStartEvent;
import com.qxcmp.weixin.event.WeixinUserSyncFinishEvent;
import com.qxcmp.weixin.event.WeixinUserSyncStartEvent;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialCountResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static me.chanjar.weixin.common.api.WxConsts.MaterialType.*;

/**
 * 微信公众号同步服务
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WeixinSyncService {

    private static final int MAX_MATERIAL_COUNT = 20;

    private final ApplicationContext applicationContext;
    private final UserService userService;
    private final SiteService siteService;
    private final ImageService imageService;
    private final WxMpService wxMpService;
    private final WeixinMpMaterialService weixinMpMaterialService;

    private boolean weixinUserSync;
    private long currentUserSync;
    private long totalUserSync;
    private boolean weixinMaterialSync;
    private long currentMaterialSync;
    private long totalMaterialSync;

    /**
     * 执行用户同步
     * <p>
     * 负责查询所有微信公众号已关注用户，并与平台用户数据库同步
     *
     * @param user 触发用户
     */
    @Async
    public void syncUsers(User user) {

        if (weixinUserSync) {
            return;
        }

        try {
            log.info("Start weixin user sync");
            weixinUserSync = true;
            applicationContext.publishEvent(new WeixinUserSyncStartEvent(user));

            List<String> openIds = Lists.newArrayList();
            long total;
            long successCount = 0;

            String nextOpenId = null;

            do {
                WxMpUserList userList = wxMpService.getUserService().userList(nextOpenId);
                total = userList.getTotal();
                openIds.addAll(userList.getOpenids());
                nextOpenId = userList.getNextOpenid();
            } while (StringUtils.isNotBlank(nextOpenId));

            totalUserSync = total;

            for (String openId : openIds) {
                try {
                    syncUser(wxMpService.getUserService().userInfo(openId));
                    ++successCount;
                    currentUserSync++;
                } catch (Exception e) {
                    log.warn("Weixin user info syncUser failed：{}", e.getMessage());
                }
            }

            log.info("Finish weixin user sync, total {}, success: {}", total, successCount);
            applicationContext.publishEvent(new WeixinUserSyncFinishEvent(user, totalUserSync));
            weixinUserSync = false;
            totalUserSync = 0;
        } catch (Exception e) {
            log.error("Can't load weixin user, cause：{}", e.getMessage(), Objects.nonNull(e.getCause()) ? e.getCause().getMessage() : "");
            weixinUserSync = false;
            totalMaterialSync = 0;
        }
    }

    /**
     * 同步一个微信用户
     * <p>
     * 如果OpenID对应的用户存在，则更新用户信息
     * <p>
     * 如果对应的用户不存在，则创建新的用户
     *
     * @param wxMpUser 获取到的微信用户信息
     */
    public void syncUser(WxMpUser wxMpUser) {

        Optional<User> userOptional = userService.findByOpenID(wxMpUser.getOpenId());

        if (userOptional.isPresent()) {
            userService.update(userOptional.get().getId(), user -> setUserWeixinInfo(user, wxMpUser));
        } else {
            userService.create(() -> {
                User user = userService.next();
                user.setUsername(nextUsername());
                setUserWeixinInfo(user, wxMpUser);
                return user;
            });
        }
    }

    /**
     * 执行素材同步
     *
     * @param user 触发用户
     */
    @Async
    public void syncMaterials(User user) {

        if (weixinMaterialSync) {
            return;
        }

        try {
            log.info("Start weixin materials sync");
            weixinMaterialSync = true;
            applicationContext.publishEvent(new AdminWeixinMaterialSyncStartEvent(user));

            WxMpMaterialCountResult countResult = wxMpService.getMaterialService().materialCount();
            totalMaterialSync = countResult.getImageCount() + countResult.getNewsCount() + countResult.getVideoCount() + countResult.getVoiceCount();

            for (int i = 0; i <= countResult.getNewsCount() / MAX_MATERIAL_COUNT; i++) {
                WxMpMaterialNewsBatchGetResult newsBatchGetResult = wxMpService.getMaterialService().materialNewsBatchGet(i * MAX_MATERIAL_COUNT, i * MAX_MATERIAL_COUNT + MAX_MATERIAL_COUNT);

                newsBatchGetResult.getItems().forEach(wxMaterialNewsBatchGetNewsItem -> {
                    List<WxMpMaterialNews.WxMpMaterialNewsArticle> articles = wxMaterialNewsBatchGetNewsItem.getContent().getArticles();

                    for (int j = 0; j < articles.size(); j++) {
                        WxMpMaterialNews.WxMpMaterialNewsArticle article = articles.get(j);
                        log.info("Load weixin material news: {}", article.getTitle());
                        String id = wxMaterialNewsBatchGetNewsItem.getMediaId() + j;

                        WeixinMpMaterial next = weixinMpMaterialService.next();
                        next.setId(id);
                        next.setType(WeixinMpMaterialType.NEWS);
                        next.setTitle(article.getTitle());
                        next.setThumbMediaId(article.getThumbMediaId());

                        try {
                            InputStream inputStream = wxMpService.getMaterialService().materialImageOrVoiceDownload(article.getThumbMediaId());
                            Image image = imageService.store(inputStream, "jpg");
                            next.setThumbUrl(String.format("/api/image/%s.jpg", image.getId()));
                        } catch (Exception e) {
                            next.setThumbUrl(siteService.getLogo());
                        }

                        next.setShowCover(article.isShowCoverPic());
                        next.setAuthor(article.getAuthor());
                        next.setDigest(article.getDigest());
                        next.setContent(getWeixinArticleContent(article.getContent()));
                        next.setUrl(article.getUrl());
                        next.setSourceUrl(article.getContentSourceUrl());
                        next.setUpdateTime(wxMaterialNewsBatchGetNewsItem.getUpdateTime());

                        weixinMpMaterialService.save(next);
                        currentMaterialSync++;
                    }
                });
            }

            for (int i = 0; i <= countResult.getImageCount() / MAX_MATERIAL_COUNT; i++) {
                WxMpMaterialFileBatchGetResult fileBatchGetResult = wxMpService.getMaterialService().materialFileBatchGet(IMAGE, i * MAX_MATERIAL_COUNT, i * MAX_MATERIAL_COUNT + MAX_MATERIAL_COUNT);
                syncMaterial(fileBatchGetResult, WeixinMpMaterialType.IMAGE);
                currentMaterialSync++;
            }

            for (int i = 0; i <= countResult.getVideoCount() / MAX_MATERIAL_COUNT; i++) {
                WxMpMaterialFileBatchGetResult fileBatchGetResult = wxMpService.getMaterialService().materialFileBatchGet(VIDEO, i * MAX_MATERIAL_COUNT, i * MAX_MATERIAL_COUNT + MAX_MATERIAL_COUNT);
                syncMaterial(fileBatchGetResult, WeixinMpMaterialType.VIDEO);
                currentMaterialSync++;
            }

            for (int i = 0; i <= countResult.getVoiceCount() / MAX_MATERIAL_COUNT; i++) {
                WxMpMaterialFileBatchGetResult fileBatchGetResult = wxMpService.getMaterialService().materialFileBatchGet(VOICE, i * MAX_MATERIAL_COUNT, i * MAX_MATERIAL_COUNT + MAX_MATERIAL_COUNT);
                syncMaterial(fileBatchGetResult, WeixinMpMaterialType.VOICE);
                currentMaterialSync++;
            }

            log.info("Finish weixin material sync");
            applicationContext.publishEvent(new AdminWeixinMaterialSyncFinishEvent(user, totalMaterialSync));
            weixinMaterialSync = false;
            totalMaterialSync = 0;
        } catch (Exception e) {
            log.warn("Can't load weixin materials, cause: {}", e.getMessage());
            weixinMaterialSync = false;
            totalMaterialSync = 0;
        }
    }

    public boolean isWeixinUserSync() {
        return weixinUserSync;
    }

    public long getCurrentUserSync() {
        return currentUserSync;
    }

    public long getTotalUserSync() {
        return totalUserSync;
    }

    public boolean isWeixinMaterialSync() {
        return weixinMaterialSync;
    }

    public long getCurrentMaterialSync() {
        return currentMaterialSync;
    }

    public long getTotalMaterialSync() {
        return totalMaterialSync;
    }

    private void setUserWeixinInfo(User user, WxMpUser wxMpUser) {
        user.setSubscribe(wxMpUser.getSubscribe());
        user.setOpenID(wxMpUser.getOpenId());
        user.setNickname(wxMpUser.getNickname());
        user.setSex(wxMpUser.getSex());
        user.setLanguage(wxMpUser.getLanguage());
        user.setCity(wxMpUser.getCity());
        user.setProvince(wxMpUser.getProvince());
        user.setCountry(wxMpUser.getCountry());
        user.setPortrait(wxMpUser.getHeadImgUrl());
        user.setSubscribeTime(wxMpUser.getSubscribeTime());
        user.setUnionId(wxMpUser.getUnionId());
        user.setSexId(wxMpUser.getSexId());
        user.setRemark(wxMpUser.getRemark());
        user.setGroupId(wxMpUser.getGroupId());
        user.setTagIds(wxMpUser.getTagIds());
    }

    private String nextUsername() {
        String username = RandomStringUtils.randomAlphanumeric(16);

        while (Character.isDigit(username.charAt(0)) || userService.findByUsername(username).isPresent()) {
            username = RandomStringUtils.randomAlphanumeric(16);
        }

        return username;
    }

    /**
     * 解析微信素材图文内容
     *
     * @param content 图文内容
     *
     * @return 解析后的内容
     */
    private String getWeixinArticleContent(String content) {
        Document document = Jsoup.parse(content);
        Elements images = document.select("img");
        images.forEach(element -> {
            String weixinImageSrc = element.attr("data-src");

            if (StringUtils.isNotBlank(weixinImageSrc)) {
                try {
                    HttpResponse response = new HttpRequest().method("GET").set(weixinImageSrc).send();
                    String imageType = StringUtils.substringAfter(weixinImageSrc, "wx_fmt=");
                    Image image = imageService.store(new ByteArrayInputStream(response.bodyBytes()), StringUtils.isNotBlank(imageType) ? imageType : "jpg");
                    element.attr("src", String.format("/api/image/%s.%s", image.getId(), image.getType()));
                } catch (Exception e) {
                    log.error("Can't convert article image: {}", e.getMessage());
                }
            }
        });
        Elements videoIframes = document.select("iframe.video_iframe");
        videoIframes.forEach(element -> {
            element.attr("src", element.attr("data-src"));
            element.attr("width", "100%");
            element.attr("height", "300px");
        });
        return document.toString();
    }

    /**
     * 同步非图文素材
     *
     * @param fileBatchGetResult 素材
     * @param type               素材类型
     */
    private void syncMaterial(WxMpMaterialFileBatchGetResult fileBatchGetResult, WeixinMpMaterialType type) {
        fileBatchGetResult.getItems().forEach(wxMaterialFileBatchGetNewsItem -> {
            log.info("Load weixin material: {}", wxMaterialFileBatchGetNewsItem.getName());
            WeixinMpMaterial next = weixinMpMaterialService.next();
            next.setId(wxMaterialFileBatchGetNewsItem.getMediaId());
            next.setType(type);
            next.setTitle(wxMaterialFileBatchGetNewsItem.getName());
            next.setUrl(wxMaterialFileBatchGetNewsItem.getUrl());
            next.setThumbUrl(siteService.getLogo());
            next.setUpdateTime(wxMaterialFileBatchGetNewsItem.getUpdateTime());
            weixinMpMaterialService.save(next);
        });
    }
}
