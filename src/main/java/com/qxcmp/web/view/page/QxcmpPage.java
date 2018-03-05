package com.qxcmp.web.view.page;

import com.qxcmp.web.view.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 平台所有页面接口
 * <p>
 * 页面负责服务器端渲染
 * <p>
 * Controller负责组装页面的Model
 * <p>
 * 所有的子类必须为Spring Bean且Scope为PROTOTYPE
 * <p>
 * 使用 {@link QxcmpPageFactory#next(Class, Object...)} 来生成一个页面
 *
 * @author Aaric
 */
public interface QxcmpPage {

    /**
     * 设置页面标题
     *
     * @param title 标题
     *
     * @return 页面本身
     */
    QxcmpPage setTitle(String title);

    /**
     * 添加一个视图组件
     *
     * @param component 要添加的视图
     *
     * @return 页面本身
     */
    QxcmpPage addComponent(Component component);

    /**
     * 添加一个视图组件
     *
     * @param supplier 要添加的视图
     *
     * @return 页面本身
     */
    QxcmpPage addComponent(Supplier<Component> supplier);

    /**
     * 添加一组视图组件
     *
     * @param components 要添加的组件
     *
     * @return 页面本身
     */
    QxcmpPage addComponents(Collection<Component> components);

    /**
     * 添加一个CSS样式表
     *
     * @param stylesheet 样式表路径 如 {@code "/assets/styles/qxcmp.css"}
     *
     * @return 页面本身
     */
    QxcmpPage addStylesheet(String stylesheet);

    /**
     * 添加一个JS脚本到 {@code <head></head>}
     *
     * @param javascript 脚本路径 如 {@code "/assets/scripts/qxcmp.js"}
     *
     * @return 页面本身
     */
    QxcmpPage addJavascript(String javascript);

    /**
     * 添加一个JS脚本到{@code <body></body>}
     *
     * @param javascript 脚本路径 如 {@code "/assets/scripts/qxcmp.js"}
     *
     * @return 页面本身
     */
    QxcmpPage addJavascriptToBody(String javascript);

    /**
     * 渲染页面
     * <p>
     * 负责把数据组装为视图
     */
    void render();

    /**
     * 把渲染完的页面构建为 {@link ModelAndView}
     *
     * @return 最终的视图
     */
    ModelAndView build();
}
