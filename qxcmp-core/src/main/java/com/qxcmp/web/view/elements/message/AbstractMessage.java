package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.support.Attached;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

/**
 * 消息组件抽象类
 * <p>
 * 消息组件用来展示信息以解释周围的内容
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractMessage extends AbstractComponent {

    /**
     * 消息ID
     * <p>
     * 用于JS初始化消息
     */
    private String id = "message-" + RandomStringUtils.randomAlphabetic(10);

    /**
     * 标题
     */
    private String title;

    /**
     * 一般文本
     */
    private String content;

    /**
     * 列表视图
     */
    private List list;

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 是否可关闭
     */
    private boolean closeable;

    /**
     * 是否隐藏
     */
    private boolean hidden;

    /**
     * 是否总是可见
     */
    private boolean visible;

    /**
     * 是否悬浮
     */
    private boolean floating;

    /**
     * 是否为紧凑样式
     * <p>
     * 宽度为实际占用宽度
     */
    private boolean compact;

    /**
     * 附着方式
     */
    private Attached attached = Attached.NONE;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public AbstractMessage(String content) {
        this.content = content;
    }

    public AbstractMessage(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public AbstractMessage(String title, String content, List list) {
        this.title = title;
        this.content = content;
        this.list = list;
    }

    public AbstractMessage(String title, List list) {
        this.title = title;
        this.list = list;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/message";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (hidden) {
            stringBuilder.append(" hidden");
        }

        if (visible) {
            stringBuilder.append(" visible");
        }

        if (floating) {
            stringBuilder.append(" floating");
        }

        if (compact) {
            stringBuilder.append(" compact");
        }

        return stringBuilder.append(attached).append(color).append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return Objects.isNull(icon) ? "message" : "icon message";
    }

    public AbstractMessage setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbstractMessage setIcon(String iconName) {
        this.icon = new Icon(iconName);
        return this;
    }

    public AbstractMessage setCloseable() {
        setCloseable(true);
        return this;
    }

    public AbstractMessage setHidden() {
        setHidden(true);
        return this;
    }

    public AbstractMessage setVisible() {
        setVisible(true);
        return this;
    }

    public AbstractMessage setFloating() {
        setFloating(true);
        return this;
    }

    public AbstractMessage setCompact() {
        setCompact(true);
        return this;
    }

    public AbstractMessage setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }

    public AbstractMessage setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractMessage setSize(Size size) {
        this.size = size;
        return this;
    }
}
