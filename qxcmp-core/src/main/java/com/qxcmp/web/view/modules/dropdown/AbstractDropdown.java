package com.qxcmp.web.view.modules.dropdown;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
public abstract class AbstractDropdown extends AbstractComponent {

    /**
     * 下拉框ID
     * <p>
     * 用于 JS 初始化
     */
    private String id = "dropdown-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * JS 配置
     */
    private DropdownConfig config;

    /**
     * 下拉框文本
     */
    private String text;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 是否为错误状态
     */
    private boolean error;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否显示滚动条
     */
    private boolean scrolling;

    /**
     * 是否移除最小宽度
     * <p>
     * 将根据内容的宽度自动调整
     */
    private boolean compact;

    /**
     * 是否占满父容器
     */
    private boolean fluid;

    public AbstractDropdown() {
    }

    public AbstractDropdown(String text) {
        this.text = text;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/dropdown";
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

        if (error) {
            stringBuilder.append(" error");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (scrolling) {
            stringBuilder.append(" scrolling");
        }

        if (compact) {
            stringBuilder.append(" compact");
        }

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "dropdown";
    }

    public AbstractDropdown setConfig(DropdownConfig config) {
        this.config = config;
        return this;
    }

    public AbstractDropdown setLoading() {
        this.loading = true;
        return this;
    }

    public AbstractDropdown setError() {
        setError(true);
        return this;
    }

    public AbstractDropdown setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractDropdown setScrolling() {
        setScrolling(true);
        return this;
    }

    public AbstractDropdown setCompact() {
        setCompact(true);
        return this;
    }

    public AbstractDropdown setFluid() {
        setFluid(true);
        return this;
    }
}
