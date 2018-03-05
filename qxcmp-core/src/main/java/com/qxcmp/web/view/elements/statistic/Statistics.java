package com.qxcmp.web.view.elements.statistic;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.ItemCount;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

/**
 * @author Aaric
 */
@Getter
public class Statistics extends AbstractComponent {

    private boolean horizontal;
    private ItemCount count = ItemCount.NONE;

    private List<Statistic> statistics = Lists.newArrayList();

    public Statistics addStatistic(Statistic statistic) {
        statistics.add(statistic);
        return this;
    }

    public Statistics addStatistics(Collection<Statistic> statisticCollection) {
        statistics.addAll(statisticCollection);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/statistic";
    }

    @Override
    public String getFragmentName() {
        return "group";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        StringBuilder stringBuilder = new StringBuilder();

        if (horizontal) {
            stringBuilder.append(" horizontal");
        }

        return stringBuilder.append(count.toString()).toString();
    }

    @Override
    public String getClassSuffix() {
        return "statistics";
    }

    public Statistics setHorizontal() {
        this.horizontal = true;
        return this;
    }

    public Statistics setCount(ItemCount count) {
        this.count = count;
        return this;
    }
}
