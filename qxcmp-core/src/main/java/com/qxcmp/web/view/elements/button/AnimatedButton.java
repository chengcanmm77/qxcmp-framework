package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 动效按钮
 * <p>
 * 该按钮可以设置可见部分和隐藏部分
 * <p>
 * 当鼠标悬浮在按钮上是，显示隐藏部分
 * <p>
 * 按钮的宽度根据可见部分决定
 *
 * @author Aaric
 */
@Getter
@Setter
public class AnimatedButton extends AbstractButton {

    /**
     * 动效类型
     */
    private AnimatedType animatedType = AnimatedType.DEFAULT;

    /**
     * 可见文本
     */
    private String visibleText;

    /**
     * 隐藏本文
     */
    private String hiddenText;

    /**
     * 可见图标
     * <p>
     * 该属性设置以后会在可见文本前面追加图标
     */
    private Icon visibleIcon;

    /**
     * 隐藏图标
     * <p>
     * 该属性设置以后会在隐藏文本前面追加图标
     */
    private Icon hiddenIcon;

    public AnimatedButton() {
    }

    public AnimatedButton(String url) {
        super(url);
    }

    public AnimatedButton(String url, AnchorTarget anchorTarget) {
        super(url, anchorTarget);
    }

    @Override
    public String getFragmentName() {
        return "animated";
    }

    @Override
    public String getClassName() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassName() + " animated");

        if (StringUtils.isNotBlank(animatedType.getClassName())) {
            stringBuilder.append(" ").append(animatedType.getClassName());
        }

        return stringBuilder.toString();
    }

    public AnimatedButton setAnimatedType(AnimatedType animatedType) {
        this.animatedType = animatedType;
        return this;
    }

    public AnimatedButton setVisibleText(String visibleText) {
        this.visibleText = visibleText;
        return this;
    }

    public AnimatedButton setVisibleIcon(Icon visibleIcon) {
        this.visibleIcon = visibleIcon;
        return this;
    }

    public AnimatedButton setHiddenText(String hiddenText) {
        this.hiddenText = hiddenText;
        return this;
    }

    public AnimatedButton setHiddenIcon(Icon hiddenIcon) {
        this.hiddenIcon = hiddenIcon;
        return this;
    }

    public enum AnimatedType {
        DEFAULT(""),
        VERTICAL("vertical"),
        FADE("fade");

        private String className;

        AnimatedType(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }
}
