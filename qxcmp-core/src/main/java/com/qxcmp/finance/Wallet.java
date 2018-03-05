package com.qxcmp.finance;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 钱包实体定义
 * <p>
 * 每个钱包对应一个用户,钱包可以目前支持以下东西 <ol> <li>人民币</li> <li>积分</li> </ol>
 *
 * @author aaric
 */
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
    private String userId;

    /**
     * 钱包人民币余额，单位为分
     */
    private int balance;

    /**
     * 平台点数，可以当做平台虚拟货币使用
     */
    private int points;
}
