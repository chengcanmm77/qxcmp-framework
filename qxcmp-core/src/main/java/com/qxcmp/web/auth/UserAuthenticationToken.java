package com.qxcmp.web.auth;

import lombok.Data;

import java.util.Date;

/**
 * 用户登录Token
 * <p>
 * 可用于网页授权登录
 *
 * @author Aaric
 */
@Data
public class UserAuthenticationToken {

    /**
     * Token 绑定的用户ID
     */
    private String userId;

    /**
     * Token 过期时间
     */
    private Date dateExpired;

    public UserAuthenticationToken(String userId, Date dateExpired) {
        this.userId = userId;
        this.dateExpired = dateExpired;
    }
}
