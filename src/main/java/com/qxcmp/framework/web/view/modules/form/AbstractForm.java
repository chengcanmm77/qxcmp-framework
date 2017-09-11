package com.qxcmp.framework.web.view.modules.form;

import com.google.common.collect.Lists;
import com.qxcmp.framework.web.view.AbstractComponent;
import com.qxcmp.framework.web.view.elements.message.ErrorMessage;
import com.qxcmp.framework.web.view.elements.message.InfoMessage;
import com.qxcmp.framework.web.view.elements.message.SuccessMessage;
import com.qxcmp.framework.web.view.elements.message.WarningMessage;
import com.qxcmp.framework.web.view.modules.form.field.AbstractFormField;
import com.qxcmp.framework.web.view.support.AnchorTarget;
import com.qxcmp.framework.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class AbstractForm extends AbstractComponent {

    /**
     * ID
     * <p>
     * 用于 JS 初始化
     */
    private String id = "form-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 名称
     */
    private String name;

    /**
     * 提交链接
     */
    private String action;

    /**
     * 提交方式
     */
    private FormMethod method;

    /**
     * 编码方式
     */
    private FormEnctype enctype;

    /**
     * 是否禁用自动完成
     */
    private boolean disableAutoComplete;

    /**
     * 提交打开方式
     *
     * @see com.qxcmp.framework.web.view.support.AnchorTarget
     */
    private String target;

    /**
     * 表单消息 - 可选
     */
    private InfoMessage infoMessage;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 是否为成功状态
     */
    private boolean success;

    /**
     * 当为成功状态时，该成功消息会自动显示
     */
    private SuccessMessage successMessage;

    /**
     * 是否为警告状态
     */
    private boolean warning;

    /**
     * 当为警告状态时，该消息会自动显示
     */
    private WarningMessage warningMessage;

    /**
     * 是否为失败状态
     */
    private boolean error;

    /**
     * 当为失败状态时，该失败消息会自动显示
     */
    private ErrorMessage errorMessage;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    /**
     * 字段是否等宽
     */
    private boolean equalWidth;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    private List<AbstractFormSection> sections = Lists.newArrayList();

    public AbstractForm addSection(AbstractFormSection section) {
        sections.add(section);
        return this;
    }

    public AbstractForm addSections(Collection<? extends AbstractFormSection> sections) {
        this.sections.addAll(sections);
        return this;
    }

    public AbstractForm addItem(AbstractFormField field) {
        AbstractFormSection formSection;
        if (sections.isEmpty()) {
            formSection = new FormSection();
            sections.add(formSection);
        } else {
            formSection = sections.get(0);
        }
        formSection.addField(field);
        return this;
    }

    public AbstractForm addItems(Collection<? extends AbstractFormField> fields) {
        AbstractFormSection formSection;
        if (sections.isEmpty()) {
            formSection = new FormSection();
            sections.add(formSection);
        } else {
            formSection = sections.get(0);
        }
        formSection.addFields(fields);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/form";
    }

    @Override
    public String getFragmentName() {
        return "form";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (equalWidth) {
            stringBuilder.append(" equal width");
        }

        if (success) {
            stringBuilder.append(" success");
        }

        if (warning) {
            stringBuilder.append(" warning");
        }

        if (error) {
            stringBuilder.append(" error");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        return stringBuilder.append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return "form";
    }

    public AbstractForm setName(String name) {
        this.name = name;
        return this;
    }

    public AbstractForm setAction(String action) {
        this.action = action;
        return this;
    }

    public AbstractForm setMethod(FormMethod method) {
        this.method = method;
        return this;
    }

    public AbstractForm setEnctype(FormEnctype enctype) {
        this.enctype = enctype;
        return this;
    }

    public AbstractForm setDisabledAutoComplete() {
        setDisableAutoComplete(true);
        return this;
    }

    public AbstractForm setTarget(AnchorTarget target) {
        this.target = target.toString();
        return this;
    }

    public AbstractForm setInfoMessage(InfoMessage infoMessage) {
        this.infoMessage = infoMessage;
        return this;
    }

    public AbstractForm setSuccessMessage(SuccessMessage successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public AbstractForm setWarningMessage(WarningMessage warningMessage) {
        this.warningMessage = warningMessage;
        return this;
    }

    public AbstractForm seErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public AbstractForm setLoading() {
        setLoading(true);
        return this;
    }

    public AbstractForm setEqualWidth() {
        setEqualWidth(true);
        return this;
    }

    public AbstractForm setSuccess() {
        setSuccess(true);
        return this;
    }

    public AbstractForm setWarning() {
        setWarning(true);
        return this;
    }

    public AbstractForm setError() {
        setError(true);
        return this;
    }

    public AbstractForm setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractForm setSize(Size size) {
        this.size = size;
        return this;
    }
}
