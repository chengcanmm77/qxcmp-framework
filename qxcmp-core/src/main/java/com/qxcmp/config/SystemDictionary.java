package com.qxcmp.config;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableField;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 系统字典配置
 *
 * @author aaric
 */
@EntityTable(value = "系统字典", action = QXCMP_BACKEND_URL + "/settings/dictionary", entityIndex = "name",
        rowActions = @RowAction(value = "编辑", action = "edit", primary = true))
@Entity
@Table
@Data
public class SystemDictionary {

    @Id
    @TableField("名称")
    private String name;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @OrderBy("priority")
    @TableField(value = "项目", collectionEntityIndex = "name")
    private List<SystemDictionaryItem> items = Lists.newArrayList();
}
