package com.qxcmp.mall.web;

import com.google.common.collect.Maps;
import com.qxcmp.mall.*;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.components.mall.*;
import com.qxcmp.web.view.components.mall.ShoppingCart;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.header.*;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.image.Images;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.support.utils.TableHelper;
import com.qxcmp.web.view.views.MobileList;
import com.qxcmp.web.view.views.MobileListItem;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商城前端页面组件生成帮助类
 *
 * @author Aaric
 */
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class MallPageHelper {

    private final CommodityService commodityService;

    private final TableHelper tableHelper;

    /**
     * 商品详情移动端页面组件
     *
     * @param commodity
     *
     * @return
     */
    public Component nextMobileCommodityDetails(Commodity commodity) {

        final Images details = new Images();

        commodity.getDetails().forEach(s -> details.addImage(new Image(s).setFluid()));

        MobileList mobileList = new MobileList();

        if (commodityService.findByParentId(commodity.getParentId()).size() > 1) {
            mobileList.addItem(new MobileListItem(StringUtils.join(commodity.getVersions().stream().map((Function<CommodityVersion, Object>) CommodityVersion::getValue).toArray(), ", "), "已选", "/mall/item/version-select?id=" + commodity.getId()));
        }

        return new Grid()
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Image(commodity.getCover()).setFluid().setBordered())))
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Container()
                        .addComponent(new PageHeader(HeaderType.H4, commodity.getTitle()))
                        .addComponent(new ContentHeader(commodity.getSubTitle(), Size.TINY).setColor(Color.RED))
                        .addComponent(new CommodityPrice(commodity))
                )))
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(mobileList)))
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Segment().addComponent(new HorizontalDivider("商品信息")).addComponent(details))))
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new CommodityActionBar(commodity))));
    }

    public Component nextMobileShoppingCartComponent(List<ShoppingCartItem> items) {

        List<ShoppingCartItem> selectedItems = items.stream().filter(ShoppingCartItem::isSelected).collect(Collectors.toList());

        if (items.isEmpty()) {
            return new Grid().setVerticallyPadded().setContainer()
                    .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Overview(new IconHeader("购物车空空的", new Icon("opencart"))).addLink("去逛逛", "/mall").setAlignment(Alignment.CENTER))));
        }

        return new Grid().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(new ShoppingCart(items, getTotalItemPrice(selectedItems), getTotalItemCount(selectedItems)))));
    }

    public Component nextMobileShoppingCartOrder(List<ShoppingCartItem> items, Consignee consignee) {

        if (items.isEmpty()) {
            return new Grid().setVerticallyPadded().setContainer()
                    .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Overview(new IconHeader("购物车空空的", new Icon("opencart"))).addLink("去逛逛", "/mall").setAlignment(Alignment.CENTER))));
        }

        List<ShoppingCartItem> selectedItems = items.stream().filter(ShoppingCartItem::isSelected).collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            return new Grid().setVerticallyPadded().setContainer()
                    .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new Overview(new IconHeader("你还没有选择商品哦", new Icon("opencart"))).addLink("返回选择", "/mall/cart").setAlignment(Alignment.CENTER))));
        }

        AbstractHeader contentHeader;

        if (Objects.nonNull(consignee)) {
            contentHeader = new ContentHeader(String.format("%s %s", consignee.getConsigneeName(), consignee.getTelephone()), Size.SMALL).setSubTitle(consignee.getAddress());
        } else {
            contentHeader = new ContentHeader("你还未设置收货地址", Size.SMALL).setSubTitle("点击设置收货地址");
        }

        Images images = new Images().setSize(Size.TINY);

        selectedItems.forEach(shoppingCartItem -> images.addImage(new Image(shoppingCartItem.getCommodity().getCover())));

        return new Grid().setVerticallyPadded()
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                        .addComponent(new HorizontalDivider("结算订单"))
                        .addComponent(new MobileList().addItem(new MobileListItem(contentHeader, "/mall/consignee")))
                        .addComponent(new MobileList().addItem(new MobileListItem(images, "/mall/cart/order/details", String.format("共%d件", getTotalItemCount(selectedItems)))))))
                .addItem(new Row().addCol(new Col().addComponent(new ShoppingCartOrderActionBar(getTotalItemPrice(selectedItems)))));
    }

    public Component nextMobileConsignee(List<Consignee> consignees) {
        MobileList mobileList = new MobileList();

        consignees.forEach(consignee -> mobileList.addItem(new MobileListItem(new ContentHeader(String.format("%s %s", consignee.getConsigneeName(), consignee.getTelephone()), Size.SMALL).setSubTitle(consignee.getAddress()), "/mall/consignee/select?id=" + consignee.getId())))
        ;

        return new Grid().setVerticallyPadded()
                .addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                        .addComponent(new HorizontalDivider("收货地址"))
                        .addComponent(mobileList)
                        .addComponent(new Container().addComponent(new Button("新建收货地址", "/mall/consignee/new").setFluid()))
                ));
    }

    public Component nextMobileCashier(CommodityOrder commodityOrder) {

        Map<Object, Object> dictionary = Maps.newLinkedHashMap();

        dictionary.put("订单号", commodityOrder.getId());
        dictionary.put("实付款", new DecimalFormat("￥0.00").format((double) commodityOrder.getActualPayment() / 100));
        dictionary.put("实付积分", commodityOrder.getActualPoint());

        return new Grid().setVerticallyPadded().setContainer()
                .addItem(new Row().addCol(new Col()
                        .addComponent(new PageHeader(HeaderType.H2, "收银台").setAlignment(Alignment.CENTER).setDividing())
                        .addComponent(tableHelper.convert(dictionary))
                        .addComponent(new Divider())
                        .addComponent(new Cashier(commodityOrder))
                ));
    }

    private int getTotalItemPrice(List<ShoppingCartItem> selectedItems) {
        return selectedItems.stream().map(shoppingCartItem -> shoppingCartItem.getCommodity().getSellPrice() * shoppingCartItem.getQuantity()).reduce(0, (sum, price) -> sum + price);
    }

    private int getTotalItemCount(List<ShoppingCartItem> selectedItems) {
        return selectedItems.stream().map(ShoppingCartItem::getQuantity).reduce(0, (sum, quantity) -> sum + quantity);
    }
}
