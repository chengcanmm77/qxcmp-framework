package com.qxcmp.article;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 700)
@Configuration
public class NewsModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_NEWS = "新闻模块基本权限";
    public static final String PRIVILEGE_NEWS_DESCRIPTION = "可以进入新闻模块";
    public static final String PRIVILEGE_NEWS_CHANNEL = "新闻栏目管理权限";
    public static final String PRIVILEGE_NEWS_CHANNEL_DESCRIPTION = "可以管理新闻栏目";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT = "文章审核权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT_DESCRIPTION = "可以对平台申请审核的文章进行审核，决定发布或驳回";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT = "文章管理权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT_DESCRIPTION = "可以对平台已发布文章进行管理，禁用或者删除";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_NEWS_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_NEWS_URL + "/article/**").hasRole(PRIVILEGE_NEWS)
                .antMatchers(ADMIN_NEWS_URL + "/channel/**").hasRole(PRIVILEGE_NEWS_CHANNEL)
                .antMatchers(ADMIN_NEWS_URL + "/**").hasRole(PRIVILEGE_NEWS)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
