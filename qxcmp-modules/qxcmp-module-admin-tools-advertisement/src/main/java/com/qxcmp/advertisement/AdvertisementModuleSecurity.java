package com.qxcmp.advertisement;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;
import static com.qxcmp.advertisement.AdvertisementModule.PRIVILEGE_ADMIN_ADVERTISEMENT;

/**
 * @author Aaric
 */
@Configuration
public class AdvertisementModuleSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_ADVERTISEMENT_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_ADVERTISEMENT_URL + "/**").hasRole(PRIVILEGE_ADMIN_ADVERTISEMENT);
    }
}
