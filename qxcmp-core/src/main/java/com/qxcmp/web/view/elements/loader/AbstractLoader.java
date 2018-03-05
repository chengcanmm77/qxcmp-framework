package com.qxcmp.web.view.elements.loader;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 加载器组件抽象类
 * <p>
 * 加载器用来告诉用户需要等待活动完成
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractLoader extends AbstractComponent {

    /**
     * 加载器文本 - 可选
     */
    private String text;

    /**
     * 是否为激活状态
     * <p>
     * 如果为非激活状态则加载器为隐藏
     */
    private boolean active;

    /**
     * 是否翻转颜色
     * <p>
     * 默认为黑色
     */
    private boolean inverted;

    /**
     * 是否为不确认状态
     */
    private boolean indeterminate;

    /**
     * 是否为禁用状态
     * <p>
     * 该状态下将隐藏加载器
     */
    private boolean disabled;

    /**
     * 是否为内联
     */
    private boolean inline;

    /**
     * 是否居中
     */
    private boolean centered;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public AbstractLoader() {
    }

    public AbstractLoader(String text) {
        this.text = text;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/loader";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (active) {
            stringBuilder.append(" active");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (indeterminate) {
            stringBuilder.append(" indeterminate");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (inline) {
            stringBuilder.append(" inline");
        }

        if (centered) {
            stringBuilder.append(" centered");
        }

        if (StringUtils.isNotBlank(text)) {
            stringBuilder.append(" text");
        }

        return stringBuilder.append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return "loader";
    }

    public AbstractLoader setActive() {
        setActive(true);
        return this;
    }

    public AbstractLoader setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractLoader setIndeterminate() {
        setIndeterminate(true);
        return this;
    }

    public AbstractLoader setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractLoader setInline() {
        setInline(true);
        return this;
    }

    public AbstractLoader setCentered() {
        setCentered(true);
        return this;
    }

    public AbstractLoader setSize(Size size) {
        this.size = size;
        return this;
    }
}
