package com.qxcmp.core.init;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

/**
 * 平台初始化
 * <p>
 * 在平台启动的最后进行平台初始化操作
 * <p>
 * 加载所有实现了{@link QxcmpInitailizer} 接口的Spring Bean，排序以后分别执行初始化操作
 * <p>
 * 可使用 {@link org.springframework.core.annotation.Order} 标注加载顺序
 *
 * @author aaric
 * @see QxcmpInitailizer
 */
@Slf4j
@Component
@AllArgsConstructor
public class QxcmpRunner implements ApplicationRunner {

    private static final String SEPARATOR = "------------------------------------------------------------------------";
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments applicationArguments) {

        log.info(SEPARATOR);
        log.info("Start Initializing");
        log.info(SEPARATOR);

        applicationContext.getBeansOfType(QxcmpInitailizer.class).values().stream().sorted(new AnnotationAwareOrderComparator()).forEach(qxcmpConfigurator -> {
            try {
                log.info(SEPARATOR);
                log.info("Initializing {}", qxcmpConfigurator.name());
                log.info(SEPARATOR);
                qxcmpConfigurator.config();
            } catch (Exception e) {
                log.error("Can't initialize {}, cause: {}", qxcmpConfigurator.name(), e.getMessage());
                e.printStackTrace();
            }
        });

        log.info(SEPARATOR);
        log.info("Finish Initializing at {}", DateTime.now().toString());
        log.info(SEPARATOR);
    }
}
