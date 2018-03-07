package com.qxcmp.spider;

import com.qxcmp.core.init.QxcmpInitializer;
import com.qxcmp.spider.event.AdminSpiderFinishEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 蜘蛛执行器
 * <p>
 * 负责在平台启动的时候加载所有蜘蛛配置，然后根据加载的蜘蛛配置启动蜘蛛组，并且在蜘蛛结束时记录到蜘蛛日志中
 *
 * @author aaric
 */
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Slf4j
@Component
@RequiredArgsConstructor
public class SpiderRunner implements QxcmpInitializer {

    private final ApplicationContext applicationContext;
    private final TaskExecutor taskExecutor;
    private final SpiderContextHolder spiderContextHolder;
    private final SpiderLogService spiderLogService;


    /**
     * 启动蜘蛛运行器
     * <p>
     * 从{@link SpiderContextHolder}中加载注册的蜘蛛配置信息根据分组数量对每个组启动一个任务来执行该组中所有的蜘蛛
     */
    void start() {
        spiderContextHolder.getSpiderDefinitions().keySet().forEach(group -> taskExecutor.execute(() -> startGroup(group)));
    }

    /**
     * 循环执行一个蜘蛛组任务
     * <p>
     * 会从{@link SpiderContextHolder} 中注册的蜘蛛元数据动态状态一个蜘蛛组队列，依次执行该蜘蛛组中的每一个蜘蛛
     * <p>
     * 当蜘蛛组中所有任务执行完成以后，重新执行该组的所有蜘蛛，如果在这期间蜘蛛的顺序发生变化，需要等到下一轮任务启动
     *
     * @param group 要启动的蜘蛛组的名称
     */
    private void startGroup(String group) {

        /*
         * 执行一个组的任务
         * */
        FutureTask<String> groupTask = new FutureTask<>(() -> {

            List<SpiderDefinition> spiderDefinitionList = spiderContextHolder.getSpiderDefinitions().get(group).stream().filter(spiderDefinition -> !spiderDefinition.isDisabled()).sorted(Comparator.comparing(SpiderDefinition::getOrder)).collect(Collectors.toList());

            if (spiderDefinitionList.isEmpty()) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (Exception ignored) {

                }
            } else {
                spiderDefinitionList.forEach(this::startSpider);
            }

            return group;
        });

        taskExecutor.execute(groupTask);

        try {
            /*
             * 当一个组的任务执行完以后，重新执行该组的任务
             * */
            String groupName = groupTask.get();
            startGroup(groupName);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据蜘蛛元数据启动同步一个蜘蛛任务
     *
     * @param spiderDefinition 蜘蛛元数据
     */
    @SuppressWarnings("unchecked")
    private void startSpider(SpiderDefinition spiderDefinition) {

        if (spiderDefinition.isDisabled()) {
            return;
        }

        try {
            SpiderPageProcessor spiderPageProcessor = applicationContext.getBean(spiderDefinition.getPageProcess());

            us.codecraft.webmagic.Spider spider = us.codecraft.webmagic.Spider.create(spiderPageProcessor).thread(spiderDefinition.getThread());

            spiderDefinition.getStartUrls().forEach(spider::addUrl);

            spiderDefinition.getPipelines().forEach(aClass -> {
                SpiderPipeline spiderPipeline = applicationContext.getBean(aClass);
                spider.addPipeline(spiderPipeline);
                spiderPageProcessor.getSpiderPipelines().add(spiderPipeline);
            });

            log.info("Start spider: [{}]:[{}]", spiderDefinition.getGroup(), spiderDefinition.getName());

            SpiderRuntime spiderRuntime = new SpiderRuntime(spiderDefinition.getGroup(), spiderDefinition.getName(), spider, spiderPageProcessor);
            spiderContextHolder.getSpiderRuntimeInfo().add(spiderRuntime);
            spider.run();
            spiderContextHolder.getSpiderRuntimeInfo().remove(spiderRuntime);
            spiderLogService.save(spiderPageProcessor);
            applicationContext.publishEvent(new AdminSpiderFinishEvent(spiderDefinition, spiderPageProcessor));
        } catch (NoSuchBeanDefinitionException e) {
            log.error("Start spider failed, cause: {}", e.getMessage());
        }
    }

    private void loadSpiderDefinition() {
        applicationContext.getBeansOfType(SpiderPageProcessor.class).forEach((s, spiderPageProcessor) -> {
            try {
                spiderContextHolder.register(spiderPageProcessor);
            } catch (Exception e) {
                log.error("Can't register spider definition for {}, cause: {}", spiderPageProcessor.getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    public void init() {
        loadSpiderDefinition();
        start();
    }
}
