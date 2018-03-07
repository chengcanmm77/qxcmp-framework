package com.qxcmp.spider;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.Color;
import lombok.Data;
import us.codecraft.webmagic.Spider;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 蜘蛛运行时信息
 * <p>
 * 存放了一个正在运行的蜘蛛信息
 *
 * @author aaric
 */
@EntityTable(value = "蜘蛛运行状态", action = QXCMP_BACKEND_URL + "/spider/status", entityIndex = "name",
        rowActions = @RowAction(value = "终止", action = "stop", method = FormMethod.POST, color = Color.RED))
@Data
public class SpiderRuntime {

    @TableField("蜘蛛组")
    private String group;

    @TableField("蜘蛛名称")
    private String name;

    private Spider spider;

    private SpiderPageProcessor spiderPageProcessor;

    public SpiderRuntime(String group, String name, Spider spider, SpiderPageProcessor spiderPageProcessor) {
        this.group = group;
        this.name = name;
        this.spider = spider;
        this.spiderPageProcessor = spiderPageProcessor;
    }
}
