package com.qxcmp.core.module;

import com.qxcmp.config.SystemConfigLoader;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.security.SecurityLoader;

/**
 * 抽象模块加载器
 * <p>
 * 负责一个模块的初始化和各种配置
 * <p>
 * 模块可以定义：
 * <ol>
 * <li>模块全局变量</li>
 * <li>系统配置</li>
 * <li>权限配置</li>
 * <li>模块导航配置</li>
 * </ol>
 *
 * @author Aaric
 */
public interface ModuleLoader extends SystemConfigLoader, SecurityLoader, NavigationLoader {
}
