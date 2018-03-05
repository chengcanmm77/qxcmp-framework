package com.qxcmp.shoppingmall;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 转账记录实体
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class TransferRecord {

    /**
     * 主键，由平台自动生成，使用订单号方式
     */
    @Id
    private String id;

    /**
     * 转账原始用户ID
     */
    private String sourceUserId;

    /**
     * 转账目标用户ID
     */
    private String targetUserId;

    /**
     * 转账金额
     */
    private int fee;

    /**
     * 转账货币类型
     */
    private String feeType;

    /**
     * 转账时间
     */
    private Date dateCreated;

    /**
     * 转账结果
     */
    @Enumerated
    private Status status;

    /**
     * 转账备注消息
     */
    private String comments;

    public enum Status {
        SUCCESS,
        FAILED
    }
}
