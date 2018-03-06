package com.qxcmp.account;

import com.google.common.collect.Lists;
import com.qxcmp.config.SiteService;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.core.init.QxcmpInitializer;
import com.qxcmp.message.EmailService;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final EmailService emailService;
    private final AccountCodeService codeService;
    private final SystemConfigService systemConfigService;
    private final SiteService siteService;

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
}
