package com.qxcmp.shoppingmall;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 购物车项目
 * <p>
 * 用于存放用于的购物车内容
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class ShoppingCartItem {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 该项目是否被用户勾选
     */
    private boolean selected;

    /**
     * 要购买的商品数量
     */
    private int quantity;

    /**
     * 商品加入时的价格
     */
    private int originPrice;

    /**
     * 加入时间
     */
    private Date dateCreated;

    /**
     * 修改时间
     */
    private Date dateModified;

    /**
     * 关联的商品
     * <p>
     * 使用联合查询，可以动态获取商品信息
     */
    @ManyToOne
    private Commodity commodity;
}
