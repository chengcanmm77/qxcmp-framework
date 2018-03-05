package com.qxcmp.web.view.elements;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

/**
 * 国旗图标
 *
 * @author Aaric
 */
@Getter
public class Flag extends AbstractComponent {

    private String flag;

    public Flag(String flag) {
        this.flag = flag;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/flag";
    }
}
