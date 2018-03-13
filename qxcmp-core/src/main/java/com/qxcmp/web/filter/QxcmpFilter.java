package com.qxcmp.web.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 平台通用拦截器
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public class QxcmpFilter extends GenericFilterBean {

    private final ApplicationContext applicationContext;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        applicationContext.publishEvent(new QxcmpRequestEvent(request, response));
        filterChain.doFilter(request, response);
    }
}