package com.qxcmp.statistics;

import com.qxcmp.util.IpAddressResolver;
import com.qxcmp.web.filter.QxcmpRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * 访问地址统计
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AccessAddressListener {

    private final AccessAddressService accessAddressService;
    private final IpAddressResolver ipAddressResolver;

    @EventListener
    public void onRequest(QxcmpRequestEvent event) {
        try {
            HttpServletRequest request = event.getRequest();

            String ipAddress = ipAddressResolver.resolve(request);

            Optional<AccessAddress> addressOptional = accessAddressService.findOne(ipAddress);

            if (addressOptional.isPresent()) {
                accessAddressService.update(ipAddress, a -> {
                    a.setDate(new Date());
                });
            } else {
                accessAddressService.create(() -> {
                    AccessAddress accessAddress = accessAddressService.next();

                    accessAddress.setAddress(ipAddress);
                    accessAddress.setDate(new Date());

                    return accessAddress;
                });
            }

        } catch (Exception ignored) {
        }
    }
}
