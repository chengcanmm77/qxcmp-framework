package com.qxcmp.web.view.modules.sidebar;

import lombok.Builder;
import lombok.Data;

/**
 * 侧边栏 JS 配置
 *
 * @author Aaric
 */
@Data
@Builder
public class SidebarConfig {

    /**
     * 打开的位置
     */
    @Builder.Default
    private String content = "body";

    /**
     * 是否在多侧边栏的时候同时打开本侧边栏
     */
    private boolean exclusive;

    /**
     * 是否在点击页面的时候关闭
     */
    @Builder.Default
    private boolean closable = true;

    /**
     * 是否在打开的时候使页面变暗
     */
    @Builder.Default
    private boolean dimPage = true;
}
