package com.qxcmp.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 图片验证码
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public class Captcha {

    /**
     * 与验证码关联的图片
     */
    private BufferedImage image;

    /**
     * 验证码内容
     */
    private String captcha;

    /**
     * 验证码创建日期
     */
    private Date dateCreated;
}
