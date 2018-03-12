package com.qxcmp.redeem;

import com.qxcmp.config.SystemConfigLoader;
import org.springframework.stereotype.Component;

/**
 * @author Aaric
 */
@Component
public class RedeemModuleSystemConfig implements SystemConfigLoader {

    /**
     * 是否开启兑换码使用功能
     */
    public static String ENABLE;
    public static Boolean ENABLE_DEFAULT = true;

    /**
     * 兑换码默认过期时间(秒)
     */
    public static String DEFAULT_EXPIRE_DURATION;
    public static Integer DEFAULT_EXPIRE_DURATION_DEFAULT = 7200;
}
