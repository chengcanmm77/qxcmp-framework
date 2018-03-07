package com.qxcmp.core.module;

import com.qxcmp.config.SystemConfigLoader;
import com.qxcmp.core.navigation.NavigationLoader;

/**
 * 抽象模块加载器
 * <p>
 * 负责一个模块的初始化和各种配置
 *
 * @author Aaric
 */
public interface ModuleLoader extends SystemConfigLoader, NavigationLoader {
}
