package com.qxcmp.mall.web;

import com.qxcmp.exception.FinanceException;
import com.qxcmp.exception.NoBalanceException;
import com.qxcmp.mall.CommodityOrder;
import com.qxcmp.mall.CommodityOrderService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 收银台页面相关路由
 *
 * @author aaric
 */
@RequestMapping("/mall/cashier")
@Controller
@RequiredArgsConstructor
public class CashierController extends QxcmpController {

    private final CommodityOrderService commodityOrderService;

    private final MallPageHelper mallPageHelper;

    @GetMapping("/{id}")
    public ModelAndView cash(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        CommodityOrder commodityOrder = commodityOrderService.findOne(id).filter(order -> StringUtils.equals(order.getUserId(), user.getId())).orElseThrow(() -> new RuntimeException("订单不存在"));

        return page().addComponent(mallPageHelper.nextMobileCashier(commodityOrder))
                .setTitle("收银台")
                .build();
    }

    @PostMapping("")
    public ModelAndView processOrder(@RequestParam String id, @RequestParam String password) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (!new BCryptPasswordEncoder().matches(password, user.getPayPassword())) {
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview(new IconHeader("支付密码错误", new Icon("warning circle"))).addLink("重新支付", "/mall/cashier/" + id)))))
                    .setTitle("收银台")
                    .build();
        }

        try {
            commodityOrderService.pay(id);
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview(new IconHeader("支付成功", new Icon("info circle").setColor(Color.GREEN))).addLink("我的订单", "/mall/order/" + id).setAlignment(Alignment.CENTER)))))
                    .setTitle("收银台")
                    .build();
        } catch (NoBalanceException e) {
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview(new IconHeader("余额不足", new Icon("warning circle"))).addLink("立即充值", "/finance/deposit").setAlignment(Alignment.CENTER)))))
                    .setTitle("收银台")
                    .build();
        } catch (FinanceException e) {
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview(new IconHeader("无法完成支付", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("重新支付", "/mall/cashier/" + id).setAlignment(Alignment.CENTER)))))
                    .setTitle("收银台")
                    .build();
        }
    }
}
