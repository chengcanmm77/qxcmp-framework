package com.qxcmp.shoppingmall;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 商品版本
 * <p>
 * 可用于定义商品颜色、尺码、种类、版本等分类
 * <p>
 * 结合关联商品使用
 *
 * @author Aaric
 */
@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "commodity"})
public class CommodityVersion {

    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Commodity commodity;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 版本值
     */
    private String value;

}
