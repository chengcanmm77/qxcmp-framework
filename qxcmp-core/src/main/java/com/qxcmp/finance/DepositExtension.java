package com.qxcmp.finance;

import com.qxcmp.core.extension.Extension;

/**
 * 平台充值方式扩展
 *
 * @author Aaric
 */
public interface DepositExtension extends Extension {

    /**
     * 支付方式的图片
     *
     * @return 支付方式的图片
     */
    String getActionImage();

    /**
     * 支付方式的名称
     *
     * @return 支付方式的图片
     */
    String getActionTitle();

    /**
     * 支付操作的提交链接
     *
     * @return 支付操作的提交链接
     */
    String getActionUrl();
}
