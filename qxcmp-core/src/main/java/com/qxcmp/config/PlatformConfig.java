package com.qxcmp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 平台配置
 *
 * @author Aaric
 */
@Data
@Validated
@Component
@ConfigurationProperties("platform")
public class PlatformConfig {

    private String adminUrl;
}
