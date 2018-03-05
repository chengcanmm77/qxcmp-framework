package com.qxcmp.web.filter;

import com.qxcmp.exception.BlackListException;
import com.qxcmp.statistics.AccessAddressService;
import com.qxcmp.statistics.AccessAddressType;
import com.qxcmp.util.IpAddressResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 平台通用拦截器
 *
 * @author Aaric
 */
public class QxcmpFilter extends GenericFilterBean {

    private final ApplicationContext applicationContext;
    private final AccessAddressService accessAddressService;
    private final IpAddressResolver ipAddressResolver;

    public QxcmpFilter(ApplicationContext applicationContext, AccessAddressService accessAddressService, IpAddressResolver ipAddressResolver) {
        this.applicationContext = applicationContext;
        this.accessAddressService = accessAddressService;
        this.ipAddressResolver = ipAddressResolver;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        applicationContext.publishEvent(new QxcmpRequestEvent(request, response));

        if (accessAddressService.findOne(ipAddressResolver.resolve(request)).map(accessAddress -> Objects.equals(accessAddress.getType(), AccessAddressType.BLACK)).orElse(false)) {
            throw new BlackListException();
        }

        filterChain.doFilter(request, response);
    }
}