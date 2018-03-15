package com.qxcmp.weixin;

import com.google.common.collect.Lists;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.weixin.event.WeixinUserSyncFinishEvent;
import com.qxcmp.weixin.event.WeixinUserSyncStartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信公众号同步服务
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WeixinSyncService {

    private final ApplicationContext applicationContext;
    private final UserService userService;
    private final WxMpService wxMpService;

    private boolean weixinUserSync;
    private long currentUserSync;
    private long totalUserSync;

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
            totalUserSync = 0;
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

    public boolean isWeixinUserSync() {
        return weixinUserSync;
    }

    public long getCurrentUserSync() {
        return currentUserSync;
    }

    public long getTotalUserSync() {
        return totalUserSync;
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

}
