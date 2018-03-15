package com.qxcmp.finance;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.UUID;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 钱包实体定义
 * <p>
 * 每个钱包对应一个用户,钱包可以目前支持以下东西 <ol> <li>人民币</li> <li>积分</li> </ol>
 *
 * @author aaric
 */
@EntityTable("用户钱包")
@Entity
@Table
@Data
public class Wallet {

    /**
     * 钱包主键，由平台生成，使用{@link UUID#randomUUID()}生成
     */
    @Id
    private String id;

    /**
     * 钱包所属的用户主键
     */
    @Column(unique = true)
    @NotNull
    @TableField(value = "用户ID", enableUrl = true, urlPrefix = QXCMP_ADMIN_URL + "/user/", urlSuffix = "/details", urlTarget = AnchorTarget.BLANK)
    private String userId;

    /**
     * 钱包人民币余额，单位为分
     */
    @TableField("钱包余额")
    private int balance;

    /**
     * 平台点数，可以当做平台虚拟货币使用
     */
    @TableField("钱包点数")
    private int points;

    @TableFieldRender("balance")
    public TableData renderFeeFiled() {
        return new TableData(new DecimalFormat("￥0.00").format((double) balance / 100));
    }
}
