package com.qxcmp.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * IP地址解析器
 * <p>
 * 用于解析请求的原始IP地址
 *
 * @author Aaric
 */
@Component
public class IpAddressResolver {

    private static final String REQUEST_HEADER_FORWARDED = "X-Forwarded-For";
    private static final String REQUEST_HEADER_REAL = "X-Real-IP";
    private static final String REQUEST_HEADER_UNKNOWN = "unKnown";

    /**
     * 获取请求的IP地址
     *
     * @param request 请求
     *
     * @return 请求的真实IP地址
     */
    public String resolve(HttpServletRequest request) {
        String ip = request.getHeader(REQUEST_HEADER_FORWARDED);

        if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(REQUEST_HEADER_UNKNOWN, ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader(REQUEST_HEADER_REAL);

        if (StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase(REQUEST_HEADER_UNKNOWN, ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
