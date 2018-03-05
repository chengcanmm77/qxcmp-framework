package com.qxcmp.core.jobs;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfigConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_FILE_UPLOAD_TEMP_FOLDER;

/**
 * 上传临时文件清理定时任务
 * <p>
 * 该定时任务将在每天自动清理 tmp 目前下午超时的临时上传文件
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FileUploadCleanupJob {

    /**
     * 清理任务每天执行一次
     */
    private final long INTERVAL = 3600000 * 24;

    private final SystemConfigService systemConfigService;

    @Scheduled(initialDelay = INTERVAL, fixedRate = INTERVAL)
    public void cleanUp() {
        log.info("Clean up upload file tmp folder...");
        long currentTimeMillis = System.currentTimeMillis();
        int duration = systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION_DEFAULT_VALE) * 60 * 1000;

        FileUtils.listFilesAndDirs(new File(QXCMP_FILE_UPLOAD_TEMP_FOLDER), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream().filter(File::isDirectory).forEach(file -> {
            try {
                String timestamp = StringUtils.substringAfterLast(file.getPath(), File.separator);

                if (currentTimeMillis - Long.parseLong(timestamp) > duration) {
                    FileUtils.forceDelete(file);
                    log.info("Remove tmp upload file {}", file.getAbsolutePath());
                }

            } catch (Exception ignored) {

            }
        });
    }
}
