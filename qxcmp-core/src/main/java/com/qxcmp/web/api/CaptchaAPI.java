package com.qxcmp.web.api;

import com.qxcmp.core.validation.PhoneValidator;
import com.qxcmp.message.SmsService;
import com.qxcmp.util.Captcha;
import com.qxcmp.util.CaptchaService;
import com.qxcmp.web.QxcmpController;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT_VALUE;

/**
 * 验证码Web API
 * <p>
 * 负责设置Session验证码并返回图片内容，没有权限控制
 * <p>
 * 需要激活 Spring Profile {@code api}
 *
 * @author aaric
 */
@Controller
@RequestMapping("/api/captcha/")
@RequiredArgsConstructor
public class CaptchaAPI extends QxcmpController {

    private final CaptchaService captchaComponent;

    private final SmsService smsService;

    @GetMapping("image")
    public void imageCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Captcha captcha = captchaComponent.next(systemConfigService.getInteger(SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH).orElse(SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT_VALUE));

        HttpSession session = request.getSession();
        session.setAttribute(CaptchaService.CAPTCHA_SESSION_ATTR, captcha);

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(captcha.getImage(), "jpeg", sos);
        sos.close();
    }

    @PostMapping("phone")
    public ResponseEntity<String> phoneCaptcha(@RequestParam String phone) {

        if (StringUtils.isBlank(phone) || !new PhoneValidator().isValid(phone, null)) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("手机号码格式不正确");
        }

        try {
            Captcha captcha = captchaComponent.next(systemConfigService.getInteger(SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH).orElse(SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT_VALUE), true);

            HttpSession session = request.getSession();
            session.setAttribute(CaptchaService.CAPTCHA_SESSION_ATTR, captcha);

            smsService.sendCaptcha(phone, captcha.getCaptcha());

            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }
}
