package com.qxcmp.shoppingmall.view;

import com.qxcmp.shoppingmall.CommodityOrder;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cashier extends AbstractComponent {

    private CommodityOrder commodityOrder;

    public Cashier(CommodityOrder commodityOrder) {
        this.commodityOrder = commodityOrder;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/mall/shopping-cart";
    }

    @Override
    public String getFragmentName() {
        return "cashier";
    }
}
