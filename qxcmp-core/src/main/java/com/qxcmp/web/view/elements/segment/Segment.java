package com.qxcmp.web.view.elements.segment;

import com.qxcmp.web.view.support.Attached;
import lombok.Getter;
import lombok.Setter;

/**
 * 片段一般用来创建一组相关的内容
 * <p>
 * 一个片段包含一定的内容
 *
 * @author aaric
 */
@Getter
@Setter
public class Segment extends AbstractSegment {

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 附着方式
     * <p>
     * 附着方式可与其他的同样拥有附着属性的组件结合使用
     */
    private Attached attached = Attached.NONE;

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        stringBuilder.append(attached.toString());

        return stringBuilder.toString();
    }

    public Segment setInverted() {
        this.inverted = true;
        return this;
    }

    public Segment setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }
}
