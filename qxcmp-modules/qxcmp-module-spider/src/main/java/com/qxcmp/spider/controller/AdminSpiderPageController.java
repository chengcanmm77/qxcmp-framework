package com.qxcmp.spider.controller;

import com.google.common.collect.Lists;
import com.qxcmp.audit.ActionException;
import com.qxcmp.spider.SpiderContextHolder;
import com.qxcmp.spider.SpiderDefinition;
import com.qxcmp.spider.SpiderLogService;
import com.qxcmp.spider.SpiderRuntime;
import com.qxcmp.spider.event.AdminSpiderDisableEvent;
import com.qxcmp.spider.event.AdminSpiderEnableEvent;
import com.qxcmp.spider.event.AdminSpiderStopEvent;
import com.qxcmp.spider.page.AdminSpiderLogPage;
import com.qxcmp.spider.page.AdminSpiderStatusPage;
import com.qxcmp.spider.page.AdminSpiderTablePage;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

import static com.qxcmp.spider.SpiderModule.ADMIN_SPIDER_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_SPIDER_URL)
@RequiredArgsConstructor
public class AdminSpiderPageController extends QxcmpController {

    private final SpiderContextHolder spiderContextHolder;
    private final SpiderLogService spiderLogService;

    @GetMapping("")
    public ModelAndView spiderPage() {
        Page<SpiderDefinition> spiderDefinitions = new PageImpl<>(Lists.newArrayList(spiderContextHolder.getSpiderDefinitions().values()), PageRequest.of(0, spiderContextHolder.getSpiderDefinitions().size()), spiderContextHolder.getSpiderDefinitions().size());
        return page(AdminSpiderTablePage.class, spiderDefinitions);
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> roleBatchRemove(@RequestParam("keys[]") List<String> keys) {
        return execute("批量禁用蜘蛛", context -> {
            try {
                for (String key : keys) {
                    spiderContextHolder.getSpiderDefinitions().values().stream().filter(definition -> definition.getName().equals(key)).findAny()
                            .ifPresent(spiderDefinition -> {
                                spiderDefinition.setDisabled(true);
                                applicationContext.publishEvent(new AdminSpiderDisableEvent(currentUser().orElseThrow(RuntimeException::new), spiderDefinition));
                            });
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @PostMapping("/{name}/enable")
    public ResponseEntity<RestfulResponse> spiderEnable(@PathVariable String name) {
        return execute("启用蜘蛛", context -> {
            try {
                spiderContextHolder.getSpiderDefinitions().values().stream().filter(definition -> definition.getName().equals(name)).findAny()
                        .ifPresent(spiderDefinition -> {
                            spiderDefinition.setDisabled(false);
                            applicationContext.publishEvent(new AdminSpiderEnableEvent(currentUser().orElseThrow(RuntimeException::new), spiderDefinition));
                        });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @PostMapping("/{name}/disable")
    public ResponseEntity<RestfulResponse> spiderDisable(@PathVariable String name) {
        return execute("禁用蜘蛛", context -> {
            try {
                spiderContextHolder.getSpiderDefinitions().values().stream().filter(definition -> definition.getName().equals(name)).findAny()
                        .ifPresent(spiderDefinition -> {
                            spiderDefinition.setDisabled(true);
                            applicationContext.publishEvent(new AdminSpiderDisableEvent(currentUser().orElseThrow(RuntimeException::new), spiderDefinition));
                        });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/status")
    public ModelAndView spiderStatusPage() {
        Page<SpiderRuntime> spiderRuntimes = new PageImpl<>(Lists.newArrayList(spiderContextHolder.getSpiderRuntimeInfo()), PageRequest.of(0, spiderContextHolder.getSpiderRuntimeInfo().size()), spiderContextHolder.getSpiderRuntimeInfo().size());
        return page(AdminSpiderStatusPage.class, spiderRuntimes);
    }

    @PostMapping("/status/{name}/stop")
    public ResponseEntity<RestfulResponse> spiderStatusStop(@PathVariable String name) {
        return execute("停止蜘蛛", context -> {
            Optional<SpiderRuntime> spiderRuntime = spiderContextHolder.getSpiderRuntimeInfo().stream().filter(runtime -> runtime.getName().equals(name)).findAny();
            if (spiderRuntime.isPresent()) {
                try {
                    spiderRuntime.get().getSpider().stop();
                    applicationContext.publishEvent(new AdminSpiderStopEvent(currentUser().orElseThrow(RuntimeException::new), spiderRuntime.get()));
                } catch (Exception e) {
                    throw new ActionException("Can't stop spider " + name, e);
                }
            } else {
                throw new ActionException("No Spider Runtime Information");
            }

        });
    }

    @GetMapping("/log")
    public ModelAndView spiderLogPage(Pageable pageable) {
        return page(AdminSpiderLogPage.class, spiderLogService, pageable);
    }
}
