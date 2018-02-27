package com.qxcmp.web.view.elements.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 二维码图片
 *
 * @author Aaric
 */
@Getter
@Setter
public class QrCodeImage extends Image {

    public QrCodeImage(String content, int width, int height) {
        super("");

        try {
            BitMatrix qrCode = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(qrCode, "JPG", outputStream);
            setSource(String.format("data:image/png;base64, %s", Base64.getEncoder().encodeToString(outputStream.toByteArray())));
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }
}
