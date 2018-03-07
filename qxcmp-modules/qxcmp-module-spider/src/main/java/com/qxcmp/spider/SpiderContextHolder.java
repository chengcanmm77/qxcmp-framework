package com.qxcmp.spider;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Sets;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * 蜘蛛信息全局存放处
 * <p>
 * 存放了以下信息： <ol> <li>蜘蛛元数据定义，可以让蜘蛛执行器动态创建蜘蛛信息</li> <li>蜘蛛运行时信息，定义当前正在运行的蜘蛛，可以动态定制蜘蛛</li> </ol>
 *
 * @author aaric
 */
@Component
public class SpiderContextHolder {

    /**
     * 如果蜘蛛组的名字为空，则使用该默认组名
     */
    private static final String DEFAULT_SPIDER_GROUP = "default";

    /**
     * 蜘蛛元数据配置缓存
     */
    @Getter
    private Multimap<String, SpiderDefinition> spiderDefinitions = ArrayListMultimap.create();

    /**
     * 蜘蛛运行时信息，大小等于定义的蜘蛛组数量
     */
    @Getter
    private Set<SpiderRuntime> spiderRuntimeInfo = Sets.newHashSet();

    /**
     * 注册一个蜘蛛元数据
     *
     * @param spiderPageProcessor 蜘蛛页面处理器，需要定义{@link Spider}注解
     */
    void register(SpiderPageProcessor spiderPageProcessor) {
        Spider spider = spiderPageProcessor.getClass().getAnnotation(Spider.class);
        checkNotNull(spider, "No spider annotation");
        SpiderDefinition spiderDefinition = new SpiderDefinition();

        spiderDefinition.setName(spider.name());

        if (StringUtils.isNotEmpty(spider.group())) {
            spiderDefinition.setGroup(spider.group());
        } else {
            spiderDefinition.setGroup(DEFAULT_SPIDER_GROUP);
        }

        spiderDefinition.setStartUrls(Arrays.asList(spider.startUrls()));
        spiderDefinition.setThread(spider.thread());
        spiderDefinition.setOrder(spider.order());
        spiderDefinition.setPageProcess(spiderPageProcessor.getClass());
        spiderDefinition.setPipelines(Arrays.asList(spider.pipelines()));
        spiderDefinition.setDisabled(spider.disabled());

        register(spiderDefinition);
    }

    private void register(SpiderDefinition spiderDefinition) {
        checkSpiderDefinition(spiderDefinition);
        spiderDefinitions.put(spiderDefinition.getGroup(), spiderDefinition);
    }

    private void checkSpiderDefinition(SpiderDefinition spiderDefinition) {
        checkState(StringUtils.isNotEmpty(spiderDefinition.getName()), "Spider name can't be empty");
        checkState(StringUtils.isNotEmpty(spiderDefinition.getGroup()), "Spider group can't be empty");
        checkState(!spiderDefinition.getStartUrls().isEmpty(), "Spider startUrls can't be empty");
        checkState(spiderDefinition.getThread() > 0, "Spider thread must great than 0");
        checkState(Objects.nonNull(spiderDefinition.getPageProcess()), "Spider PageProcessor can't be empty");
        checkState(!spiderDefinition.getPipelines().isEmpty(), "Spider Pipeline can't be empty");
    }
}
