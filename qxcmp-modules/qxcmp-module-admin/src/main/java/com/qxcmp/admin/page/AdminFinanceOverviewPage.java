package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.finance.*;
import com.qxcmp.web.view.elements.statistic.Statistic;
import com.qxcmp.web.view.elements.statistic.Statistics;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.ItemCount;
import com.qxcmp.web.view.support.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_FINANCE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminFinanceOverviewPage extends AbstractQxcmpAdminPage {

    private final List<Wallet> wallets;
    private final List<WalletRecord> walletRecords;
    private final List<DepositOrder> depositOrders;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_FINANCE, "");
        addComponent(viewHelper.nextInfoOverview("财务统计")
                .addComponent(getBalanceDetails()));
    }

    private Statistics getBalanceDetails() {
        return new Statistics().setCount(ItemCount.FIVE).setSize(Size.TINY)
                .addStatistic(new Statistic("充值总额", getTotalBalance()).setColor(Color.GREEN))
                .addStatistic(new Statistic("消费总额", getConsumedBalance()).setColor(Color.GREY))
                .addStatistic(new Statistic("消费点数", getConsumedPoints()).setColor(Color.GREY))
                .addStatistic(new Statistic("未消费额", getRemainBalance()).setColor(Color.RED))
                .addStatistic(new Statistic("剩余点数", getRemainPoints()).setColor(Color.RED))
                ;
    }

    private String getTotalBalance() {
        return depositOrders.stream().filter(depositOrder -> depositOrder.getStatus().equals(OrderStatusEnum.FINISHED)).map(DepositOrder::getFee).reduce(Integer::sum).map(integer -> new DecimalFormat("￥0.00").format((double) integer / 100)).orElse("-");
    }

    private String getConsumedBalance() {
        return walletRecords.stream().filter(walletRecord -> StringUtils.equals(walletRecord.getType(), WalletRecordType.BALANCE.name())).map(WalletRecord::getAmount).reduce(Integer::sum).map(integer -> new DecimalFormat("￥0.00").format((double) integer / 100)).orElse("-");
    }

    private String getConsumedPoints() {
        return walletRecords.stream().filter(walletRecord -> StringUtils.equals(walletRecord.getType(), WalletRecordType.POINT.name())).map(WalletRecord::getAmount).reduce(Integer::sum).map(String::valueOf).orElse("-");
    }

    private String getRemainBalance() {
        return wallets.stream().map(Wallet::getBalance).reduce(Integer::sum).map(integer -> new DecimalFormat("￥0.00").format((double) integer / 100)).orElse("-");
    }

    private String getRemainPoints() {
        return wallets.stream().map(Wallet::getPoints).reduce(Integer::sum).map(String::valueOf).orElse("-");
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("财务统计");
    }

}
