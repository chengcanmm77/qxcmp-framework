package com.qxcmp.news;

import com.google.common.collect.Sets;
import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.modules.form.FormMethod;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 文章栏目实体
 *
 * @author aaric
 */
@EntityTable(value = "我的栏目", name = "user", action = QXCMP_BACKEND_URL + "/news/user/channel",
        rowActions = {
                @RowAction(value = "文章管理", action = "article"),
                @RowAction(value = "查看", action = "details"),
                @RowAction(value = "编辑", action = "edit")
        })
@EntityTable(value = "栏目管理", name = "admin", action = QXCMP_BACKEND_URL + "/news/channel",
        tableActions = @TableAction(value = "新建栏目", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "预览", action = "preview"),
                @RowAction(value = "编辑", action = "edit"),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@Entity
@Table
@Data
public class Channel {

    /**
     * 主键，由平台自动生成
     */
    @Id
    @GeneratedValue
    @TableField("ID")
    private Long id;

    /**
     * 栏目显示名称
     */
    @TableField("名称")
    @Column(unique = true)
    private String name;

    @TableField(value = "封面", image = true, order = Integer.MIN_VALUE)
    private String cover;

    /**
     * 栏目描述
     */
    private String description;

    /**
     * 栏目自定义页面
     */
    @Lob
    private String content;

    /**
     * 栏目自定义页面
     */
    @Lob
    private String contentQuill;

    /**
     * 栏目所有者
     */
    @ManyToOne
    @TableField(value = "所有者", fieldSuffix = ".username")
    private User owner;

    /**
     * 栏目管理者
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @TableField(value = "管理员", collectionEntityIndex = "username")
    private Set<User> admins = Sets.newHashSet();

    /**
     * 栏目类型
     */
    @ElementCollection
    private Set<String> catalog = Sets.newHashSet();
}
