package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.message.SmsSendRecordService;
import com.qxcmp.message.SmsTemplateExtensionPoint;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.statistic.Statistic;
import com.qxcmp.web.view.elements.statistic.Statistics;
import com.qxcmp.web.view.modules.table.dictionary.ComponentCell;
import com.qxcmp.web.view.page.Page;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.ItemCount;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS;
import static com.qxcmp.core.QxcmpSystemConfig.MESSAGE_SMS_ACCESS_KEY;
import static com.qxcmp.core.QxcmpSystemConfig.MESSAGE_SMS_ACCESS_SECRET;
import static com.qxcmp.message.SmsAliyunService.ALIYUM_SMS_CODE_SUCCSEE;

/**
 * @author Aaric
 */
@Page
@RequiredArgsConstructor
public class AdminSmsOverviewPage extends AbstractQxcmpAdminPage {

    private final Pageable pageable;

    private SmsSendRecordService smsSendRecordService;
    private SmsTemplateExtensionPoint smsTemplateExtensionPoint;

    @Override
    public void render() {
        DateTime now = DateTime.now();
        Date today = now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        Date day = DateTime.now().minusDays(1).toDate();
        Date month = DateTime.now().minusMonths(1).toDate();
        addComponent(new Container().addComponent(viewHelper.nextInfoOverview("短信服务")
                .addComponent(new HorizontalDivider("数据统计"))
                .addComponent(new Statistics().setCount(ItemCount.FOUR)
                        .addStatistic(new Statistic("今日发送", smsSendRecordService.countByDateAfter(today).toString()).setColor(Color.GREEN))
                        .addStatistic(new Statistic("24小时发送", smsSendRecordService.countByDateAfter(day).toString()).setColor(Color.GREEN))
                        .addStatistic(new Statistic("本月发送", smsSendRecordService.countByDateAfter(month).toString()).setColor(Color.ORANGE))
                        .addStatistic(new Statistic("全部发送", String.valueOf(smsSendRecordService.count((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), ALIYUM_SMS_CODE_SUCCSEE)))).setColor(Color.GREY))
                )
                .addComponent(new HorizontalDivider("服务配置"))
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("AccessKey", new ComponentCell(new Icon(getIcon(systemConfigService.getString(MESSAGE_SMS_ACCESS_KEY).orElse("")))));
                    objectObjectMap.put("AccessSecret", new ComponentCell(new Icon(getIcon(systemConfigService.getString(MESSAGE_SMS_ACCESS_SECRET).orElse("")))));
                    smsTemplateExtensionPoint.getExtensions().forEach(extension -> objectObjectMap.put(extension.getName() + "-" + extension.getTemplateCode(), extension.getContent()));
                }))
                .addComponent(new Divider())
                .addComponent(viewHelper.nextEntityTable(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateCreated"), smsSendRecordService, request))
        ));
        setMenu(ADMIN_MENU_SMS, "");
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("短信服务");
    }

    @Autowired
    public void setSmsSendRecordService(SmsSendRecordService smsSendRecordService) {
        this.smsSendRecordService = smsSendRecordService;
    }

    @Autowired
    public void setSmsTemplateExtensionPoint(SmsTemplateExtensionPoint smsTemplateExtensionPoint) {
        this.smsTemplateExtensionPoint = smsTemplateExtensionPoint;
    }

    private String getIcon(String value) {
        if (StringUtils.isBlank(value)) {
            return "warning red circle";
        } else {
            return "check green circle";
        }
    }
}
