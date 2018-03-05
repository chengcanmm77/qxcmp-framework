package com.qxcmp.message;

import com.qxcmp.core.support.ThrowingConsumer;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

/**
 * 邮件服务平台统一接口
 * <p>
 * 可以发送邮件到指定邮箱，邮件的形式可以自由组合
 * <p>
 * 支持动态更改邮件服务参数
 *
 * @author aaric
 */
public interface EmailService {

    /**
     * 底层邮件构建接口
     *
     * @param consumer 对邮件参数进行详细配置
     * @throws MailException 如果邮件失败则抛出该异常
     */
    void send(ThrowingConsumer<MimeMessageHelper, MessagingException> consumer) throws MailException;

    /**
     * 配置邮件服务
     * <p>
     * 从系统配置里面取出相应邮件服务参数进行配置
     */
    void config();
}
