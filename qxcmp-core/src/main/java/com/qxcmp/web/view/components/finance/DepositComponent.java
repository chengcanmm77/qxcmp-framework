package com.qxcmp.web.view.components.finance;

import com.qxcmp.finance.DepositExtension;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class DepositComponent extends AbstractComponent {

    private final List<DepositExtension> depositExtensions;

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/finance/deposit";
    }

}
