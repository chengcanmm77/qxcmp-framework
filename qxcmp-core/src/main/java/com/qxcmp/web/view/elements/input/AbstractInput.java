package com.qxcmp.web.view.elements.input;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 输入框
 * <p>
 * 样式可以为
 * <p>
 * 【输入框】
 * <p>
 * 【输入框】【图标】
 * <p>
 * 【标签】【输入框】
 * <p>
 * 【输入框】【按钮】
 * <p>
 * 【输入框】【下拉框】
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractInput extends AbstractComponent {

    /**
     * 提交名称，用于表单提交字段
     */
    private String name;

    /**
     * 默认值
     */
    private String value;

    /**
     * 默认文本
     */
    private String placeholder;

    /**
     * 是否获得焦点
     */
    private boolean focus;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否为错误状态
     */
    private boolean error;

    /**
     * 是否为透明
     */
    private boolean transparent;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 是否占满宽度
     */
    private boolean fluid;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public AbstractInput() {
    }

    public AbstractInput(String placeholder) {
        this.placeholder = placeholder;
    }

    public AbstractInput(String placeholder, String name) {
        this.name = name;
        this.placeholder = placeholder;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/input";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (focus) {
            stringBuilder.append(" focus");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (error) {
            stringBuilder.append(" error");
        }

        if (transparent) {
            stringBuilder.append(" transparent");
        }

        if (inverted) {
            stringBuilder.append(" transparent");
        }

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        stringBuilder.append(size);

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "input";
    }

    public AbstractInput setValue(String value) {
        this.value = value;
        return this;
    }

    public AbstractInput setFocus() {
        setFocus(true);
        return this;
    }

    public AbstractInput setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractInput setError() {
        setError(true);
        return this;
    }

    public AbstractInput setTransparent() {
        setTransparent(true);
        return this;
    }

    public AbstractInput setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractInput setFluid() {
        setFluid(true);
        return this;
    }
}
