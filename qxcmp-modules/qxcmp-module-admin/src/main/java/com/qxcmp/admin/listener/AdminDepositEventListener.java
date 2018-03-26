package com.qxcmp.admin.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.finance.DepositEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_USER_URL;
import static com.qxcmp.admin.QxcmpAdminModuleSecurity.PRIVILEGE_FINANCE_DEPOSIT;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminDepositEventListener {

    private final UserService userService;
    private final FeedService feedService;
    private final SiteService siteService;

    @EventListener
    public void onDepositEvent(DepositEvent event) {
        try {
            User user = userService.findOne(event.getDepositOrder().getUserId()).orElseThrow(RuntimeException::new);
            feedService.feedForUserGroup(PRIVILEGE_FINANCE_DEPOSIT, user,
                    String.format("%s 充值了 %s 元 <a href='%s%s/%s/details'>查看详情</a>",
                            user.getDisplayName(),
                            new DecimalFormat("￥0.00").format((double) event.getDepositOrder().getFee() / 100),
                            siteService.getHomeUrl(),
                            ADMIN_USER_URL,
                            user.getId()));
        } catch (Exception ignored) {

        }
    }
}
