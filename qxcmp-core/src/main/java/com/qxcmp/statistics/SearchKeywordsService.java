package com.qxcmp.statistics;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Aaric
 */
@Service
public class SearchKeywordsService extends AbstractEntityService<SearchKeywords, Long, SearchKeywordsRepository> {

    /**
     * 增加一个搜索关键词记录
     *
     * @param title  关键词
     * @param userId 搜索用户
     */
    public void add(String title, String userId) {
        create(() -> {
            SearchKeywords next = next();
            next.setDateCreated(new Date());
            next.setTitle(title);
            next.setUserId(userId);
            return next;
        });
    }

    /**
     * 查找指定日期内搜索关键字排名最高的结构
     *
     * @param date     日期
     * @param pageable 分页信息
     *
     * @return 排名结果
     */
    public Page<SearchKeywordsPageResult> findByDateCreatedAfter(Date date, Pageable pageable) {
        return repository.findByDateCreatedAfter(date, pageable);
    }

    public Page<SearchKeywordsPageResult> findAllResult(Pageable pageable) {
        return repository.findAllResult(pageable);
    }

}
