package com.qxcmp.web.api;

import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.core.validation.ImageValidator;
import com.qxcmp.image.Image;
import com.qxcmp.image.ImageService;
import com.qxcmp.web.QxcmpController;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片Web API
 * <p>
 * 负责访问并返回图片内容，没有权限控制
 * <p>
 * 需要激活 Spring Profile {@code api}
 *
 * @author aaric
 */
@Controller
@RequestMapping("/api/image/")
@RequiredArgsConstructor
public class ImageApi extends QxcmpController {

    private final ImageService imageService;

    @GetMapping("{id}.{type}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id, @PathVariable String type) {
        try {
            return imageService.findOne(id).filter(image -> image.getType().equalsIgnoreCase(type)).map(image ->
                    ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.parseMediaType(String.format("image/%s", image.getType())))
                            .body(image.getContent()))
                    .orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
        try {
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

            if (new ImageValidator().isValid(file, null)) {
                Image image = imageService.store(file.getInputStream(), fileExtension);

                if (systemConfigService.getBoolean(QxcmpSystemConfig.IMAGE_WATERMARK_ENABLE).orElse(QxcmpSystemConfig.IMAGE_WATERMARK_ENABLE_DEFAULT)) {
                    imageService.addWatermark(image, siteService.getTitle(), Positions.values()[systemConfigService.getInteger(QxcmpSystemConfig.IMAGE_WATERMARK_POSITION).orElse(QxcmpSystemConfig.IMAGE_WATERMARK_POSITION_DEFAULT)], systemConfigService.getInteger(QxcmpSystemConfig.IMAGE_WATERMARK_FONT_SIZE).orElse(QxcmpSystemConfig.IMAGE_WATERMARK_FONT_SIZE_DEFAULT));
                }

                return ResponseEntity.ok(String.format("/api/image/%s.%s", image.getId(), image.getType()));
            } else {
                return ResponseEntity.badRequest().body("仅支持以下图片格式:jpg,gif,png");
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }
}
