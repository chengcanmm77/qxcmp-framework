package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTableActionButton extends Button {

    private FormMethod method = FormMethod.POST;

    private boolean showConfirm;

    private String confirmDialogTitle;

    private String confirmDialogDescription;

    public AbstractTableActionButton(String text) {
        super(text);
    }

    public AbstractTableActionButton(String text, String url) {
        super(text, url);
    }

    public AbstractTableActionButton(String text, String url, AnchorTarget anchorTarget) {
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
    public String getClassName() {
        return (method.equals(FormMethod.POST) ? "action " : "") + super.getClassName();
    }

    public AbstractTableActionButton setMethod(FormMethod method) {
        this.method = method;
        return this;
    }
}
