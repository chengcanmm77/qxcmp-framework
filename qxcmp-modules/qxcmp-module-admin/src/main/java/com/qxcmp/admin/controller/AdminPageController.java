package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.page.AdminHomePage;
import com.qxcmp.message.FeedService;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_URL)
@RequiredArgsConstructor
public class AdminPageController extends QxcmpAdminController {

    private final FeedService feedService;

    @GetMapping("")
    public ModelAndView homePage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<com.qxcmp.message.Feed> feeds = feedService.findByOwner(user.getId(), pageable);
        return page(AdminHomePage.class, feeds);
    }
}