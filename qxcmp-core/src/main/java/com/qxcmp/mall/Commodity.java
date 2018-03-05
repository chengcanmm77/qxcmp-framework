package com.qxcmp.mall;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 平台商品
 *
 * @author aaric
 */
@EntityTable(value = "商品管理", name = "userStoreCommodity", action = QXCMP_BACKEND_URL + "/mall/user/store/commodity",
        tableActions = @TableAction(value = "添加商品", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "编辑", action = "edit", color = Color.BLACK),
                @RowAction(value = "复制", action = "copy", method = FormMethod.POST, color = Color.ORANGE),
                @RowAction(value = "下架", action = "disable", method = FormMethod.POST, color = Color.RED),
                @RowAction(value = "上架", action = "enable", method = FormMethod.POST, color = Color.GREEN)
        })
@Entity
@Table
@Data
public class Commodity {

    /**
     * 商品主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    @TableField("商品编号")
    private Long id;

    /**
     * 父商品主键
     * <p>
     * 用于关联多型号商品
     */
    private Long parentId;

    /**
     * 商品封面
     */
    @TableField(value = "封面", image = true, order = Integer.MIN_VALUE)
    private String cover;

    /**
     * 商品介绍相册
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> albums = Sets.newLinkedHashSet();

    /**
     * 商品图文详情
     * <p>
     * 当前平台商品尽支持图片描述
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> details = Sets.newLinkedHashSet();

    /**
     * 商品所属店铺
     */
    @ManyToOne
    private Store store;

    /**
     * 商品标题
     */
    @TableField("标题")
    private String title;

    /**
     * 商品子标题
     */
    private String subTitle;

    /**
     * 商品分类
     * <p>
     * 如，电脑、手机等
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @TableField(value = "商品类别", maxCollectionCount = 3)
    private Set<String> catalogs = Sets.newLinkedHashSet();

    /**
     * 商品原始价格(分) - 用于界面显示
     */
    private int originPrice;

    /**
     * 商品结算价格(分) - 用于结算订单
     */
    @TableField("售价")
    private int sellPrice;

    /**
     * 商品当前库存
     */
    private int inventory;

    /**
     * 销量
     */
    @TableField("销量")
    private int sales;

    /**
     * 商品是否下架，如果商品下架以后，将不会出现在商品池中
     */
    @TableField("是否上架")
    private boolean disabled;

    @ManyToOne(fetch = FetchType.EAGER)
    private User userModified;

    private Date dateCreated;

    private Date dateModified;

    private Date dateDisabled;

    /**
     * 商品自定义属性
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> customProperties = Maps.newLinkedHashMap();

    /**
     * 商品版本
     */
    @OneToMany(mappedBy = "commodity", fetch = FetchType.EAGER)
    private List<CommodityVersion> versions = Lists.newArrayList();

    /**
     * 该商品是否为积分兑换商品
     */
    private boolean pointOnly;

    /**
     * 兑换该商品需要的积分
     */
    private int point;

    @RowActionCheck("下架")
    public boolean canPerformDisable() {
        return !disabled;
    }

    @RowActionCheck("上架")
    public boolean canPerformEnable() {
        return disabled;
    }

    @TableFieldRender("disabled")
    public TableData renderStatusField() {
        final TableData tableData = new TableData();
        if (disabled) {
            tableData.addComponent(new Icon("warning circle").setColor(Color.RED));
        } else {
            tableData.addComponent(new Icon("check circle").setColor(Color.GREEN));
        }

        tableData.setAlignment(Alignment.CENTER);
        return tableData;
    }

    @TableFieldRender("sellPrice")
    public TableData renderSellPrice() {
        return renderPriceCell(sellPrice);
    }

    private TableData renderPriceCell(int price) {
        TableData tableData = new TableData(new DecimalFormat("￥0.00").format((double) price / 100));
        tableData.setAlignment(Alignment.RIGHT);
        return tableData;
    }
}
