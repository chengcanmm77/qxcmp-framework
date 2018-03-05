package com.qxcmp.spdier;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 蜘蛛日志实体
 * <p>
 * 记录了一次蜘蛛抓取的相信信息
 *
 * @author aaric
 */
@EntityTable("蜘蛛日志")
@Entity
@Table
@Data
public class SpiderLog {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 蜘蛛所属组
     */
    @TableField("蜘蛛组")
    private String spiderGroup;

    /**
     * 蜘蛛名称
     */
    @TableField("蜘蛛名称")
    private String name;

    /**
     * 抓取内容页面
     */
    @TableField("内容页面数量")
    private Long contentPageCount;

    /**
     * 抓取的目标页面
     */
    @TableField("目标页面数量")
    private Long targetPageCount;

    /**
     * 新添实体数量
     */
    @TableField("新添数量")
    private Long newPageCount;

    /**
     * 更新实体数量
     */
    @TableField("更新数量")
    private Long updatePageCount;

    /**
     * 丢弃实体数量
     */
    @TableField("丢弃数量")
    private Long dropPageCount;

    /**
     * 蜘蛛耗时
     */
    @TableField("抓取时长")
    private String duration;

    /**
     * 抓取开始时间
     */
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm")
    @TableField("开始时间")
    private Date dateStart;

    /**
     * 抓取结束时间
     */
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm")
    @TableField("结束时间")
    private Date dateFinish = new Date();
}
