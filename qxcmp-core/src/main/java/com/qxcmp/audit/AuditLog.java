package com.qxcmp.audit;

import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 审计日志实体
 * <p>
 * 用于记录用户操作或者系统特定操作，审计日志需要包含When，Where，How，Who，What这些信息
 * <p>
 * 审计日志方便后期对系统故障的调试和查错
 *
 * @author aaric
 */
@EntityTable(value = "系统日志", action = QXCMP_ADMIN_URL + "/audit",
        rowActions = @RowAction(value = "查看", action = "details"))
@Entity
@Table
@Data
public class AuditLog {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    @TableField("ID")
    private Long id;

    /**
     * 日志时间
     */
    @TableField("时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    /**
     * 日志请求链接
     */
    private String url;

    /**
     * 日志名称
     */
    @TableField("名称")
    private String title;

    /**
     * 日志当事人
     */
    @ManyToOne
    @TableField(value = "当事人", fieldSuffix = ".username")
    private User owner;

    /**
     * 日志内容
     */
    @Lob
    private String content;

    /**
     * 备注信息
     */
    @Lob
    private String comments;

    /**
     * 结果状态
     */
    @TableField(value = "结果")
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 操作上下文
     */
    @Transient
    private Map<String, Object> actionContext;

    /**
     * 结果状态枚举
     */
    public enum Status {

        SUCCESS("成功"),
        FAILURE("失败");

        private String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @TableFieldRender("status")
    public TableData renderStatusField() {
        final TableData tableData = new TableData();
        if (status.equals(Status.SUCCESS)) {
            tableData.addComponent(new Icon("check circle").setColor(Color.GREEN));
        } else {
            tableData.addComponent(new Icon("warning circle").setColor(Color.RED));
        }

        tableData.setAlignment(Alignment.CENTER);
        return tableData;
    }
}
