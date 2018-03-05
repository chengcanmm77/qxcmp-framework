package com.qxcmp.security;

import com.qxcmp.core.init.QxcmpInitailizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;

/**
 * 权限加载器
 * <p>
 * 负责加载所有使用{@link PrivilegeAutowired}注解的Spring Bean，并加载相应的系统配置
 * <p>
 * 具体加载方式如下： <ol> <li>如果字段以{@link PrivilegeAutowired#prefix()}开头，则加载该字段</li> <li>如果字段值为空，则跳过该字段并报错</li>
 * <li>创建以字段值为主键的权限</li> <li>寻找与字段名称相同并且以{@link PrivilegeAutowired#suffix()}结尾的字段</li> <li>如果找到，则设置权限描述信息为该字段值</li>
 * </ol>
 *
 * @author aaric
 * @see PrivilegeAutowired
 * @see Privilege
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Component
@AllArgsConstructor
public class PrivilegeLoader implements QxcmpInitailizer {

    private ApplicationContext applicationContext;

    private PrivilegeService privilegeService;

    @Override
    public void config() {
        applicationContext.getBeansWithAnnotation(PrivilegeAutowired.class).forEach((s, o) -> loadFromClass(o));
    }

    private void loadFromClass(Object bean) {

        PrivilegeAutowired privilegeAutowired = bean.getClass().getAnnotation(PrivilegeAutowired.class);

        Arrays.stream(bean.getClass().getFields()).filter(field -> field.getName().startsWith(privilegeAutowired.prefix()) && !field.getName().endsWith(privilegeAutowired.suffix())).forEach(field -> {
            try {
                field.setAccessible(true);

                String privilegeName = field.get(bean).toString();
                checkState(StringUtils.isNotEmpty(privilegeName), "Empty privilege name");

                String privilegeDescField = field.getName() + privilegeAutowired.suffix();

                try {
                    Field defaultPrivilegeDesc = bean.getClass().getField(privilegeDescField);
                    defaultPrivilegeDesc.setAccessible(true);
                    String privilegeDesc = defaultPrivilegeDesc.get(bean).toString();
                    privilegeService.create(privilegeName, privilegeDesc);
                } catch (NoSuchFieldException e) {
                    privilegeService.create(privilegeName, "暂无描述");
                }

            } catch (IllegalStateException e) {
                log.error("Can't create privilege {}:{}, cause: {}", bean.getClass().getSimpleName(), field.getName(), e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("Can't get privilege information {}:{}", bean.getClass().getSimpleName(), field.getName());
            }
        });
    }
}
