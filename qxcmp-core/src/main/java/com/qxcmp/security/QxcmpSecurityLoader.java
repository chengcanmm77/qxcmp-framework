package com.qxcmp.security;

import com.google.common.collect.ImmutableSet;
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
import static com.qxcmp.core.QxcmpSecurityConfiguration.*;

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

    private static final String ROOT = "administrator";
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

        /*
         * 创建超级角色
         * */
        if (!roleService.findByName("ROOT").isPresent()) {
            roleService.create(() -> {
                Role root = roleService.next();
                root.setName("ROOT");
                root.setDescription("超级用户角色，拥有该角色的用户会拥有所有权限");
                return root;
            });
        }

        /*
         * 创建超级用户
         * */
        if (!userService.findByUsername("administrator").isPresent()) {
            userService.create(() -> {
                User user = userService.next();
                user.setUsername("administrator");
                user.setNickname("超级管理员");
                user.setPassword(new BCryptPasswordEncoder().encode("administrator"));
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setEnabled(true);
                userService.setDefaultPortrait(user);
                return user;
            });
        }

        /*
         * 在平台每次启动的时候重置超级角色拥有的权限，和超级用户的角色
         * */
        roleService.findByName("ROOT").ifPresent(role -> {

            /*
             * 重置超级角色权限
             * */
            roleService.update(role.getId(), r -> r.setPrivileges(Sets.newHashSet(privilegeService.findAll())));

            /*
             * 重置超级管理员角色
             * */
            userService.findByUsername("administrator").ifPresent(user ->
                    userService.update(user.getId(), u -> u.getRoles().add(role))
            );
        });

        initialBuiltInRoles();
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

    private void initialBuiltInRoles() {
        if (!roleService.findByName(ROLE_NEWS).isPresent()) {
            roleService.create(() -> {
                Role role = roleService.next();
                role.setName(ROLE_NEWS);
                role.setDescription(ROLE_NEWS_DESCRIPTION);
                role.setPrivileges(ImmutableSet.of(privilegeService.findByName(PRIVILEGE_NEWS).get(), privilegeService.findByName(PRIVILEGE_SYSTEM_ADMIN).get()));
                return role;
            });
        }
    }
}
