package com.qxcmp.security;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.RowActionCheck;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.Color;
import lombok.Data;

import javax.persistence.*;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 权限实体定义
 *
 * @author aaric
 */
@EntityTable(value = "权限列表", action = QXCMP_ADMIN_URL + "/security/privilege",
        rowActions = {
                @RowAction(value = "激活", action = "enable", method = FormMethod.POST, color = Color.GREEN),
                @RowAction(value = "禁用", action = "disable", method = FormMethod.POST, color = Color.RED, showConfirmDialog = true, confirmDialogTitle = "禁用权限", confirmDialogDescription = "确认要禁用该权限，权限禁用后将不能使用权限所控制的功能")
        })
@Entity
@Table
@Data
public class Privilege {

    /**
     * 权限主键，有框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 权限名称
     */
    @TableField("权限名称")
    @Column(unique = true)
    private String name;

    /**
     * 权限描述信息
     */
    @TableField("权限描述")
    private String description;

    /**
     * 权限是否禁用，若权限禁用以后，该权限关联的所有操作将会失效
     */
    private boolean disabled;

    public Privilege() {
    }

    public Privilege(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @RowActionCheck("激活")
    public boolean canPerformEnable() {
        return this.disabled;
    }

    @RowActionCheck("禁用")
    public boolean canPerformDisable() {
        return !this.disabled;
    }
}
