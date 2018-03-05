package com.qxcmp.account;

import lombok.Builder;
import lombok.Data;

/**
 * 账户注册模块组件
 * <p>
 * 用于保存注册模块相关信息
 *
 * @author aaric
 */
@Data
@Builder
public class AccountComponent {

    /**
     * 注册模块名称
     */
    private String registerName;

    /**
     * 找回模块名称
     */
    private String resetName;

    /**
     * 账户注册Url
     */
    private String registerUrl;

    /**
     * 账户找回Url
     */
    private String resetUrl;

    /**
     * 是有具有账户找回模块
     */
    private boolean disableReset;
}
