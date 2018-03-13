package com.qxcmp.message;

import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.core.support.ThrowingConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Aaric
 */
@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private SystemConfigService systemConfigService;
    private JavaMailSender javaMailSender;

    @Override
    public void send(ThrowingConsumer<MimeMessageHelper, MessagingException> consumer) throws MailException {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_USERNAME).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_USERNAME_DEFAULT));
            consumer.accept(mimeMessageHelper);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException(e.getMessage(), e);
        }
    }

    @Override
    public void config() {
        log.info("Loading email service");
        JavaMailSenderImpl sender = (JavaMailSenderImpl) javaMailSender;
        sender.setProtocol("smtp");
        sender.setHost(systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_HOSTNAME).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_HOSTNAME_DEFAULT));
        sender.setPort(systemConfigService.getInteger(QxcmpSystemConfig.MESSAGE_EMAIL_PORT).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_PORT_DEFAULT));
        sender.setUsername(systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_USERNAME).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_USERNAME_DEFAULT));
        sender.setPassword(systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_PASSWORD).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_PASSWORD_DEFAULT));

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_HOSTNAME).orElse(QxcmpSystemConfig.MESSAGE_EMAIL_HOSTNAME_DEFAULT));
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.port", systemConfigService.getString(QxcmpSystemConfig.MESSAGE_EMAIL_PORT).orElse(String.valueOf(QxcmpSystemConfig.MESSAGE_EMAIL_PORT_DEFAULT)));
        properties.put("mail.smtp.socketFactory.port", sender.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.starttls.enable", "true");

        sender.setJavaMailProperties(properties);
    }

    @EventListener
    public void onSystemConfigChange(SystemConfigChangeEvent event) {
        if (StringUtils.startsWith(event.getName(), "qxcmp.message.email")) {
            config();
        }
    }
}
