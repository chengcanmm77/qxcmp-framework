package com.qxcmp.redeem;

import com.qxcmp.exception.BaseQXCMPException;
import lombok.Getter;

/**
 * 兑换码使用异常
 *
 * @author Aaric
 */
@Getter
public class RedeemKeyException extends BaseQXCMPException {

    private final Code code;

    public RedeemKeyException(Code code) {
        this.code = code;
    }

    public enum Code {
        /**
         * 兑换码不存在
         */
        NOT_EXIST,

        /**
         * 兑换码已过期
         */
        EXPIRED
    }
}
