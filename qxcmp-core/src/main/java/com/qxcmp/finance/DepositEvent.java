package com.qxcmp.finance;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户充值事件
 * <p>
 * 当用户充值成功以后发送该事件
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public class DepositEvent {

    private DepositOrder depositOrder;
}
