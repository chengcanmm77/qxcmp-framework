package com.qxcmp.mall;


import com.qxcmp.exception.FinanceException;

/**
 * 平台转账服务
 * <p>
 * 此服务是除了充值服务以外，唯一一个能够修改用户钱包金额的服务，并保存每一次转账记录
 *
 * @author aaric
 */
public interface TransferService {

    /**
     * 通用人民币转账接口
     *
     * @param from 转账原始用户ID
     * @param to   转账目标用户ID
     * @param fee  转账金额
     *
     * @return 转账交易记录
     *
     * @throws FinanceException 如果转账失败，抛出财务类异常
     */
    TransferRecord transfer(String from, String to, int fee) throws FinanceException;

    /**
     * 通用转账接口
     *
     * @param from    转账原始用户ID
     * @param to      转账目标用户ID
     * @param fee     转账金额
     * @param feeType 货币类型
     *
     * @return 转账交易记录
     *
     * @throws FinanceException 如果转账失败，抛出财务类异常
     */
    TransferRecord transfer(String from, String to, int fee, String feeType) throws FinanceException;
}
