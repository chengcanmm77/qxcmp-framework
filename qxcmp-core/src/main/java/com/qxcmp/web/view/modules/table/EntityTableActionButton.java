package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 表单按钮
 * <p>
 * 按钮将以表单的形式提交
 *
 * @author Aaric
 */
@Getter
@Setter
public class EntityTableActionButton extends AbstractTableActionButton {

    public EntityTableActionButton(String text) {
        super(text);
    }

    public EntityTableActionButton(String text, String url) {
        super(text, url);
    }

    public EntityTableActionButton(String text, String url, AnchorTarget anchorTarget) {
        super(text, url, anchorTarget);
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/table";
    }

    @Override
    public String getFragmentName() {
        return "action-button";
    }

    @Override
    public String getClassSuffix() {
        return "table-action " + super.getClassSuffix();
    }
}
