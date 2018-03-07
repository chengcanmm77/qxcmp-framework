package com.qxcmp.message;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.modules.form.FormMethod;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * @author Aaric
 */
@EntityTable(value = "网站通知", action = QXCMP_ADMIN_URL + "/message/site/notification",
        tableActions = @TableAction(value = "新建网站通知", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "编辑", action = "edit"),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        }
)
@Entity
@Table
@Data
public class SiteNotification {

    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 发布者ID
     */
    private String ownerId;

    /**
     * 类型
     */
    @TableField("类型")
    private String type;

    /**
     * 生效时间
     */
    @TableField("生效时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateStart;

    /**
     * 结束时间
     */
    @TableField("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateEnd;

    /**
     * 通知标题
     */
    @TableField("标题")
    private String title;

    /**
     * 通知内容
     */
    private String content;

}
