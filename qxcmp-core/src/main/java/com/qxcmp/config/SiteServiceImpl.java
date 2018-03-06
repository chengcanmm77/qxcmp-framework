package com.qxcmp.config;

import com.qxcmp.core.QxcmpSystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Aaric
 */
@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SystemConfigService systemConfigService;

    /**
     * 平台默认名称
     * <p>
     * 配置在 Spring Boot application.yml 文件里面
     */
    @Value("${platform.application.title}")
    private String applicationName;

    @Override
    public String getTitle() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_TITLE).orElse(applicationName);
    }

    @Override
    public String getDomain() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_DOMAIN).orElse("");
    }

    @Override
    public String getLogo() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_LOGO).orElse(QxcmpSystemConfig.SITE_LOGO_DEFAULT);
    }

    @Override
    public String getFavicon() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_FAVICON).orElse(QxcmpSystemConfig.SITE_FAVICON_DEFAULT);
    }

    @Override
    public String getKeywords() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_KEYWORDS).orElse("");
    }

    @Override
    public String getDescription() {
        return systemConfigService.getString(QxcmpSystemConfig.SITE_DESCRIPTION).orElse("");
    }
}
