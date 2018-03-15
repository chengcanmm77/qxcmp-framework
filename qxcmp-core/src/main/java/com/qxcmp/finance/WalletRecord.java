package com.qxcmp.finance;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 钱包消费记录实体
 * <p>
 * 用于记录用户钱包消费记录
 * <p>
 * 可用于记录余额、积分等各项数据变化
 *
 * @author Aaric
 */
@Entity
@Table
@Data
public class WalletRecord {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 消费该记录的用户
     */
    private String userId;

    /**
     * 消费类型，用于扩展业务
     */
    private String type;

    /**
     * 消费数量，整数为增加，负数为减少
     */
    private int amount;

    /**
     * 消费时间
     */
    private Date date;

    /**
     * 备注
     */
    private String comments;

    /**
     * 查看消费详情对应的Url
     */
    private String url;
}
