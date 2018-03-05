package com.qxcmp.web.api;

import com.qxcmp.web.QxcmpController;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_FILE_UPLOAD_TEMP_FOLDER;

/**
 * 文件上传通用接口
 * <p>
 * 接受文件异步上传、上传后将存放于临时文件夹，并返回文件标识
 * <p>
 * 临时文件里面的文件保存一个小时以后自动删除
 * <p>
 * 标识为时间戳
 * <p>
 * 时间戳为文件夹的名称
 * <p>
 * 文件夹里面存在原始上传文件（包含原始文件文件名）
 *
 * @author Aaric
 */
@Controller
@RequestMapping("/api/file/")
public class FileUploadAPI extends QxcmpController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            File parent = new File(QXCMP_FILE_UPLOAD_TEMP_FOLDER + timestamp);
            FileUtils.forceMkdir(parent);
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(parent, file.getOriginalFilename()));
            return ResponseEntity.ok(timestamp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }
}
