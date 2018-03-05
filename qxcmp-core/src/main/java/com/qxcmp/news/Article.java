package com.qxcmp.news;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 平台文章实体
 *
 * @author aaric
 */
@EntityTable(value = "我的文章", name = "user", action = QXCMP_BACKEND_URL + "/news/user/article",
        tableActions = @TableAction(value = "新建文章", action = "new", primary = true),
        rowActions = {@RowAction(value = "预览", action = "preview", target = AnchorTarget.BLANK)})
@EntityTable(value = "草稿箱", name = "userDraft", action = QXCMP_BACKEND_URL + "/news/user/article",
        tableActions = @TableAction(value = "新建文章", action = "new", primary = true),
        batchActions = {
                @BatchAction(value = "批量申请审核", action = "audit", secondary = true),
                @BatchAction(value = "批量删除", action = "remove", color = Color.RED)
        },
        rowActions = {
                @RowAction(value = "申请审核", action = "audit"),
                @RowAction(value = "预览", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "编辑", action = "edit"),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@EntityTable(value = "审核中文章", name = "userAuditing", action = QXCMP_BACKEND_URL + "/news/user/article",
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "撤销申请", action = "repeal", method = FormMethod.POST)
        })
@EntityTable(value = "未通过文章", name = "userRejected", action = QXCMP_BACKEND_URL + "/news/user/article",
        batchActions = {
                @BatchAction(value = "批量申请审核", action = "audit", secondary = true)
        },
        rowActions = {
                @RowAction(value = "重新申请审核", action = "audit"),
                @RowAction(value = "编辑", action = "edit"),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@EntityTable(value = "已发布文章", name = "userPublished", action = QXCMP_BACKEND_URL + "/news/user/article",
        batchActions = {
                @BatchAction(value = "批量禁用", action = "disable", secondary = true)
        },
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "禁用", action = "disable", method = FormMethod.POST)
        })
@EntityTable(value = "已禁用文章", name = "userDisabled", action = QXCMP_BACKEND_URL + "/news/user/article",
        batchActions = {
                @BatchAction(value = "批量删除", action = "remove", color = Color.RED)
        },
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "启用", action = "enable", method = FormMethod.POST),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@EntityTable(value = "待审核文章", name = "auditing", action = QXCMP_BACKEND_URL + "/news/article",
        batchActions = {
                @BatchAction(value = "批量发布", action = "publish", color = Color.GREEN),
                @BatchAction(value = "批量驳回", action = "reject", color = Color.RED)
        },
        rowActions = @RowAction(value = "开始审核", action = "audit"))
@EntityTable(value = "已发布文章", name = "published", action = QXCMP_BACKEND_URL + "/news/article",
        batchActions = {
                @BatchAction(value = "批量禁用", action = "disable", secondary = true)
        },
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "禁用", action = "disable", method = FormMethod.POST)
        })
@EntityTable(value = "已禁用文章", name = "disabled", action = QXCMP_BACKEND_URL + "/news/article",
        batchActions = {
                @BatchAction(value = "批量删除", action = "remove", color = Color.RED)
        },
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK),
                @RowAction(value = "启用", action = "enable", method = FormMethod.POST),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@EntityTable(value = "栏目文章管理", name = "userChannel",
        rowActions = {
                @RowAction(value = "查看", action = "preview", target = AnchorTarget.BLANK, primary = true),
                @RowAction(value = "禁用文章", action = "disable", method = FormMethod.POST, color = Color.RED),
                @RowAction(value = "启用文章", action = "enable", method = FormMethod.POST, color = Color.GREEN)
        })
@Entity
@Table
@Data
public class Article {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 文章创建者用户ID
     */
    private String userId;

    /**
     * 文章标题
     */
    @TableField("标题")
    private String title;

    /**
     * 文章作者
     */
    @TableField("作者")
    private String author;

    /**
     * 文章摘要
     */
    private String digest;

    /**
     * 文章封面图片Url
     */
    @TableField(value = "封面", image = true, order = Integer.MIN_VALUE)
    private String cover;

    /**
     * 编辑器内容，用于编辑时使用
     */
    @Lob
    private String content;

    /**
     * HTML内容，用于只读时使用
     */
    @Lob
    private String contentQuill;

    /**
     * 文章是否被发布
     */
    @Enumerated
    @TableField(name = "user", value = "状态", fieldSuffix = ".name")
    private ArticleStatus status;

    /**
     * 文章审核人ID
     */
    private String auditor;

    /**
     * 文章申请审核说明
     */
    private String auditRequest;

    /**
     * 文章申请审核响应，如通过审核，说明通过原因，如驳回审核，说明驳回原因
     */
    private String auditResponse;

    /**
     * 文章禁用人ID
     */
    private String disableUser;

    /**
     * 文章创建时间
     */
    private Date dateCreated;

    /**
     * 上次修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateModified;

    /**
     * 提交审计的时间
     */
    private Date dateAuditing;

    /**
     * 文章驳回时间
     */
    private Date dateRejected;

    /**
     * 文章发布时间
     */
    private Date datePublished;

    /**
     * 文章禁用日期
     */
    private Date dateDisabled;

    /**
     * 文章所属的栏目
     */
    @ManyToMany
    @TableField(value = "所属栏目", collectionEntityIndex = "name", maxCollectionCount = 3, enableUrl = true, urlPrefix = QXCMP_BACKEND_URL + "/news/user/channel/", urlEntityIndex = "id", urlSuffix = "article")
    private List<Channel> channels = Lists.newArrayList();

    /**
     * 文章访问历史
     */
    @ElementCollection
    private Set<String> viewRecord = Sets.newHashSet();

    /**
     * 文章流量
     */
    @TableField(value = "访问量", name = "userPublished")
    @TableField(value = "访问量", name = "userDisabled")
    @TableField(value = "访问量", name = "published")
    @TableField(value = "访问量", name = "disabled")
    private int viewCount;

    @RowActionCheck("禁用文章")
    public boolean canPerformChannelArticleDisable() {
        return status.equals(ArticleStatus.PUBLISHED);
    }

    @RowActionCheck("启用文章")
    public boolean canPerformChannelArticleEnable() {
        return status.equals(ArticleStatus.DISABLED);
    }
}
