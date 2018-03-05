package com.qxcmp.util;


import com.qxcmp.exception.CaptchaExpiredException;
import com.qxcmp.exception.CaptchaIncorrectException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

/**
 * 验证码生成工具
 *
 * @author Aaric
 */
@Component
public class CaptchaService {

    public static final String CAPTCHA_SESSION_ATTR = "captcha";

    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 60;

    private static final int fontHeight = 30;

    private static final String IMAGE_FONT = "Fixedsys";
    private static final Color IMAGE_BG_COLOR = Color.WHITE;
    private static final Color IMAGE_BORDER_COLOR = Color.BLACK;

    private static final int CODE_X = 30;
    private static final int CODE_Y = 40;

    private static Random random = new Random();

    public Captcha next(int captchaLength) {
        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = initImage(bufferedImage);
        StringBuilder captcha = generateCode(graphics, captchaLength, false);
        return new Captcha(bufferedImage, captcha.toString(), new Date());
    }

    public Captcha next(int captchaLength, boolean numberOnly) {
        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = initImage(bufferedImage);
        StringBuilder captcha = generateCode(graphics, captchaLength, numberOnly);
        return new Captcha(bufferedImage, captcha.toString(), new Date());
    }

    /**
     * 检查验证码是否有效
     *
     * @param captcha     受检查的验证码
     * @param captchaText 用户输入的验证码
     */
    public void verify(Captcha captcha, String captchaText) throws CaptchaExpiredException, CaptchaIncorrectException {
        if (!StringUtils.equalsIgnoreCase(captchaText, captcha.getCaptcha())) {
            throw new CaptchaIncorrectException();
        }

        if (System.currentTimeMillis() - captcha.getDateCreated().getTime() > 60000) {
            throw new CaptchaExpiredException();
        }
    }

    private Graphics initImage(BufferedImage buffImg) {
        Graphics gd = buffImg.getGraphics();

        gd.setColor(IMAGE_BG_COLOR);
        gd.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        Font font = new Font(IMAGE_FONT, Font.BOLD, fontHeight);
        gd.setFont(font);

        gd.setColor(IMAGE_BORDER_COLOR);
        gd.drawRect(0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1);

        gd.setColor(Color.BLACK);

        for (int i = 0; i < 40; i++) {
            int startX = random.nextInt(IMAGE_WIDTH);
            int startY = random.nextInt(IMAGE_HEIGHT);
            int offsetX = random.nextInt(12);
            int offsetY = random.nextInt(12);
            gd.drawLine(startX, startY, startX + offsetX, startY + offsetY);
        }

        return gd;
    }

    private StringBuilder generateCode(Graphics gd, int captchaLength, boolean numberOnly) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < captchaLength; i++) {
            String codePoint = numberOnly ? RandomStringUtils.randomNumeric(1) : RandomStringUtils.randomAlphanumeric(1);
            gd.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            gd.drawString(codePoint, (i + 1) * CODE_X, CODE_Y);
            stringBuilder.append(codePoint);
        }

        return stringBuilder;
    }

}
