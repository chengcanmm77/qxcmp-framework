package com.qxcmp.spdier;

import lombok.Getter;
import org.assertj.core.util.Lists;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Optional;

/**
 * 蜘蛛页面处理器
 * <p>
 * 为平台蜘蛛提供统一的抽象类，使用模板方法进行页面抓取
 * <p>
 * 页面处理器负责解析页面，页面分为内容页面和目标页面
 * <p>
 * 内容页面包含更多的目标页面
 * <p>
 * 目标页面包含解析实体所需要的信息
 * <p>
 * 页面处理器负责把目标页面转换为领域实体对象，然后有框架交给页面处理器对应的管理进行下一步处理{@link SpiderPipeline}
 *
 * @param <T> 最后给管道处理的实体类型
 *
 * @author aaric
 * @see SpiderPipeline
 */
public abstract class SpiderPageProcessor<T> implements PageProcessor {

    /**
     * 交给管理领域实体对象键
     */
    static final String PIPELINE_RESULT = "entity";

    /**
     * 蜘蛛启动时间
     */
    @Getter
    private long startTime = System.currentTimeMillis();

    /**
     * 蜘蛛抓取页面总数
     */
    @Getter
    private long totalPageCount;

    /**
     * 蜘蛛抓取内容页面数量
     */
    @Getter
    private long contentPageCount;

    /**
     * 蜘蛛抓取目标页面数量
     */
    @Getter
    private long targetPageCount;

    /**
     * 蜘蛛管道链，负责处理目标页面解析后的领域对象实体
     */
    @Getter
    private List<SpiderPipeline> spiderPipelines = Lists.newArrayList();

    /**
     * 蜘蛛站点配置
     */
    @Getter
    private Site site = Site.me();

    /**
     * 页面处理器逻辑，使用模板方法处理页面，不允许子类修改该方法
     *
     * @param page 抓取到的页面
     */
    @Override
    public final void process(Page page) {
        ++totalPageCount;

        page.setSkip(true);

        if (isTargetPage(page)) {
            ++targetPageCount;

            processTargetPage(page).ifPresent(t -> {
                page.putField(PIPELINE_RESULT, t);
                page.setSkip(false);
            });
        } else {
            ++contentPageCount;
            processContentPage(page);
        }
    }

    /**
     * 判断是否为目标页面，如果该页面包含了要抓取实体的信息则为目标页面
     *
     * @param page 当前抓取页面
     *
     * @return 是否为目标页面
     */
    protected abstract boolean isTargetPage(Page page);

    /**
     * 处理目标页面，将页面要抓取的信息整合成为实体对象并存放在结果中，交由管道处理
     *
     * @param page 当前抓取页面
     *
     * @return 处理后的实体对象
     */
    protected abstract Optional<T> processTargetPage(Page page);

    /**
     * 处理内容页面，当页面不为目标页面的时候，使用该方法，一般将需要抓取的页面链接添加到待抓取页面中
     *
     * @param page 当前抓取页面
     */
    protected abstract void processContentPage(Page page);
}
