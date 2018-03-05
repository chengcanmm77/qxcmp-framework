package com.qxcmp.mall.web;

import com.qxcmp.exception.ShoppingCartServiceException;
import com.qxcmp.mall.*;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

@Controller
@RequestMapping("/mall/cart")
@RequiredArgsConstructor
public class ShoppingCartController extends QxcmpController {

    private final ShoppingCartService shoppingCartService;

    private final ShoppingCartItemService shoppingCartItemService;

    private final ConsigneeService consigneeService;

    private final CommodityOrderService commodityOrderService;

    private final MallPageHelper mallPageHelper;

    @GetMapping("")
    private ModelAndView cartPage() {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<ShoppingCartItem> items = shoppingCartItemService.findByUser(user.getId());

        return page().addComponent(mallPageHelper.nextMobileShoppingCartComponent(items))
                .setTitle("我的购物车")
                .hideMobileBottomMenu()
                .build();
    }

    @PostMapping("/item")
    public ModelAndView modifyItem(MallItemForm form) {

        shoppingCartItemService.update(form.getId(), shoppingCartItem -> {
            shoppingCartItem.setSelected(form.isSelected());
            shoppingCartItem.setQuantity(form.getQuantity());
        });

        return redirect("/mall/cart");
    }

    @PostMapping("/item/add")
    public ModelAndView addItem(@RequestParam long id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        try {
            shoppingCartItemService.addCommodity(user.getId(), id);
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview("加入购物车成功").addLink("返回", String.format("/mall/item/%d.html", id)).addLink("我的购物车", "/mall/cart").setAlignment(Alignment.CENTER)))))
                    .setTitle("我的购物车")
                    .build();
        } catch (ShoppingCartServiceException e) {
            return page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                    .addComponent(new Overview(new IconHeader("加入购物车失败", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")))))
                    .setTitle("我的购物车")
                    .build();
        }
    }

    @GetMapping("/order")
    public ModelAndView order() throws ShoppingCartServiceException {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<ShoppingCartItem> items = shoppingCartItemService.findByUser(user.getId());

        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());

        return page().addComponent(mallPageHelper.nextMobileShoppingCartOrder(items, consigneeService.findOne(shoppingCart.getConsigneeId()).orElse(null)))
                .setTitle("我的购物车")
                .hideMobileBottomMenu()
                .build();
    }

    @PostMapping("/order")
    public ModelAndView orderPage() throws ShoppingCartServiceException {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<ShoppingCartItem> items = shoppingCartItemService.findSelectedByUser(user.getId());

        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());

        return commodityOrderService.order(user.getId(), items, shoppingCart).map(commodityOrder -> redirect("/mall/cashier/" + commodityOrder.getId()))
                .orElse(page().addComponent(new Grid().setVerticallyPadded().setContainer().addItem(new Row().addCol(new Col()
                        .addComponent(new Overview(new IconHeader("下单失败", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall/cart")))))
                        .setTitle("我的购物车")
                        .build());

    }
}
