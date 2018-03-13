package com.qxcmp.admin.controller;


import com.qxcmp.statistics.AccessHistoryPageResult;
import com.qxcmp.statistics.AccessHistoryService;
import com.qxcmp.statistics.SearchKeywordsPageResult;
import com.qxcmp.statistics.SearchKeywordsService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.modules.table.Table;
import com.qxcmp.web.view.modules.table.TableHead;
import com.qxcmp.web.view.modules.table.TableHeader;
import com.qxcmp.web.view.modules.table.TableRow;
import com.qxcmp.web.view.modules.table.dictionary.TextValueCell;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_ADMIN_URL + "/statistic")
@RequiredArgsConstructor
public class AdminStatisticPageController extends QxcmpController {

    private final AccessHistoryService accessHistoryService;
    private final SearchKeywordsService searchKeywordsService;

    @GetMapping("")
    public ModelAndView statisticPage() {
        return page()
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, "")
                .setBreadcrumb("控制台", "", "网站统计")
                .build();
    }

    @GetMapping("/pages")
    public ModelAndView statisticPagesPage(Pageable pageable) {

        PageRequest pageRequest = new PageRequest(0, pageable.getPageSize());

        List<AccessHistoryPageResult> todayResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().withHourOfDay(0).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> dayResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusDays(1).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> weekResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusWeeks(1).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> monthResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusMonths(1).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> seasonResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusMonths(3).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> halfYearResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusMonths(6).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> yearResult = accessHistoryService.findByDateCreatedAfter(DateTime.now().minusYears(1).toDate(), pageRequest).getContent();
        List<AccessHistoryPageResult> allResult = accessHistoryService.findAllResult(pageRequest).getContent();

        return page().addComponent(new Grid()
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("今日", todayResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近24小时", dayResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近7天", weekResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近30天", monthResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近3月", seasonResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近6月", halfYearResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("近1年", yearResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertAccessResultToTable("全部", allResult)))
        )
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_PAGES)
                .setBreadcrumb("控制台", "", "网站统计", "statistic", "页面访问统计")
                .build();
    }

    @GetMapping("/keywords")
    public ModelAndView statisticsKeywordsPage(Pageable pageable) {

        PageRequest pageRequest = new PageRequest(0, pageable.getPageSize());

        List<SearchKeywordsPageResult> todayResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().withHourOfDay(0).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> dayResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusDays(1).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> weekResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusWeeks(1).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> monthResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(1).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> seasonResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(3).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> halfYearResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(6).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> yearResult = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusYears(1).toDate(), pageRequest).getContent();
        List<SearchKeywordsPageResult> allResult = searchKeywordsService.findAllResult(pageRequest).getContent();

        return page().addComponent(new Grid()
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("今日", todayResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近24小时", dayResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近7天", weekResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近30天", monthResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近3月", seasonResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近6月", halfYearResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("近1年", yearResult)))
                .addItem(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(convertSearchResultToTable("全部", allResult)))
        )
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_KEYWORDS)
                .setBreadcrumb("控制台", "", "网站统计", "statistic", "页面访问统计")
                .build();
    }

    private Table convertAccessResultToTable(String title, List<AccessHistoryPageResult> results) {
        Table table = convertToTable(stringObjectMap -> results.forEach(result -> stringObjectMap.put(new TextValueCell(result.getUrl(), result.getUrl()), result.getNbr())));
        TableHeader tableHeader = new TableHeader();
        tableHeader.addRow(new TableRow().addCell(new TableHead(title)));
        table.setHeader(tableHeader);
        return table;
    }

    private Table convertSearchResultToTable(String title, List<SearchKeywordsPageResult> results) {
        Table table = convertToTable(stringObjectMap -> results.forEach(result -> stringObjectMap.put(result.getTitle(), result.getCount())));
        TableHeader tableHeader = new TableHeader();
        tableHeader.addRow(new TableRow().addCell(new TableHead(title)));
        table.setHeader(tableHeader);
        return table;
    }
}
