package com.qxcmp.spider.event;

import com.qxcmp.spider.SpiderDefinition;
import com.qxcmp.spider.SpiderPageProcessor;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSpiderFinishEvent {

    private final SpiderDefinition spiderDefinition;
    private final SpiderPageProcessor spiderPageProcessor;

    public AdminSpiderFinishEvent(SpiderDefinition spiderDefinition, SpiderPageProcessor spiderPageProcessor) {
        this.spiderDefinition = spiderDefinition;
        this.spiderPageProcessor = spiderPageProcessor;
    }
}
