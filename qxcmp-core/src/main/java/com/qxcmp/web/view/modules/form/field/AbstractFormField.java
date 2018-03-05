package com.qxcmp.web.view.modules.form.field;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

/**
 * 表单字段抽象类
 * <p>
 * 字段默认为水平显示，标签在左边，字段组件在右边
 * <p>
 * 字段在移动端将自动转换为垂直显示，标签在上面，字段组件在下面
 * <p>
 * 字段需要支持一些基本功能
 * <p>
 * 1. 验证错误信息显示 2. 字段输入限制 3. 提示文本的显示
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractFormField extends AbstractComponent {

    /**
     * ID
     * <p>
     * 用于 JS 初始化
     */
    private String id = "form-field-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段标签
     */
    private String label;

    /**
     * 字段说明文本
     */
    private String tooltip;

    /**
     * 是否为必选项
     * <p>
     * 会在标签上加上红星
     */
    private boolean required;

    /**
     * 错误消息，当为错误状态时自动显示
     */
    private String errorMessage;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/form";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (required) {
            stringBuilder.append(" required");
        }

        if (Objects.nonNull(errorMessage)) {
            stringBuilder.append(" error");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "qxcmp field";
    }

    public AbstractFormField setName(String name) {
        this.name = name;
        return this;
    }

    public AbstractFormField setLabel(String label) {
        this.label = label;
        return this;
    }

    public AbstractFormField setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public AbstractFormField setRequired() {
        setRequired(true);
        return this;
    }

    public AbstractFormField setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public AbstractFormField setDisabled() {
        setDisabled(true);
        return this;
    }
}
