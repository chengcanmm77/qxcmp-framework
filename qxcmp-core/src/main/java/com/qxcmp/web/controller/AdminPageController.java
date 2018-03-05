package com.qxcmp.web.controller;

import com.qxcmp.core.extension.AdminToolPageExtensionPoint;
import com.qxcmp.message.FeedService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.page.AdminAboutPage;
import com.qxcmp.web.page.AdminHomePage;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.TextItem;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.core.Platform.Pages.ABOUT;
import static com.qxcmp.core.Platform.Pages.ADMIN;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN)
@RequiredArgsConstructor
public class AdminPageController extends QxcmpController {

    private final AdminToolPageExtensionPoint adminToolPageExtensionPoint;
    private final FeedService feedService;

    @GetMapping("")
    public ModelAndView homePage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<com.qxcmp.message.Feed> feeds = feedService.findByOwner(user.getId(), pageable);
        return qxcmpPage(AdminHomePage.class, feeds);
    }

    @GetMapping(ABOUT)
    public ModelAndView aboutPage() {
        return qxcmpPage(AdminAboutPage.class);
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