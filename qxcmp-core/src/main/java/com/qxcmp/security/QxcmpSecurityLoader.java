package com.qxcmp.security;

import com.google.common.collect.Sets;
import com.qxcmp.core.init.QxcmpInitializer;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkState;

/**
 * 平台安全配置
 * <p>
 * 负责创建超级用户以及相关权限，以及重置超级权限
 *
 * @author aaric
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@RequiredArgsConstructor
public class QxcmpSecurityLoader implements QxcmpInitializer {

    private static final String ROOT_USERNAME = "administrator";
    private static final String ROOT_ROLE = "ROOT_USERNAME";
    private static final String DEFAULT_PREFIX = "PRIVILEGE_";
    private static final String DEFAULT_SUFFIX = "_DESCRIPTION";

    private final ApplicationContext applicationContext;
    private final UserService userService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void init() {
        loadPrivilege();
        loadRootUser();
    }

    /**
     * 加载平台权限
     */
    private void loadPrivilege() {
        log.info("Start loading privilege");
        applicationContext.getBeansOfType(SecurityLoader.class).values().forEach(bean -> {
            Arrays.stream(bean.getClass().getFields())
                    .filter(field -> field.getName().startsWith(DEFAULT_PREFIX) && !field.getName().endsWith(DEFAULT_SUFFIX))
                    .filter(field -> Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                    .forEach(field -> {
                        counter.incrementAndGet();
                        try {
                            field.setAccessible(true);

                            String privilegeName = field.get(bean).toString();
                            checkState(StringUtils.isNotEmpty(privilegeName), "Empty privilege name");
                            String privilegeDescField = field.getName() + DEFAULT_SUFFIX;
                            String privilegeDescription = "暂无描述";

                            try {
                                Field defaultPrivilegeDesc = bean.getClass().getField(privilegeDescField);
                                defaultPrivilegeDesc.setAccessible(true);
                                privilegeDescription = defaultPrivilegeDesc.get(bean).toString();
                            } catch (NoSuchFieldException ignored) {
                            }

                            log.info("Loading: {}({})", privilegeName, privilegeDescription);
                            privilegeService.create(privilegeName, privilegeDescription);

                        } catch (IllegalStateException e) {
                            log.error("Can't create privilege {}:{}, cause: {}", bean.getClass().getSimpleName(), field.getName(), e.getMessage());
                        } catch (IllegalAccessException e) {
                            log.error("Can't get privilege information {}:{}", bean.getClass().getSimpleName(), field.getName());
                        }
                    });
        });
        log.info("Finish loading privilege, total {}", counter.intValue());
    }

    /**
     * 创建超级用户并重置所有权限
     */
    private void loadRootUser() {
        userService.update(userService.findByUsername(ROOT_USERNAME).orElseGet(() -> userService.create(() -> {
            User next = userService.next();
            next.setUsername(ROOT_USERNAME);
            next.setNickname("超级管理员");
            next.setPassword(new BCryptPasswordEncoder().encode(ROOT_USERNAME));
            next.setAccountNonExpired(true);
            next.setAccountNonLocked(true);
            next.setCredentialsNonExpired(true);
            next.setEnabled(true);
            userService.setDefaultPortrait(next);
            return next;
        })).getId(), user -> user.getRoles().add(loadRootRole()));
    }

    /**
     * 加载并重置平台内置超级用户角色
     * 超级用户角色包含了所有权限
     *
     * @return 超级用户角色
     */
    private Role loadRootRole() {
        return roleService.update(roleService.findByName(ROOT_ROLE).orElseGet(() -> roleService.create(() -> {
            Role next = roleService.next();
            next.setName(ROOT_ROLE);
            next.setDescription("超级用户角色，拥有该角色的用户会拥有所有权限");
            return next;
        })).getId(), role -> role.setPrivileges(Sets.newHashSet(privilegeService.findAll())));
    }
}
