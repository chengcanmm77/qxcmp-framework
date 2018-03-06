package com.qxcmp.account.auth;

import com.qxcmp.core.QxcmpConfiguration;
import com.qxcmp.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户信息获取接口，属于框架基本功能，供平台和前端共同使用
 * <p>
 * 配置在{@link QxcmpConfiguration}中，负责通过输入的登录账号找到对应的用户实体
 * <p>
 * 目前支持的登录方式有： <ol> <li>用户名登录</li> <li>邮箱登录</li> <li>手机号登录</li> </ol>
 *
 * @author aaric
 */
@Service
@AllArgsConstructor
public class BaseUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
