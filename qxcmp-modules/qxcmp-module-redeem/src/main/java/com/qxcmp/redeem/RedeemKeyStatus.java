package com.qxcmp.redeem;

/**
 * 兑换码状态
 *
 * @author aaric
 */
public enum RedeemKeyStatus {
    USED("已使用"),
    NEW("未使用"),
    EXPIRED("已过期");

    private String value;

    RedeemKeyStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
