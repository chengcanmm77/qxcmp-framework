package com.qxcmp.shoppingmall.view;

import com.qxcmp.shoppingmall.Commodity;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommodityActionBar extends AbstractComponent {

    private Commodity commodity;

    public CommodityActionBar(Commodity commodity) {
        this.commodity = commodity;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/mall/commodity";
    }

    @Override
    public String getFragmentName() {
        return "action-bar";
    }
}
