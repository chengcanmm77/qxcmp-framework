package com.qxcmp.security;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.qxcmp.core.init.QxcmpInitializer;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpSecurityConfiguration.*;

/**
 * 平台安全配置
 * <p>
 * 负责创建超级用户以及相关权限，以及重置超级权限
 *
 * @author aaric
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@AllArgsConstructor
public class SecurityConfigurator implements QxcmpInitializer {

    private UserService userService;

    private RoleService roleService;

    private PrivilegeService privilegeService;

    @Override
    public void init() {

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
