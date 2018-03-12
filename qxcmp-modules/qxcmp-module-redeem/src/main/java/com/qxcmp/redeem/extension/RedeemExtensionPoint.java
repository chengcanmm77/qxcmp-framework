package com.qxcmp.redeem.extension;

import com.google.common.collect.Sets;
import com.qxcmp.core.extension.AbstractExtensionPoint;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 兑换码扩展点
 *
 * @author Aaric
 */
@Component
public class RedeemExtensionPoint extends AbstractExtensionPoint<RedeemExtension> {

    /**
     * 获取当前平台支持的兑换码业务名称
     *
     * @return 业务列表
     */
    public Set<String> getTypes() {
        Set<String> types = Sets.newHashSet();
        getExtensions().forEach(redeemExtension -> types.addAll(redeemExtension.getTypes()));
        return types;
    }
}
