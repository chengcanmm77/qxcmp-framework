package com.qxcmp.advertisement;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.modules.form.FormMethod;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;

/**
 * 广告实体
 * <p>
 * 代表了一张广告
 *
 * @author aaric
 */
@EntityTable(value = "广告管理", action = ADMIN_ADVERTISEMENT_URL,
        tableActions = @TableAction(value = "新建", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "编辑", action = "edit"),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@Entity
@Table
@Data
public class Advertisement {

    /**
     * 广告实体主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    @TableField("ID")
    private Long id;

    /**
     * 广告类型，用于区分在不同位置显示
     */
    @TableField("类型")
    private String type;

    /**
     * 广告名称
     */
    @TableField("名称")
    private String title;

    /**
     * 广告图片
     */
    @TableField(value = "图片", image = true, order = Integer.MIN_VALUE)
    private String image;

    /**
     * 广告链接
     */
    @TableField("链接")
    private String link;

    /**
     * 广告顺序
     */
    @TableField("顺序")
    private int adOrder;

    /**
     * 是否在新窗口打开
     */
    private boolean blank;
}
