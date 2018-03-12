package com.qxcmp.redeem;

import com.qxcmp.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 使用兑换码事件
 */
@Data
@AllArgsConstructor
public class RedeemKeyEvent {

    /**
     * 使用兑换码的用户
     */
    private User user;

    /**
     * 使用的兑换码
     */
    private RedeemKey redeemKey;
}
