package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.elements.message.InfoMessage;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.elements.statistic.Statistic;
import com.qxcmp.web.view.elements.statistic.Statistics;
import com.qxcmp.web.view.support.ItemCount;
import com.qxcmp.weixin.WeixinService;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminUserTablePage extends AbstractQxcmpAdminPage {

    private final Pageable pageable;
    private final WeixinService weixinService;

    @Override
    public void render() {

        if (weixinService.getSyncService().isWeixinUserSync()) {
            addComponent(new InfoMessage(String.format("微信用户正在同步中... 当前进度为 %d/%d，请稍后刷新查看", weixinService.getSyncService().getCurrentUserSync(), weixinService.getSyncService().getTotalUserSync())).setCloseable());
        }

        addComponent(viewHelper.nextOverview("用户管理")
                .addComponent(new Segment()
                        .addComponent(new HorizontalDivider("今日统计"))
                        .addComponent(getStatistics()))
                .addComponent(new HorizontalDivider("用户列表"))
                .addComponent(viewHelper.nextEntityTable(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateLogin"), userService, request))
        );
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("用户管理");
    }

    private Statistics getStatistics() {
        Date dateTarget = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        return new Statistics().setCount(ItemCount.THREE)
                .addStatistic(new Statistic("用户总数", String.valueOf(userService.count())))
                .addStatistic(new Statistic("今日新增", Long.toString(userService.findByDateCreate(dateTarget).size())))
                .addStatistic(new Statistic("今日登陆", Long.toString(userService.findByDateLogin(dateTarget).size())));
    }
}
