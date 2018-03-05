package com.qxcmp.config;

import com.qxcmp.core.init.QxcmpInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 系统配置加载器
 * <p>
 * 负责加载所有使用{@link SystemConfigAutowired}注解的Spring Bean，并加载相应的系统配置
 * <p>
 * 具体加载方式如下： <ol> <li>如果字段以{@link SystemConfigAutowired#prefix()}开头，则加载该字段</li> <li>如果字段值不为空，则系统配置的名称为该字段值</li>
 * <li>如果字段值为空，则根据字段名称转换系统配置名称并设置该字段值为转换后的值</li> <li>去掉{@link SystemConfigAutowired#prefix()}以后，用{@code .} 代替{@code
 * _}并把所有字母转换为小写</li> <li>寻找与字段名称相同并且以{@link SystemConfigAutowired#suffix()}结尾的字段</li> <li>如果找到，则设置系统配置的值为该字段的值</li>
 * </ol>
 *
 * @author aaric
 * @see SystemConfigAutowired
 * @see SystemConfig
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemConfigLoader implements QxcmpInitializer {

    private static final String DEFAULT_CONFIG_PREFIX = "SYSTEM_CONFIG_";
    private static final String DEFAULT_VALUE_SUFFIX = "_DEFAULT_VALUE";
    private final ApplicationContext applicationContext;
    private final SystemConfigService systemConfigService;
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void init() {
        log.info("Start loading system config");
        applicationContext.getBeansOfType(QxcmpSystemConfig.class).forEach((s, o) -> loadFromClass(o));
        log.info("Finish loading system config, total {}", counter.intValue());
    }

    private void loadFromClass(Object bean) {

        Arrays.stream(bean.getClass().getFields()).filter(field -> StringUtils.startsWith(field.getName(), DEFAULT_CONFIG_PREFIX) && !field.getName().endsWith(DEFAULT_VALUE_SUFFIX)).forEach(field -> {
            try {
                field.setAccessible(true);

                String systemConfigName = Objects.isNull(field.get(bean)) ? "" : field.get(bean).toString();

                if (StringUtils.isBlank(systemConfigName)) {
                    systemConfigName = field.getName().replaceAll(DEFAULT_CONFIG_PREFIX, "").toLowerCase().replaceAll("_", ".");
                    field.set(this, systemConfigName);
                }

                log.info("Loading {}", systemConfigName);
                counter.incrementAndGet();

                try {
                    Field defaultValueField = bean.getClass().getField(field.getName() + DEFAULT_VALUE_SUFFIX);
                    defaultValueField.setAccessible(true);
                    String systemConfigDefaultValue = defaultValueField.get(bean).toString();
                    systemConfigService.create(systemConfigName, systemConfigDefaultValue);
                } catch (NoSuchFieldException e) {
                    systemConfigService.create(systemConfigName, "");
                }

            } catch (IllegalAccessException e) {
                log.error("Can't get system init information {}:{}", bean.getClass().getSimpleName(), field.getName());
            }
        });
    }
}
