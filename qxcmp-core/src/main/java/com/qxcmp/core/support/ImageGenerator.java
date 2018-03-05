package com.qxcmp.core.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 图像生成器
 * <p>
 * 用于生成不同业务领域的图像
 *
 * @author aaric
 */
@Component
public class ImageGenerator {

    private static final ColorPair[] COLORS = new ColorPair[]{
            new ColorPair(fromHex("#291B2C"), fromHex("#CCA969")),
            new ColorPair(fromHex("#1A2C56"), fromHex("#D1A683")),
            new ColorPair(fromHex("#3B1313"), fromHex("#A66D3F")),
            new ColorPair(fromHex("#2A2927"), fromHex("#FCCF0D")),
            new ColorPair(fromHex("#721C47"), fromHex("#D1A377")),
            new ColorPair(fromHex("#2E3268"), fromHex("#989479")),
            new ColorPair(fromHex("#7F1518"), fromHex("#B69D70")),
            new ColorPair(fromHex("#060709"), fromHex("#DEA527")),
            new ColorPair(fromHex("#22422D"), fromHex("#28B14A")),
            new ColorPair(fromHex("#F98866"), fromHex("#80BD93")),
            new ColorPair(fromHex("#90AFC5"), fromHex("#2A3132")),
            new ColorPair(fromHex("#336B87"), fromHex("#763626")),
            new ColorPair(fromHex("#003B46"), fromHex("#66A5AD")),
            new ColorPair(fromHex("#7D4427"), fromHex("#2E4600")),
            new ColorPair(fromHex("#021C1E"), fromHex("#6FB98F")),
            new ColorPair(fromHex("#FB6542"), fromHex("#FFBB00")),
            new ColorPair(fromHex("#5BC8AC"), fromHex("#F18D93")),
            new ColorPair(fromHex("#324851"), fromHex("#34675C")),
            new ColorPair(fromHex("#4CB5F5"), fromHex("#B3C100")),
            new ColorPair(fromHex("#F4CC70"), fromHex("#20948B")),
            new ColorPair(fromHex("#8d230f"), fromHex("#c99e10")),
            new ColorPair(fromHex("#f1f1f2"), fromHex("#1995ad")),
            new ColorPair(fromHex("#ec96a4"), fromHex("#dfe166")),
            new ColorPair(fromHex("#011a27"), fromHex("#e6df44")),
    };

    public BufferedImage nextPortrait(String username, int size) throws IOException {

        if (username.length() > 2) {
            username = username.substring(0, 2);
        }

        username = username.toUpperCase();

        ColorPair colorPair = COLORS[new Random().nextInt(COLORS.length)];

        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(colorPair.getBackground());
        graphics2D.fillRect(0, 0, size, size);

        graphics2D.setFont(new Font("Microsoft YaHei", Font.BOLD, size / 2));

        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int x = ((size - fontMetrics.stringWidth(username)) / 2);
        int y = ((size - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();

        graphics2D.setColor(colorPair.getText());
        graphics2D.drawString(username, x, y);
        graphics2D.dispose();
        return bufferedImage;
    }

    private static Color fromHex(String hex) {
        return new Color(Integer.valueOf(hex.substring(1, 3), 16), Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(hex.substring(5, 7), 16));
    }

    @Data
    @AllArgsConstructor
    private static class ColorPair {

        private Color background;

        private Color text;

    }
}
