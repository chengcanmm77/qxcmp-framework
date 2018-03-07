package com.qxcmp.message;


import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Alignment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 站内消息
 *
 * @author Aaric
 */
@EntityTable(value = "我的站内消息", action = QXCMP_ADMIN_URL + "/profile/message", disableFilter = true,
        rowActions = {
                @RowAction(value = "查看", action = "details"),
                @RowAction(value = "标为已读", action = "read", method = FormMethod.POST),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST)
        })
@Entity
@Table
@Data
public class InnerMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String sender;

    @TableField("标题")
    private String title;

    private String content;

    @TableField("发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @TableField("状态")
    private boolean unread;

    @TableFieldRender("unread")
    public TableData renderStatusField() {
        final TableData tableData = new TableData();
        if (unread) {
            tableData.setContent("未读");
        } else {
            tableData.setContent("已读");
        }

        tableData.setAlignment(Alignment.CENTER);
        return tableData;
    }

    @RowActionCheck("标为已读")
    public boolean canPerformReadAction() {
        return unread;
    }
}
