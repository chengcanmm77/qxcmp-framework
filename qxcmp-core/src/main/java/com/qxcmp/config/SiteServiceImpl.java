package com.qxcmp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.qxcmp.core.QxcmpSystemConfig.*;

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
        return systemConfigService.getString(SITE_TITLE).orElse(applicationName);
    }

    @Override
    public String getProtocol() {
        return systemConfigService.getString(SITE_PROTOCOL).orElse(SITE_PROTOCOL_DEFAULT);
    }

    @Override
    public String getDomain() {
        return systemConfigService.getString(SITE_DOMAIN).orElse("");
    }

    @Override
    public String getHomeUrl() {
        return getProtocol().toLowerCase() + "://" + getDomain();
    }

    @Override
    public String getLogo() {
        return systemConfigService.getString(SITE_LOGO).orElse(SITE_LOGO_DEFAULT);
    }

    @Override
    public String getFavicon() {
        return systemConfigService.getString(SITE_FAVICON).orElse(SITE_FAVICON_DEFAULT);
    }

    @Override
    public String getKeywords() {
        return systemConfigService.getString(SITE_KEYWORDS).orElse("");
    }

    @Override
    public String getDescription() {
        return systemConfigService.getString(SITE_DESCRIPTION).orElse("");
    }
}
