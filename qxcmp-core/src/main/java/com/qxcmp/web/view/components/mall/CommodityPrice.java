package com.qxcmp.web.view.components.mall;

import com.qxcmp.mall.Commodity;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品价格组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class CommodityPrice extends AbstractComponent {

    private Commodity commodity;

    public CommodityPrice(Commodity commodity) {
        this.commodity = commodity;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/mall/commodity";
    }

    @Override
    public String getFragmentName() {
        return "price";
    }
}
