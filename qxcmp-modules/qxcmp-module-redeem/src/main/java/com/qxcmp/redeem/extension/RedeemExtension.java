package com.qxcmp.redeem.extension;

import com.qxcmp.core.extension.Extension;

import java.util.Set;

/**
 * 兑换码扩展
 *
 * @author Aaric
 */
public interface RedeemExtension extends Extension {

    /**
     * 支持的兑换码业务名称
     *
     * @return 业务名称
     */
    Set<String> getTypes();
}
