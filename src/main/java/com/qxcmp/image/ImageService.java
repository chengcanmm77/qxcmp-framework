package com.qxcmp.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 图片服务
 *
 * @author aaric
 */
@Service
public class ImageService extends AbstractEntityService<Image, String, ImageRepository> {

    /**
     * 平台支持的图片类型
     */
    private final static Set<String> SUPPORT_TYPE = new HashSet<>();

    static {
        SUPPORT_TYPE.add("jpg");
        SUPPORT_TYPE.add("jpeg");
        SUPPORT_TYPE.add("png");
        SUPPORT_TYPE.add("gif");
    }

    public ImageService(ImageRepository repository) {
        super(repository);
    }

    @Override
    protected <S extends Image> String getEntityId(S entity) {
        return entity.getId();
    }

    @Override
    public <S extends Image> Optional<S> create(Supplier<S> supplier) {
        S entity = supplier.get();
        checkNotNull(entity, "Image can't be null");
        entity.setId(IDGenerator.sha384());
        entity.setDateCreated(new Date());
        return super.create(() -> entity);
    }

    /**
     * 保存原图
     *
     * @param inputStream 图片流
     * @param type        图片类型
     *
     * @return 保存后的图片
     *
     * @throws IOException 如果存储过程发生异常则抛出该异常
     */
    public Optional<Image> store(InputStream inputStream, String type) throws IOException {
        return store(inputStream, type, 0, 0);
    }

    /**
     * 将图片按照指定格式、宽度和高度存储
     *
     * @param inputStream 图片输入流
     * @param type        图片类型，支持的类型有 {@link #SUPPORT_TYPE}
     * @param width       图片宽度，小于等于0表示自动
     * @param height      图片高度，小于等于0表示自动
     *
     * @return 存储后的图片
     *
     * @throws IOException 如果存储过程发生异常则抛出该异常
     */
    public Optional<Image> store(InputStream inputStream, String type, int width, int height) throws IOException {
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        String imageType = SUPPORT_TYPE.contains(type) ? type : "jpg";

        BufferedImage bufferedImage = ImageIO.read(inputStream);

        Thumbnails.of(bufferedImage)
                .size(width > 0 ? width : bufferedImage.getWidth(), height > 0 ? height : bufferedImage.getHeight())
                .outputFormat(imageType)
                .toOutputStream(content);

        return createImage(imageType, content.toByteArray());
    }

    /**
     * 为一个图片添加水印文本
     *
     * @param target    目标图片
     * @param watermark 水印文本
     *
     * @return 添加后的图片
     */
    public Optional<Image> addWatermark(Image target, String watermark) {
        return addWatermark(target, watermark, Positions.BOTTOM_RIGHT, 18);
    }

    public Optional<Image> addWatermark(Image target, String watermark, Position position, int fontSize) {
        return update(target.getId(), image -> {
            try {
                BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(image.getContent()));

                BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = bufferedImage.createGraphics();
                graphics.setFont(new Font("Microsoft YaHei", Font.PLAIN, fontSize));
                FontMetrics fontMetrics = graphics.getFontMetrics();

                int watermarkWidth = fontMetrics.stringWidth(watermark);
                int watermarkHeight = fontMetrics.getHeight();

                bufferedImage = new BufferedImage(watermarkWidth, watermarkHeight, BufferedImage.TYPE_INT_ARGB);
                graphics = bufferedImage.createGraphics();

                graphics.setComposite(AlphaComposite.Clear);
                graphics.fillRect(0, 0, watermarkWidth, watermarkHeight);

                graphics.setComposite(AlphaComposite.Src);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Microsoft YaHei", Font.PLAIN, fontSize));
                graphics.drawString(watermark, 0, graphics.getFontMetrics().getAscent());
                graphics.dispose();

                ByteArrayOutputStream content = new ByteArrayOutputStream();
                Thumbnails.of(new ByteArrayInputStream(image.getContent()))
                        .size(originImage.getWidth(), originImage.getHeight())
                        .watermark(position, bufferedImage, 0.5f)
                        .toOutputStream(content);

                image.setContent(content.toByteArray());
            } catch (IOException ignored) {

            }
        });
    }

    /**
     * 创建一个二维码图片
     *
     * @param text   二维码文本
     * @param width  图片宽度
     * @param height 图片高度
     *
     * @return 二维码位图
     */
    public BitMatrix createQrCode(String text, int width, int height) {
        try {
            return new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Optional<Image> createImage(String type, byte[] content) {
        return create(() -> {
            Image image = next();
            image.setType(type);
            image.setContent(content);
            return image;
        });
    }
}
