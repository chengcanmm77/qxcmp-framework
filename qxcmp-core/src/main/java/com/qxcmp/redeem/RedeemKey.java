package com.qxcmp.redeem;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableAction;
import com.qxcmp.web.view.annotation.table.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 兑换码实体
 *
 * @author aaric
 */
@EntityTable(value = "兑换码管理", action = QXCMP_BACKEND_URL + "/redeem",
        tableActions = @TableAction(value = "生成兑换码", action = "generate", primary = true),
        rowActions = @RowAction(value = "查看", action = "details"))
@Entity
@Table
@Data
public class RedeemKey {

    /**
     * 主键，也为兑换码的值，有平台自动生成
     */
    @Id
    private String id;

    /**
     * 使用兑换码的用户ID
     */
    private String userId;

    /**
     * 兑换码类型，用于具体业务逻辑处理
     */
    @TableField("业务名称")
    private String type;

    /**
     * 兑换码内容，业务处理需要用到的数据
     */
    private String content;

    /**
     * 兑换码状态
     */
    @TableField(value = "使用状态", fieldSuffix = ".value")
    private RedeemKeyStatus status;

    /**
     * 兑换码使用时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("使用时间")
    private Date dateUsed;

    /**
     * 兑换码创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("创建时间")
    private Date dateCreated;

    /**
     * 兑换码过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("过期时间")
    private Date dateExpired;
}
