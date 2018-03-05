package com.qxcmp.web.controller;

import com.jcabi.manifests.Manifests;
import com.qxcmp.core.QxcmpConfiguration;
import com.qxcmp.core.extension.AdminToolPageExtensionPoint;
import com.qxcmp.message.FeedService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.page.AdminHomePage;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.TextItem;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL)
@RequiredArgsConstructor
public class AdminPageController extends QxcmpController {

    private final AdminToolPageExtensionPoint adminToolPageExtensionPoint;
    private final FeedService feedService;

    @GetMapping("")
    public ModelAndView homePage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<com.qxcmp.message.Feed> feeds = feedService.findByOwner(user.getId(), pageable);

//        return page().addComponent(new VerticallyDividedGrid().setContainer().setCentered()
//                .addItem(new Row()
//                        .addCol(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(new Segment()
//                                .addComponent(new ContentHeader("我的动态", Size.NONE).setDividing())
//                                .addComponent(new Feed(feeds.getContent()))
//                                .addComponent(new Pagination("", feeds.getNumber() + 1, (int) feeds.getTotalElements(), feeds.getSize()))
//                        ))
//                )
//        ).build();

        return qxcmpPage(AdminHomePage.class, feeds);
    }

    @GetMapping("/about")
    public ModelAndView aboutPage() {
        return page()
                .addComponent(new TextContainer().addComponent(new Overview(new PageHeader(HeaderType.H1, QXCMP)).addComponent(convertToTable(stringObjectMap -> {
                    String appVersion = QxcmpConfiguration.class.getPackage().getImplementationVersion();
                    String appBuildDate = "development";
                    String appStartUpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(applicationContext.getStartupDate()));

                    try {
                        appBuildDate = Manifests.read("Build-Date");
                    } catch (Exception ignored) {

                    }

                    if (StringUtils.isBlank(appVersion)) {
                        appVersion = "暂无版本信息";
                    }

                    stringObjectMap.put("平台版本", appVersion);
                    stringObjectMap.put("构建日期", appBuildDate);
                    stringObjectMap.put("启动日期", appStartUpDate);
                    stringObjectMap.put("软件版本", System.getProperty("java.version"));
                })).addLink("返回", QXCMP_BACKEND_URL)))
                .setBreadcrumb("控制台", "", "关于")
                .build();
    }

    @GetMapping("/tools")
    public ModelAndView toolsPage() {

        List list = new List().setSelection();

        adminToolPageExtensionPoint.getExtensions().forEach(adminToolPageExtension -> list.addItem(new TextItem(adminToolPageExtension.getTitle()).setUrl(adminToolPageExtension.getUrl())));

        return page().addComponent(new TextContainer().addComponent(new Segment().setAlignment(Alignment.CENTER)
                .addComponent(new PageHeader(HeaderType.H1, "系统工具").setDividing())
                .addComponent(list)))
                .setBreadcrumb("控制台", "", "系统工具")
                .build();
    }
}