package com.qxcmp.finance;

/**
 * 平台订单状态枚举类型
 * <p>
 * 可以表示以下订单状态
 * <p>
 * <ol> <li>新订单： 由系统刚创建订单对象时的状态</li> <li>待付款： 订单已经生成，等待用户付款</li> <li>已付款： 用户已经付款，等待系统处理订单</li> <li>取消中：
 * 用户申请取消订单后的状态</li> <li>已取消： 审核要取消的订单，同意以后订单将标记为已取消状态，已取消的订单将不能继续使用</li> <li>已完成：
 * 表明订单已经完成，可能是系统后台标记完成，也可以是用户手动确认订单完成</li> </ol>
 *
 * @author aaric
 */
public enum OrderStatusEnum {

    /**
     * 已付款但是还未处理的订单
     */
    PAYED("已付款"),

    /**
     * 用户申请取消还未处理的订单
     */
    CANCELLING("取消中"),

    /**
     * 订单状态错误
     */
    EXCEPTION("订单异常"),

    /**
     * 用户已下单但是还未付款
     */
    PAYING("待付款"),

    /**
     * 已经成功取消的订单
     */
    CANCELED("已取消"),

    /**
     * 已经完成的订单
     */
    FINISHED("已完成"),

    /**
     * 系统刚生成的订单
     */
    NEW("新订单"),

    /**
     * 已经过期的订单
     */
    EXPIRED("已过期");

    private String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
