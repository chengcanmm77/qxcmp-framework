package com.qxcmp.shoppingmall;

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

    PAYED("已付款"),
    CANCELLING("取消中"),
    EXCEPTION("订单异常"),
    PAYING("待付款"),
    CANCELED("已取消"),
    FINISHED("已完成"),
    NEW("新订单");

    private String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
