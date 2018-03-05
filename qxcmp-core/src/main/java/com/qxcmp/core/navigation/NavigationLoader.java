package com.qxcmp.core.navigation;

import com.qxcmp.core.init.QxcmpInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

/**
 * 平台后端侧边导航栏加载
 *
 * @author aaric
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NavigationLoader implements QxcmpInitializer {

    private final ApplicationContext applicationContext;
    private final NavigationService navigationService;

    @Override
    public void init() {
        applicationContext.getBeansOfType(NavigationConfigurator.class).values().stream().sorted(new AnnotationAwareOrderComparator()).forEach(navigationConfigurator -> {
            log.info("--- loading navigation {}", navigationConfigurator.getClass().getSimpleName());
            navigationConfigurator.configureNavigation(navigationService);
        });
    }
}
