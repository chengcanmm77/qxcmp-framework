package com.qxcmp.statistics;

import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.util.IpAddressResolver;
import com.qxcmp.web.filter.QxcmpRequestEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 统计网站访问记录
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AccessHistoryListener {

    private final UserService userService;

    private final AccessHistoryService accessHistoryService;
    private final AccessAddressService accessAddressService;
    private final IpAddressResolver ipAddressResolver;
    private final DeviceResolver deviceResolver;

    @EventListener
    public void onRequest(QxcmpRequestEvent event) {
        try {
            HttpServletRequest request = event.getRequest();

            String ipAddress = ipAddressResolver.resolve(request);
            String requestURI = request.getRequestURI();
            Device device = deviceResolver.resolveDevice(request);

            if (isAccessRequest(requestURI) && !isSpider(ipAddress)) {
                AccessHistory accessHistory = accessHistoryService.next();
                accessHistory.setDateCreated(new Date());
                accessHistory.setIp(ipAddress);
                accessHistory.setUrl(request.getRequestURL().toString());
                accessHistory.setComments(device.isMobile() ? "手机" : (device.isTablet() ? "平板" : "电脑"));

                User user = userService.currentUser();

                if (Objects.nonNull(user)) {
                    accessHistory.setUserId(user.getId());
                }

                accessHistoryService.create(() -> accessHistory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSpider(String ipAddress) {
        return accessAddressService.findOne(ipAddress).map(accessAddress -> Objects.equals(accessAddress.getType(), AccessAddressType.SPIDER)).orElse(false);
    }

    private boolean isAccessRequest(String requestURI) {
        return !StringUtils.startsWith(requestURI, "/api") && !StringUtils.startsWith(requestURI, "/assets");
    }
}
