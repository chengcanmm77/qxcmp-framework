package com.qxcmp.web.model.navigation;

import com.qxcmp.core.QxcmpConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * 平台后端侧边导航栏加载
 *
 * @author aaric
 */
@Component
@RequiredArgsConstructor
public class NavigationLoader implements QxcmpConfigurator {

    private final ApplicationContext applicationContext;

    private final NavigationService navigationService;

    @Override
    public void config() {
        applicationContext.getBeansOfType(NavigationConfigurator.class).values().stream().sorted(Comparator.comparingInt(NavigationConfigurator::order)).forEach(navigationConfigurator -> navigationConfigurator.configureNavigation(navigationService));
    }
}
