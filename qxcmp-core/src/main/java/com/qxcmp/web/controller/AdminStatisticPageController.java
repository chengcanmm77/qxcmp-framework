package com.qxcmp.web.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qxcmp.audit.ActionException;
import com.qxcmp.statistics.*;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminStatisticBaiduLinkForm;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.modules.table.Table;
import com.qxcmp.web.view.modules.table.TableHead;
import com.qxcmp.web.view.modules.table.TableHeader;
import com.qxcmp.web.view.modules.table.TableRow;
import com.qxcmp.web.view.modules.table.dictionary.TextValueCell;
import com.qxcmp.web.view.support.Wide;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_ADMIN_URL + "/statistic")
@RequiredArgsConstructor
public class AdminStatisticPageController extends QxcmpController {

    private static final int MAX_BAIDU_LINK_PUSH_SIZE = 2000;

    private final AccessHistoryService accessHistoryService;
    private final AccessAddressService accessAddressService;
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

    @GetMapping("/access/history")
    public ModelAndView accessHistoryPage(Pageable pageable) {
        return page().addComponent(convertToTable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateCreated"), accessHistoryService))
                .setBreadcrumb("控制台", "", "网站统计", "statistic", "访问历史记录")
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_ACCESS_HISTORY)
                .build();
    }

    @GetMapping("/access/address")
    public ModelAndView accessAddressPage(Pageable pageable) {
        return page().addComponent(convertToTable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "date"), accessAddressService))
                .setBreadcrumb("控制台", "", "网站统计", "statistic", "访问地址管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_ACCESS_ADDRESS)
                .build();
    }

    @PostMapping("/access/address/{id}/normal")
    public ResponseEntity<RestfulResponse> accessAddressNormal(@PathVariable String id) {
        return accessAddressService.findOne(id)
                .map(address -> {
                    RestfulResponse restfulResponse = audit("修改访问地址为普通", context -> {
                        try {
                            accessAddressService.update(id, accessAddress -> accessAddress.setType(AccessAddressType.NORMAL));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/access/address/{id}/black")
    public ResponseEntity<RestfulResponse> accessAddressBlack(@PathVariable String id) {
        return accessAddressService.findOne(id)
                .map(address -> {
                    RestfulResponse restfulResponse = audit("修改访问地址为黑名单", context -> {
                        try {
                            accessAddressService.update(id, accessAddress -> accessAddress.setType(AccessAddressType.BLACK));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/access/address/{id}/white")
    public ResponseEntity<RestfulResponse> accessAddressWhite(@PathVariable String id) {
        return accessAddressService.findOne(id)
                .map(address -> {
                    RestfulResponse restfulResponse = audit("修改访问地址为白名单", context -> {
                        try {
                            accessAddressService.update(id, accessAddress -> accessAddress.setType(AccessAddressType.WHITE));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/access/address/{id}/spider")
    public ResponseEntity<RestfulResponse> accessAddressSpider(@PathVariable String id) {
        return accessAddressService.findOne(id)
                .map(address -> {
                    RestfulResponse restfulResponse = audit("修改访问地址为网络爬虫", context -> {
                        try {
                            accessAddressService.update(id, accessAddress -> accessAddress.setType(AccessAddressType.SPIDER));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @GetMapping("/baidu/link")
    public ModelAndView statisticBaiduLinkPage(final AdminStatisticBaiduLinkForm form) {

        form.setSite(siteService.getDomain());

        return page().addComponent(convertToForm(form))
                .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_BAIDU_LINK)
                .setBreadcrumb("控制台", "", "网站统计", "statistic", "百度链接提交")
                .build();
    }

    @PostMapping("/baidu/link")
    public ModelAndView statisticBaiduLinkPage(@Valid final AdminStatisticBaiduLinkForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    .setVerticalNavigation(NAVIGATION_ADMIN_STATISTIC, NAVIGATION_ADMIN_STATISTIC_BAIDU_LINK)
                    .setBreadcrumb("控制台", "", "网站统计", "statistic", "百度链接提交")
                    .build();
        }

        return submitForm(form, context -> {

            Map<Object, Object> result = Maps.newLinkedHashMap();
            context.put("result", result);

            try {
                List<String> links = Lists.newArrayList();

                links.addAll(form.getLinks());

                if (StringUtils.isNotBlank(form.getLinkPrefix())) {

                    String suffix = StringUtils.isBlank(form.getLinkSuffix()) ? "" : form.getLinkSuffix();

                    for (int i = form.getLinkItemStart(); i <= form.getLinkItemEnd(); i++) {
                        links.add(String.format("%s%d%s", form.getLinkPrefix(), i, suffix));
                    }
                }

                int successCount = 0;
                int remainCount = 0;
                int round = (int) Math.ceil(links.size() / MAX_BAIDU_LINK_PUSH_SIZE);
                for (int i = 0; i < round; i++) {

                    StringBuilder stringBuilder = new StringBuilder();

                    for (int j = i * MAX_BAIDU_LINK_PUSH_SIZE; j < i * MAX_BAIDU_LINK_PUSH_SIZE + MAX_BAIDU_LINK_PUSH_SIZE; j++) {
                        try {
                            stringBuilder.append(links.get(j)).append("\n");
                        } catch (Exception e) {
                            break;
                        }
                    }

                    HttpResponse response = HttpRequest.post(String.format("http://data.zz.baidu.com/urls?site=%s&token=%s", form.getSite(), form.getToken())).body(stringBuilder.toString()).send();

                    int code = response.statusCode();

                    Map<String, Object> json = new Gson().fromJson(response.body(), new TypeToken<Map<String, Object>>() {
                    }.getType());

                    if (code == HttpStatus.OK.value()) {
                        successCount += Double.valueOf(json.get("success").toString());
                        remainCount = Double.valueOf(json.get("remain").toString()).intValue();
                    }
                }

                result.put("推送成功条数", successCount);
                result.put("当天剩余次数", remainCount);

            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }

        }, (stringObjectMap, overview) -> overview.addComponent(convertToTable((Map<Object, Object>) stringObjectMap.get("result"))));
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
