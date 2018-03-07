package com.qxcmp.config;

import com.google.common.base.CaseFormat;
import com.qxcmp.core.init.QxcmpInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 平台系统配置加载器
 * <p>
 * 负责加载所有实现{@link SystemConfigLoader}接口的Spring Bean，并加载相应的系统配置
 * <p>
 * 具体加载方式如下：
 * <ol>
 * <li>如果字段以<b>{@code public static String}</b>修饰，并且字段名称不含有 {@code $} 字符，则加载该字段</li>
 * <li>如果字段值不为空，则系统配置的名称为该字段值</li>
 * <li>如果字段值为空，则根据字段名称转换系统配置名称并设置该字段值为转换后的值</li>
 * <li>以字段所在类名为名称空间</li>
 * <li>去掉{@link #DEFAULT_VALUE_SUFFIX}以后，用{@code .} 代替{@code _}并把所有字母转换为小写</li>
 * <li>寻找与字段名称相同并且以{@link #DEFAULT_VALUE_SUFFIX}结尾的字段</li>
 * <li>如果找到，则设置系统配置的值为该字段的值</li>
 * </ol>
 *
 * @author aaric
 * @see SystemConfigLoader
 * @see SystemConfig
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
@Component
@RequiredArgsConstructor
public class QxcmpSystemConfigLoader implements QxcmpInitializer {

    private static final String DEFAULT_VALUE_SUFFIX = "_DEFAULT";
    private final ApplicationContext applicationContext;
    private final SystemConfigService systemConfigService;

    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void init() {
        log.info("Start loading system config");
        applicationContext.getBeansOfType(SystemConfigLoader.class).forEach((s, o) -> loadFromClass(o));
        log.info("Finish loading system config, total {}", counter.intValue());
    }

    private void loadFromClass(Object bean) {

        String prefix = getSystemConfigPrefix(bean);

        Arrays.stream(bean.getClass().getFields())
                .filter(field -> !StringUtils.contains(field.getName(), "$"))
                .filter(field -> !StringUtils.endsWith(field.getName(), DEFAULT_VALUE_SUFFIX))
                .filter(field -> Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);

                        String systemConfigName = Objects.isNull(field.get(bean)) ? "" : field.get(bean).toString();

                        if (StringUtils.isBlank(systemConfigName)) {
                            systemConfigName = prefix + field.getName().replaceAll("_", ".").toLowerCase();
                            field.set(this, systemConfigName);
                        }

                        String systemConfigValue = "";

                        try {
                            Field defaultValueField = bean.getClass().getField(field.getName() + DEFAULT_VALUE_SUFFIX);
                            defaultValueField.setAccessible(true);
                            systemConfigValue = defaultValueField.get(bean).toString();
                        } catch (Exception ignored) {
                        }

                        String value = systemConfigService.create(systemConfigName, systemConfigValue)
                                .map(SystemConfig::getValue)
                                .orElse(systemConfigService.getString(systemConfigName).orElse(""));

                        log.info("Loading [{}][{}]:[{}]", bean.getClass().getSimpleName(), systemConfigName, value);
                        counter.incrementAndGet();

                    } catch (IllegalAccessException e) {
                        log.error("Can't get system init information {}:{}", bean.getClass().getSimpleName(), field.getName());
                    }
                });
    }

    /**
     * 获取系统配置名称空间
     *
     * @param bean 含有配置信息的类
     *
     * @return 系统配置名称空间
     */
    private String getSystemConfigPrefix(Object bean) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.substringBefore(bean.getClass().getSimpleName().replaceAll("SystemConfig", ""), "$$")).replaceAll("_", ".") + ".";
    }
}
