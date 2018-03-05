package com.qxcmp.config;

import com.qxcmp.core.QxcmpConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

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
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemConfigLoader implements QxcmpConfigurator {

    private final ApplicationContext applicationContext;

    private final SystemConfigService systemConfigService;

    @Override
    public void config() {
        applicationContext.getBeansWithAnnotation(SystemConfigAutowired.class).forEach((s, o) -> loadFromClass(o));
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 1;
    }

    private void loadFromClass(Object bean) {

        SystemConfigAutowired systemConfigAutowired = bean.getClass().getAnnotation(SystemConfigAutowired.class);

        Arrays.stream(bean.getClass().getFields()).filter(field -> field.getName().startsWith(systemConfigAutowired.prefix()) && !field.getName().endsWith(systemConfigAutowired.suffix())).forEach(field -> {
            try {
                field.setAccessible(true);

                String systemConfigName = Objects.isNull(field.get(bean)) ? "" : field.get(bean).toString();

                if (StringUtils.isBlank(systemConfigName)) {
                    systemConfigName = field.getName().replaceAll(systemConfigAutowired.prefix(), "").toLowerCase().replaceAll("_", ".");
                    field.set(this, systemConfigName);
                }

                try {
                    Field defaultValueField = bean.getClass().getField(field.getName() + systemConfigAutowired.suffix());
                    defaultValueField.setAccessible(true);
                    String systemConfigDefaultValue = defaultValueField.get(bean).toString();
                    systemConfigService.create(systemConfigName, systemConfigDefaultValue);
                } catch (NoSuchFieldException e) {
                    systemConfigService.create(systemConfigName, "");
                }
            } catch (IllegalAccessException e) {
                log.error("Can't get system config information {}:{}", bean.getClass().getSimpleName(), field.getName());
            }
        });
    }
}
