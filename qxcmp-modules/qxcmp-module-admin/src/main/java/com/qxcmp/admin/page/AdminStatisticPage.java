package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.statistics.SearchKeywordsPageResult;
import com.qxcmp.web.view.elements.container.Container;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_STATISTIC_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminStatisticPage extends AbstractQxcmpAdminPage {

    private final String title;
    private final List<SearchKeywordsPageResult> results;

    @Override
    public void render() {
        addComponent(new Container().addComponent(viewHelper.nextInfoOverview("关键字统计", title)
                .addComponent(viewHelper.nextTable(objectObjectMap -> results.forEach(searchKeywordsPageResult -> objectObjectMap.put(searchKeywordsPageResult.getTitle(), searchKeywordsPageResult.getCount()))))
                .addLink("今日", ADMIN_STATISTIC_URL + "?range=today")
                .addLink("24小时", ADMIN_STATISTIC_URL + "?range=day")
                .addLink("近7天", ADMIN_STATISTIC_URL + "?range=week")
                .addLink("近1月", ADMIN_STATISTIC_URL + "?range=month")
                .addLink("近3月", ADMIN_STATISTIC_URL + "?range=season")
                .addLink("近半年", ADMIN_STATISTIC_URL + "?range=halfYear")
                .addLink("近一年", ADMIN_STATISTIC_URL + "?range=year")
                .addLink("全部", ADMIN_STATISTIC_URL + "")
        ));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("统计信息");
    }
}
