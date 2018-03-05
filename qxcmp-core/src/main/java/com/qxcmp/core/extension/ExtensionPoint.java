package com.qxcmp.core.extension;

import java.util.List;

/**
 * 拓展点
 * <p>
 * 拓展点可以接入
 *
 * @author Aaric
 */
public interface ExtensionPoint<T extends Extension> {

    /**
     * 获取该扩展点扩展的类型信息
     *
     * @return 扩展类型信息
     */
    Class<T> getExtension();

    /**
     * 获取该扩展点当前载入的扩展
     *
     * @return 该扩展点当前的扩展
     */
    List<T> getExtensions();

    /**
     * 向该扩展点加入一个扩展
     *
     * @param extension 要加入的扩展
     *
     * @return 加入扩展后的扩展点
     */
    ExtensionPoint<T> addExtension(T extension);
}
