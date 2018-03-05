package com.qxcmp.spdier;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.TimeDurationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 蜘蛛日志服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class SpiderLogService extends AbstractEntityService<SpiderLog, Long, SpiderLogRepository> {

    private final TimeDurationHelper timeDurationHelper;


    public void save(SpiderPageProcessor spiderPageProcessor) {
        create(() -> {
            SpiderLog spiderLog = next();
            spiderLog.setSpiderGroup(spiderPageProcessor.getClass().getAnnotation(Spider.class).group());
            spiderLog.setName(spiderPageProcessor.getClass().getAnnotation(Spider.class).name());
            spiderLog.setTargetPageCount(spiderPageProcessor.getTargetPageCount());
            spiderLog.setContentPageCount(spiderPageProcessor.getContentPageCount());
            spiderLog.setDateStart(new Date(spiderPageProcessor.getStartTime()));
            spiderLog.setDuration(timeDurationHelper.convert(System.currentTimeMillis() - spiderPageProcessor.getStartTime()));
            if (!spiderPageProcessor.getSpiderPipelines().isEmpty()) {
                SpiderPipeline spiderPipeline = (SpiderPipeline) spiderPageProcessor.getSpiderPipelines().get(0);
                spiderLog.setNewPageCount(spiderPipeline.getNewCount());
                spiderLog.setUpdatePageCount(spiderPipeline.getUpdateCount());
                spiderLog.setDropPageCount(spiderPipeline.getDropCount());
            }
            return spiderLog;
        });
    }

    @Override
    public Page<SpiderLog> findAll(Pageable pageable) {
        return repository.findAllByOrderByDateFinishDesc(pageable);
    }

}
