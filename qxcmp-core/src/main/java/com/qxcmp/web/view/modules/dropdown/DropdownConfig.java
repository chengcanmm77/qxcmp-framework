package com.qxcmp.web.view.modules.dropdown;

import com.qxcmp.web.view.support.Transition;
import lombok.Builder;
import lombok.Data;

/**
 * 下拉框JS配置
 *
 * @author aaric
 */
@Data
@Builder
public class DropdownConfig {

    /**
     * 多选项最大选项数
     */
    private int maxSelections;

    /**
     * 执行搜索的最小字符
     */
    @Builder.Default
    private int minCharacters = 1;

    /**
     * 搜索的匹配方式
     */
    @Builder.Default
    private MatchEnum match = MatchEnum.both;

    /**
     * 是否使用标签显示
     */
    @Builder.Default
    private boolean useLabels = true;

    /**
     * 是否允许自定义选项
     */
    private boolean allowAdditions;

    /**
     * 是否起开键盘控制
     * <p>
     * 如果关闭该选项，需要使用 Enter 键来确认选项
     */
    @Builder.Default
    private boolean selectOnKeydown = true;

    /**
     * 是否在失去焦点的时候选择当前选项
     */
    @Builder.Default
    private boolean forceSelection = true;

    /**
     * 是否允许选择类型选项
     * <p>
     * 当为多级菜单时，是否可以选择类型项
     */
    private boolean allowCategorySelection;

    /**
     * 是否允许重复选择
     * <p>
     * 当启用该选项时，重复选择当前选项的时候，仍然会触发 onChange 事件
     */
    private boolean allowReselection;

    /**
     * 是否开启模糊匹配
     */
    private boolean fullTextSearch;

    /**
     * 是否允许使用 tab 键来移动
     */
    @Builder.Default
    private boolean allowTab = true;

    /**
     * 下拉框打开动画效果
     *
     * @see Transition
     */
    @Builder.Default
    private String transition = Transition.FADE.toString();

    /**
     * 下拉框的打开方式
     */
    @Builder.Default
    private OnEnum on = OnEnum.click;

    /**
     * 菜单打开方向，默认为根据屏幕自动调整
     */
    @Builder.Default
    private DirectionEnum direction = DirectionEnum.auto;

    public enum OnEnum {
        hover, click
    }

    public enum MatchEnum {
        both, value, text
    }

    public enum DirectionEnum {
        auto, upward, downward
    }
}
