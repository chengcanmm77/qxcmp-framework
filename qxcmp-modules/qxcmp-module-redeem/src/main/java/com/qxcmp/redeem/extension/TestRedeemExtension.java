package com.qxcmp.redeem.extension;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestRedeemExtension implements RedeemExtension {
    @Override
    public Set<String> getTypes() {
        return ImmutableSet.of("业务1", "业务2");
    }
}
