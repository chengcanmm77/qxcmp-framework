package com.qxcmp.spider;

import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.Color;
import lombok.Data;
import org.assertj.core.util.Lists;

import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 蜘蛛元数据定义
 * <p>
 * 定义了创建一个蜘蛛需要的所有信息 由蜘蛛管理器根据此信息动态创建蜘蛛任务
 *
 * @author aaric
 */
@EntityTable(value = "蜘蛛管理", action = QXCMP_ADMIN_URL + "/spider", entityIndex = "name",
        batchActions = @BatchAction(value = "批量禁用", action = "remove", color = Color.RED),
        rowActions = {
                @RowAction(value = "启用", action = "enable", method = FormMethod.POST, color = Color.GREEN),
                @RowAction(value = "禁用", action = "disable", method = FormMethod.POST, color = Color.RED)
        })
@Data
public class SpiderDefinition {

    /**
     * 蜘蛛名称，名称应该在蜘蛛组内唯一
     */
    @TableField("蜘蛛名称")
    private String name;

    /**
     * 蜘蛛组名称，定义蜘蛛执行所属组的名称，不同组的蜘蛛可以并行执行，同一组的蜘蛛串行执行
     */
    @TableField("蜘蛛组")
    private String group;

    /**
     * 蜘蛛抓取起始链接
     */
    private List<String> startUrls = Lists.newArrayList();

    /**
     * 蜘蛛线程数量，设置蜘蛛启用多少个线程来抓取页面
     */
    @TableField("线程数量")
    private Integer thread;

    /**
     * 蜘蛛在蜘蛛组中的执行顺序
     */
    @TableField("执行顺序")
    private Integer order;

    @TableField("是否禁用")
    private boolean disabled;

    /**
     * 蜘蛛页面处理器
     * <p>
     * 页面处理器需要定义为Spring Bean，Scope为Prototype，由Spring从IoC容易中获取并创建新的实例
     */
    private Class<? extends SpiderPageProcessor> pageProcess;

    /**
     * 蜘蛛管理处理器
     * <p>
     * 用于把页面处理器抓取后的实体进行管理处理，定义方式同蜘蛛页面处理器
     */
    private List<Class<? extends SpiderPipeline>> pipelines = Lists.newArrayList();

    @RowActionCheck("启用")
    public boolean canPerformEnable() {
        return this.disabled;
    }

    @RowActionCheck("禁用")
    public boolean canPerformDisable() {
        return !this.disabled;
    }
}
