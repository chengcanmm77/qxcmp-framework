package com.qxcmp.region;

import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 地区实体
 * <p>
 * 代表一个行政区或者地区
 *
 * @author Aaric
 */
@EntityTable(value = "地区列表", action = QXCMP_ADMIN_URL + "/settings/region", entityIndex = "code",
        rowActions = {
                @RowAction(value = "禁用", action = "disable", method = FormMethod.POST, color = Color.RED),
                @RowAction(value = "启用", action = "enable", method = FormMethod.POST, color = Color.GREEN),
                @RowAction(value = "添加子地区", action = "new", color = Color.BLACK, target = AnchorTarget.BLANK)
        })
@Entity
@Table
@Data
public class Region {

    /**
     * 地区代码
     */
    @Id
    @TableField("地区代码")
    private String code;

    /**
     * 上级地区代码
     */
    @TableField("上级地区代码")
    private String parent;

    /**
     * 地区级别
     */
    @Enumerated
    @TableField("地区级别")
    private RegionLevel level;

    /**
     * 地区名称
     */
    @TableField("地区名称")
    private String name;

    /**
     * 是否禁用
     * <p>
     * 禁用以后将无法获取本地区以及所有下级地区
     */
    @TableField("是否禁用")
    private boolean disable;

    @RowActionCheck("禁用")
    public boolean canPerformDisableAction() {
        return !disable;
    }

    @RowActionCheck("启用")
    public boolean canPerformEnableAction() {
        return disable;
    }

    @RowActionCheck("添加子地区")
    public boolean canPerformNewAction() {
        return level.equals(RegionLevel.CITY);
    }

    @TableFieldRender("disable")
    public TableData renderDisableField() {
        final TableData tableData = new TableData();
        if (disable) {
            tableData.addComponent(new Icon("warning circle").setColor(Color.RED));
        } else {
            tableData.addComponent(new Icon("check circle").setColor(Color.GREEN));
        }
        return tableData;
    }

    @TableFieldRender("level")
    public TableData renderLevelField() {
        final TableData tableData = new TableData();
        switch (level) {
            case STATE:
                tableData.setContent("国家");
                break;
            case PROVINCE:
                tableData.setContent("省、省级市");
                break;
            case CITY:
                tableData.setContent("市、市级县");
                break;
            case COUNTY:
                tableData.setContent("区、县");
                break;
        }
        return tableData;
    }
}
