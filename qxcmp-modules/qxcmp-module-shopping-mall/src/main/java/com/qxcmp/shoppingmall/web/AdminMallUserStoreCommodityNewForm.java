package com.qxcmp.shoppingmall.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qxcmp.shoppingmall.CommodityVersion;
import com.qxcmp.web.view.annotation.form.*;
import com.qxcmp.web.view.modules.form.support.KeyValueEntity;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author Aaric
 */
@Form(value = "添加商品", submitText = "确认添加")
@Data
public class AdminMallUserStoreCommodityNewForm {

    @DynamicField(value = "商品分类", section = "商品分类", tooltip = "分类用于定义关联商品，所有关联商品ID相同的商品会被关联起来，分类可以定义商品的颜色、尺码、型号等属性", itemHeaders = {"分类名称", "分类值"}, itemFields = {"name", "value"})
    private List<CommodityVersion> versions = Lists.newArrayList();

    @AvatarField(value = "商品封面", section = "商品信息")
    private String cover;

    @AlbumField(value = "商品相册", maxCount = 8, section = "商品信息")
    private Set<String> albums = Sets.newLinkedHashSet();

    @AlbumField(value = "商品详情", maxCount = 20, section = "商品信息")
    private Set<String> details = Sets.newLinkedHashSet();

    @TextInputField(value = "标题", section = "商品信息", required = true, autoFocus = true)
    private String title;

    @TextInputField(value = "子标题", section = "商品信息")
    private String subTitle;

    @NumberField(value = "商品原价", section = "商品价格", tooltip = "单位：分")
    private int originPrice;

    @NumberField(value = "商品售价", section = "商品价格", tooltip = "单位：分")
    private int sellPrice;

    @BooleanField(value = "积分兑换", section = "商品价格", tooltip = "该商品将使用积分兑换方式")
    private boolean pointOnly;

    @NumberField(value = "兑换积分", section = "商品价格", tooltip = "兑换需要积分")
    private int point;

    @TextSelectionField(value = "商品类别", section = "商品属性")
    private Set<String> catalogs = Sets.newLinkedHashSet();

    @NumberField(value = "商品库存", section = "商品属性", tooltip = "库存为零以后商品将不能销售")
    private int inventory;

    @BooleanField(value = "是否下架", section = "商品属性")
    private boolean disabled;

    @DynamicField(value = "自定义属性", section = "额外属性", itemHeaders = {"属性名称", "属性值"}, itemFields = {"key", "value"})
    private List<KeyValueEntity> customProperties = Lists.newArrayList();
}
