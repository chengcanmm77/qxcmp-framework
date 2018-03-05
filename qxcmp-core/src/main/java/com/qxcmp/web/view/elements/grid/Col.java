package com.qxcmp.web.view.elements.grid;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.support.Floated;
import com.qxcmp.web.view.support.Wide;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 网格列组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class Col extends AbstractGridItem {

    /**
     * 默认宽度
     */
    private Wide generalWide = Wide.NONE;

    /**
     * 在电脑端的宽度
     */
    private Wide computerWide = Wide.NONE;

    /**
     * 在平板端的宽度
     */
    private Wide tabletWide = Wide.NONE;

    /**
     * 在移动端的宽度
     */
    private Wide mobileWide = Wide.NONE;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    /**
     * 容器内容
     */
    private List<Component> components = Lists.newArrayList();

    public Col() {
        super();
    }

    public Col(Wide generalWide) {
        this();
        this.generalWide = generalWide;
    }

    public Col addComponent(Component component) {
        components.add(component);
        return this;
    }

    public Col addComponents(Collection<? extends Component> components) {
        this.components.addAll(components);
        return this;
    }

    @Override
    public String getFragmentName() {
        return "col";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (StringUtils.isNotBlank(computerWide.toString())) {
            stringBuilder.append(computerWide).append(" computer");
        }

        if (StringUtils.isNotBlank(tabletWide.toString())) {
            stringBuilder.append(tabletWide).append(" tablet");
        }

        if (StringUtils.isNotBlank(mobileWide.toString())) {
            stringBuilder.append(mobileWide).append(" mobile");
        }

        stringBuilder.append(generalWide.toString()).append(floated.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "column";
    }

    public Col setGeneralWide(Wide wide) {
        this.generalWide = wide;
        return this;
    }

    public Col setComputerWide(Wide wide) {
        this.computerWide = wide;
        return this;
    }

    public Col setTabletWide(Wide wide) {
        this.tabletWide = wide;
        return this;
    }

    public Col setMobileWide(Wide wide) {
        this.mobileWide = wide;
        return this;
    }

    public Col setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }
}
