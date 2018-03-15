package com.qxcmp.core.extension;

/**
 * 拓展
 * <p>
 * 拓展可以插入到拓展点
 *
 * @author Aaric
 */
public interface Extension {

    /**
     * 该插件是否可用
     *
     * @return 是否可用
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * 排序
     * <p>
     * 越小越优先
     *
     * @return 排序参数
     */
    default int order() {
        return Integer.MAX_VALUE;
    }
}
