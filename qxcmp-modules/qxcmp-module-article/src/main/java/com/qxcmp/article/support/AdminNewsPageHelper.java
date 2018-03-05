package com.qxcmp.article.support;

import com.google.common.collect.Maps;
import com.qxcmp.article.Article;
import com.qxcmp.user.UserService;
import com.qxcmp.web.view.modules.table.dictionary.CollectionValueCell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminNewsPageHelper {

    private final UserService userService;

    public Map<Object, Object> getArticleInfoTable(Article article) {
        Map<Object, Object> stringObjectMap = Maps.newLinkedHashMap();
        stringObjectMap.put("所属栏目", new CollectionValueCell(article.getChannels(), "name"));
        stringObjectMap.put("文章摘要", article.getDigest());
        stringObjectMap.put("文章状态", article.getStatus().getName());
        stringObjectMap.put("创建日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateCreated()));
        stringObjectMap.put("上次修改", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateModified()));

        switch (article.getStatus()) {
            case NEW:
                break;
            case AUDITING:
                stringObjectMap.put("申请说明", article.getAuditRequest());
                stringObjectMap.put("申请日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateAuditing()));
                break;
            case REJECT:
                stringObjectMap.put("驳回原因", article.getAuditResponse());
                stringObjectMap.put("驳回日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateRejected()));
                userService.findOne(article.getAuditor()).ifPresent(user -> stringObjectMap.put("审核人", user.getDisplayName()));
                break;
            case PUBLISHED:
                stringObjectMap.put("发布说明", article.getAuditResponse());
                stringObjectMap.put("发布日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDatePublished()));
                userService.findOne(article.getAuditor()).ifPresent(user -> stringObjectMap.put("审核人", user.getDisplayName()));
                break;
            case DISABLED:
                stringObjectMap.put("禁用日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDatePublished()));
                userService.findOne(article.getAuditor()).ifPresent(user -> stringObjectMap.put("审核人", user.getDisplayName()));
                userService.findOne(article.getDisableUser()).ifPresent(user -> stringObjectMap.put("禁用人", user.getDisplayName()));
                break;
        }
        return stringObjectMap;
    }
}
