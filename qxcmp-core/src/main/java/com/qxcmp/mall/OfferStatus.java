package com.qxcmp.mall;

/**
 * 券的状态
 *
 * @author aaric
 */
public enum OfferStatus {

    NEW("未领取"),
    PICKED("已领取"),
    USED("已使用"),
    EXPIRED("已过期");

    private String value;

    OfferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
