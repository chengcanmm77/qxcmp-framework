package com.qxcmp.weixin;

import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信服务
 * <p>
 * 提供微信模块常用的服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeixinService {

    /**
     * 微信被动回复文本信息最大长度限制
     */
    public static final int MAX_WEIXIN_MP_TEXT_RESPONSE_LENGTH = 2400;

    private final UserService userService;
    private final WxMpService wxMpService;
    private final WeixinSyncService syncService;

    /**
     * 微信网页授权登录
     * <p>
     * 根据code获取用户OpenId，如果查询到用户信息，则设置OpenId对应的用户为登录状态，并设置登录时间
     *
     * @param code Oauth2 认证码
     *
     * @return 如果认证成功返回认证以后的用户，否则返回 empty
     */
    public Optional<User> loadOauth2User(String code) {

        if (Objects.nonNull(userService.currentUser()) || StringUtils.isBlank(code)) {
            return Optional.empty();
        }

        try {
            WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(accessToken, null);
            Optional<User> userOptional = userService.findByOpenID(wxMpUser.getOpenId());

            if (!userOptional.isPresent()) {
                syncService.syncUser(wxMpUser);
            }

            userService.findByOpenID(wxMpUser.getOpenId()).ifPresent(user -> {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
                userService.update(user.getId(), u -> u.setDateLogin(new Date()));
            });
            return userOptional;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public WeixinSyncService getSyncService() {
        return syncService;
    }

}
