package com.qxcmp.core.extension;

import com.qxcmp.core.QxcmpConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 平台拓展点加载
 *
 * @author Aaric
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ExtensionConfigurator implements QxcmpConfigurator {

    private final ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    @Override
    public void config() {
        log.info("Start load extension points...");

        applicationContext.getBeansOfType(ExtensionPoint.class).forEach((s, extensionPoint) -> {
            log.info("Loading extension point {}", extensionPoint.getClass().getName());

            try {
                applicationContext.getBeansOfType(extensionPoint.getExtension()).forEach((n, extension) -> {
                    if (extension instanceof Extension) {
                        log.info("Add extension {}", extension.getClass().getName());
                        extensionPoint.addExtension((Extension) extension);
                    }
                });
            } catch (Exception e) {
                log.error("Can't load extension for {}, cause {}", extensionPoint.getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
