package com.qxcmp.web.controller;

import com.qxcmp.finance.DepositExtensionPoint;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.components.finance.DepositComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 平台充值默认页面
 *
 * @author Aaric
 */
@Controller
@RequestMapping("/deposit")
@RequiredArgsConstructor
public class DepositPageController extends QxcmpController {

    private final DepositExtensionPoint depositExtensionPoint;

    @GetMapping("")
    public ModelAndView depositGet() {
        return overviewPage(viewHelper.nextOverview("充值中心").addComponent(new DepositComponent(depositExtensionPoint.getExtensions())));
    }
}
