package com.qxcmp.core.event;

import com.qxcmp.spdier.SpiderDefinition;
import com.qxcmp.spdier.SpiderPageProcessor;
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
