package com.qxcmp.web.view.page;

import com.qxcmp.core.init.QxcmpInitializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 平台页面解析服务
 * <p>
 * 用于解析基础页面的实现类型
 *
 * @author Aaric
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PageRevolveService implements QxcmpInitializer {

    private final ApplicationContext applicationContext;

    @Getter
    private Class<? extends AbstractErrorPage> errorPage = DefaultErrorPage.class;

    @Getter
    private Class<? extends AbstractOverviewPage> overviewPage = DefaultOverviewPage.class;

    @Override
    public void init() {
        applicationContext.getBeansOfType(AbstractErrorPage.class).values().stream()
                .filter(abstractErrorPage -> !DefaultErrorPage.class.equals(abstractErrorPage.getClass()))
                .findAny().ifPresent(abstractErrorPage -> errorPage = abstractErrorPage.getClass());
        applicationContext.getBeansOfType(AbstractErrorPage.class).values().stream()
                .filter(abstractErrorPage -> !DefaultErrorPage.class.equals(abstractErrorPage.getClass()))
                .findAny().ifPresent(abstractErrorPage -> errorPage = abstractErrorPage.getClass());

        log.info("Resolve error page: {}", errorPage.getSimpleName());
        log.info("Resolve overview page: {}", overviewPage.getSimpleName());
    }
}
