package com.qxcmp.account;

import com.google.common.collect.Lists;
import com.qxcmp.account.event.UserLogonEvent;
import com.qxcmp.account.form.*;
import com.qxcmp.config.SiteService;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.core.init.QxcmpInitializer;
import com.qxcmp.message.EmailService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpSystemConfig.*;

/**
 * 账户模块服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class AccountService implements QxcmpInitializer {

    private final ApplicationContext applicationContext;
    private final UserService userService;
    private final EmailService emailService;
    private final AccountCodeService codeService;
    private final SystemConfigService systemConfigService;
    private final SiteService siteService;
    private final AccountSecurityQuestionService securityQuestionService;

    /**
     * 平台当前激活的模块
     */
    private List<AccountComponent> activateComponents = Lists.newArrayList();

    public List<AccountComponent> getRegisterItems() {
        return activateComponents;
    }

    public List<AccountComponent> getResetItems() {
        return activateComponents.stream().filter(accountComponent -> !accountComponent.isDisableReset()).collect(Collectors.toList());
    }

    public void loadConfig() {
        activateComponents.clear();

        if (systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false)) {
            activateComponents.add(AccountComponent.builder()
                    .registerName("用户名注册").registerUrl("/account/logon/username")
                    .resetName("密保找回").resetUrl("/account/reset/username").build());
        }
        if (systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).orElse(false)) {
            activateComponents.add(AccountComponent.builder()
                    .registerName("邮箱注册").registerUrl("/account/logon/email")
                    .resetName("邮箱找回").resetUrl("/account/reset/email").build());
        }
        if (systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).orElse(false)) {
            activateComponents.add(AccountComponent.builder()
                    .registerName("手机号注册").registerUrl("/account/logon/phone")
                    .resetName("短信找回").resetUrl("/account/reset/phone").build());
        }
        if (systemConfigService.getBoolean(ACCOUNT_ENABLE_INVITE).orElse(false)) {
            activateComponents.add(AccountComponent.builder()
                    .registerName("邀请码注册").registerUrl("/account/logon/invite")
                    .disableReset(true).build());
        }
    }


    /**
     * 向用户发送激活链接邮件
     *
     * @param user 要激活的用户
     */
    public void sendActivateEmail(User user) {
        emailService.send(mimeMessageHelper -> {
            AccountCode code = codeService.nextActivateCode(user.getId());
            String subject = String.format("【%s】", siteService.getTitle()) + systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT_DEFAULT);
            String content = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT_DEFAULT);
            content = content.replaceAll("\\$\\{link}", String.format("https://%s/account/activate/%s", siteService.getDomain(), code.getId()));
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
        });
    }

    /**
     * 向用户发送密码重置链接邮件
     *
     * @param user 要重置的用户
     */
    public void sendResetEmail(User user) {
        emailService.send(mimeMessageHelper -> {
            AccountCode code = codeService.nextPasswordCode(user.getId());
            String subject = String.format("【%s】", siteService.getTitle()) + systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT_DEFAULT);
            String content = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT_DEFAULT);
            content = content.replaceAll("\\$\\{link}", String.format("https://%s/account/reset/%s", siteService.getDomain(), code.getId()));
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
        });
    }

    /**
     * 向用户发送绑定邮箱邮件
     *
     * @param email   要绑定的邮箱
     * @param captcha 绑定验证码
     */
    public void sendBindEmail(String email, String captcha) {
        emailService.send(mimeMessageHelper -> {
            String subject = String.format("【%s】", siteService.getTitle()) + systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT_DEFAULT);
            String content = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT_DEFAULT);
            content = content.replaceAll("\\$\\{captcha}", captcha);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
        });
    }

    @Override
    public void init() {
        loadConfig();
    }

    /**
     * 进行用户名注册
     *
     * @param form          注册表单
     * @param bindingResult 错误对象
     */
    public void logon(AccountLogonUsernameForm form, BindingResult bindingResult) {

        if (userService.findById(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "Username.exist");
        }

        if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return;
        }

        applicationContext.publishEvent(new UserLogonEvent(userService.create(() -> {
            User user = userService.next();
            user.setUsername(form.getUsername());
            user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            userService.setDefaultPortrait(user);
            return user;
        })));
    }

    /**
     * 进行邮箱注册
     *
     * @param form          注册表单
     * @param bindingResult 错误对象
     */
    public void logon(AccountLogonEmailForm form, BindingResult bindingResult) {

        if (userService.findById(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "Username.exist");
        }

        if (userService.findById(form.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "Email.exist");
        }

        if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return;
        }

        User user = userService.create(() -> {
            User next = userService.next();
            next.setUsername(form.getUsername());
            next.setEmail(form.getEmail());
            next.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
            next.setAccountNonExpired(true);
            next.setAccountNonLocked(true);
            next.setCredentialsNonExpired(true);
            next.setEnabled(false);
            userService.setDefaultPortrait(next);
            return next;
        });

        sendActivateEmail(user);
    }

    /**
     * 获取用户密保问题并对表单进行效验
     *
     * @param form          表单
     * @param bindingResult 错误对象
     *
     * @return 用户密保问题
     */
    public AccountSecurityQuestion getUserSecurityQuestion(AccountResetUsernameForm form, BindingResult bindingResult) {
        Optional<User> user = userService.findById(form.getUsername());

        if (!user.isPresent()) {
            bindingResult.rejectValue("username", "Account.reset.noUsername");
        } else {
            return securityQuestionService.findByUserId(user.get().getId()).orElseGet(() -> {
                bindingResult.rejectValue("username", "Account.reset.noQuestion");
                return null;
            });
        }

        return null;
    }

    /**
     * 验证用户密保问题，如果验证成功，返回重置码
     *
     * @param form 表单
     *
     * @return 账户重置码
     */
    public AccountCode validateSecurityQuestion(AccountResetUsernameQuestionForm form) {
        AccountSecurityQuestion securityQuestion = securityQuestionService.findByUserId(form.getUserId()).orElseThrow(RuntimeException::new);
        int count = 0;
        if (form.getAnswer1().equals(securityQuestion.getAnswer1())) {
            count++;
        }
        if (form.getAnswer2().equals(securityQuestion.getAnswer2())) {
            count++;
        }
        if (form.getAnswer3().equals(securityQuestion.getAnswer3())) {
            count++;
        }
        if (count >= 2) {
            return codeService.nextPasswordCode(securityQuestion.getUserId());
        }
        return null;
    }

    /**
     * 重置用户密码
     *
     * @param id            重置ID
     * @param form          表单
     * @param bindingResult 错误对象
     */
    public void reset(String id, AccountResetForm form, BindingResult bindingResult) {
        AccountCode code = codeService.findOne(id).orElseThrow(RuntimeException::new);
        if (!StringUtils.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }
        userService.update(code.getUserId(), user -> {
            user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
            codeService.delete(code);
        });
    }

    /**
     * 邮件重置用户密码
     *
     * @param form          表单
     * @param bindingResult 错误对象
     */
    public void reset(AccountResetEmailForm form, BindingResult bindingResult) {
        Optional<User> userOptional = userService.findByEmail(form.getEmail());
        if (!userOptional.isPresent()) {
            bindingResult.rejectValue("email", "Account.reset.noEmail");
        }
        if (bindingResult.hasErrors()) {
            return;
        }
        userOptional.ifPresent(this::sendResetEmail);
    }
}
