package com.qxcmp.message;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.table.TableData;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 短信发送记录实体
 *
 * @author Aaric
 */
@EntityTable(value = "发送记录", action = QXCMP_ADMIN_URL + "/sms", disableFilter = true,
        rowActions = @RowAction(value = "详情", action = "details"))
@Entity
@Table
@Data
public class SmsSendRecord {

    /**
     * 短信发送请求ID
     */
    @Id
    private String id;

    /**
     * 发送的手机号
     */
    @Lob
    @TableField("发送条数")
    private String phones;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信业务名称
     */
    @TableField("业务名称")
    private String templateName;

    /**
     * 短信模板
     */
    private String templateCode;

    /**
     * 短信参数
     */
    private String templateParameter;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 请求状态码
     */
    @TableField("发送状态")
    private String code;

    /**
     * 状态码描述
     */
    private String message;

    @TableField("发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @TableFieldRender("phones")
    public TableData renderPhones() {
        return new TableData(String.valueOf(phones.split(",").length));
    }

    @TableFieldRender("code")
    public TableData renderCode() {
        if (StringUtils.equals("OK", code)) {
            return new TableData(new Icon("check green circle"));
        } else {
            return new TableData(new Icon("warning red circle"));
        }
    }
}
