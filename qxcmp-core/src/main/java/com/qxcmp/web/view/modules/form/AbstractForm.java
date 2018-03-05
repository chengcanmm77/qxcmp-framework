package com.qxcmp.web.view.modules.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.button.AnimatedButton;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.header.AbstractHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.elements.message.InfoMessage;
import com.qxcmp.web.view.elements.message.SuccessMessage;
import com.qxcmp.web.view.elements.message.WarningMessage;
import com.qxcmp.web.view.modules.form.field.AbstractFormField;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.ButtonType;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 表单抽象类
 * <p>
 * 所有表单都需要关联一个表单对象，即使用表单对象来存储表单提交数据
 *
 * @author Aaric
 */
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
     * 表单对象名称
     * <p>
     * 表单对象需要另外手动添加到 ModelAndView 中
     * <p>
     * 表单必须包裹一个表单对象用于存储表单提交数据
     */
    private String object = "object";

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
    private FormMethod method = FormMethod.GET;

    /**
     * 编码方式
     */
    private FormEnctype enctype = FormEnctype.NONE;

    /**
     * 是否禁用自动完成
     */
    private boolean disableAutoComplete;

    /**
     * 提交打开方式
     *
     * @see AnchorTarget
     */
    private String target;

    /**
     * 提交按钮
     */
    private AbstractButton submitButton = new Button("提交").setSecondary();

    /**
     * 表单消息 - 可选
     */
    private InfoMessage infoMessage;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 表单头部
     */
    private AbstractHeader header;

    /**
     * 当为成功状态时，该成功消息会自动显示
     */
    private SuccessMessage successMessage;

    /**
     * 当为警告状态时，该消息会自动显示
     */
    private WarningMessage warningMessage;

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

    public AbstractForm addItem(AbstractFormField field, String sectionName) {
        AbstractFormSection section = null;

        for (AbstractFormSection abstractFormSection : sections) {
            if (StringUtils.equals(abstractFormSection.getName(), sectionName)) {
                section = abstractFormSection;
                break;
            }
        }

        if (Objects.isNull(section)) {
            section = new FormSection(sectionName);
            sections.add(section);
        }

        section.addField(field);

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

        if (Objects.nonNull(successMessage)) {
            stringBuilder.append(" success");
        }

        if (Objects.nonNull(warningMessage)) {
            stringBuilder.append(" warning");
        }

        if (Objects.nonNull(errorMessage)) {
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

    public AbstractForm setObject(String object) {
        this.object = object;
        return this;
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

    public AbstractForm setHeader(AbstractHeader header) {
        this.header = header;
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

    public AbstractForm setErrorMessage(ErrorMessage errorMessage) {
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

    public AbstractForm setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractForm setSize(Size size) {
        this.size = size;
        return this;
    }

    public AbstractForm setSubmitButton(String submitText) {
        return setSubmitButton(submitText, "send");
    }

    public AbstractForm setSubmitButton(String submitText, String submitIcon) {
        this.submitButton = new AnimatedButton().setVisibleText(submitText).setHiddenIcon(new Icon(submitIcon)).setAnimatedType(AnimatedButton.AnimatedType.FADE).setSecondary().setType(ButtonType.SUBMIT);
        return this;
    }

    public AbstractForm setSubmitButton(AbstractButton button) {
        this.submitButton = button;
        this.submitButton.setType(ButtonType.SUBMIT);
        return this;
    }
}
