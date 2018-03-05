package com.qxcmp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 平台配置
 *
 * @author Aaric
 */
@Data
@Component
public class PlatformConfig {

    public static final String ADMIN_URL = "${platform.admin-url}";

    @Value(ADMIN_URL)
    private String adminUrl;
}
