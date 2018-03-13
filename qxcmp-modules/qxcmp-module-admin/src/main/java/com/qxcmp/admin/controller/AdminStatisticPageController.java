package com.qxcmp.admin.controller;


import com.qxcmp.admin.page.AdminStatisticPage;
import com.qxcmp.statistics.SearchKeywordsPageResult;
import com.qxcmp.statistics.SearchKeywordsService;
import com.qxcmp.web.QxcmpController;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_STATISTIC_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_STATISTIC_URL)
@RequiredArgsConstructor
public class AdminStatisticPageController extends QxcmpController {

    private final SearchKeywordsService searchKeywordsService;

    @GetMapping("")
    public ModelAndView statisticsKeywordsPage(@RequestParam(defaultValue = "") String range, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(0, pageable.getPageSize());

        String title;
        List<SearchKeywordsPageResult> results;

        switch (range) {
            case "today":
                title = "今日搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate(), pageRequest).getContent();
                break;
            case "day":
                title = "24小时搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusDays(1).toDate(), pageRequest).getContent();
                break;
            case "week":
                title = "近7天搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusWeeks(1).toDate(), pageRequest).getContent();
                break;
            case "month":
                title = "近1月搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(1).toDate(), pageRequest).getContent();
                break;
            case "season":
                title = "近3月搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(3).toDate(), pageRequest).getContent();
                break;
            case "halfYear":
                title = "近半年搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusMonths(6).toDate(), pageRequest).getContent();
                break;
            case "year":
                title = "近一年搜索关键词";
                results = searchKeywordsService.findByDateCreatedAfter(DateTime.now().minusYears(1).toDate(), pageRequest).getContent();
                break;
            default:
                title = "全部搜索关键词";
                results = searchKeywordsService.findAllResult(pageRequest).getContent();
                break;
        }
        return page(AdminStatisticPage.class, title, results);
    }

}
