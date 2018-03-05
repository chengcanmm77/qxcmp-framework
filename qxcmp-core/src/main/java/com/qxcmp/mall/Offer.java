package com.qxcmp.mall;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台券实体
 * <p>
 * 可以代表不同类型券，优惠券，使用券等等
 * <p>
 * 商户通过商城后台发布券，发布以后用户可以领取在自己的名下
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class Offer {

    /**
     * 券主键，由平台自动生成
     */
    @Id
    private String id;

    /**
     * 券名称，可以用作具体业务逻辑的处理
     */
    @NotNull
    private String name;

    /**
     * 券类型，可以用作具体业务逻辑的处理
     */
    @NotNull
    private String type;

    /**
     * 券商，发行该券的商户ID
     */
    @NotNull
    private String vendorID;

    /**
     * 券所属的用户，一个用户可以有多个券，如果该值为空则表示券还没有被领取
     */
    private String userId;

    /**
     * 券使用说明
     */
    private String description;

    /**
     * 券包含的金钱和点数数量
     */
    private BigDecimal amount;

    /**
     * 券发放日期，自动设置
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    /**
     * 券领取时间，自动设置
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateReceived;

    /**
     * 券有效开始时间，需要在原型里面设置
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateStart;

    /**
     * 券过期时间，需要在原型里面设置
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateEnd;

    /**
     * 券使用时间，自动设置
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateUsed;

    /**
     * 券的状态
     */
    private OfferStatus status;

}

