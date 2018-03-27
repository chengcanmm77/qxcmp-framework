package com.qxcmp.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 短信验证码通用参数对象
 *
 * @author Aaric
 */
@Data
@AllArgsConstructor
public class SmsCaptchaPara {
    private String captcha;
}
