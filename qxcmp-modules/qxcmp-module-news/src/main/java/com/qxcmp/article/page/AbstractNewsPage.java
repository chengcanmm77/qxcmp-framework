package com.qxcmp.article.page;

import com.qxcmp.admin.page.AbstractQxcmpAdminPage;
import com.qxcmp.article.Article;
import com.qxcmp.web.view.modules.table.AbstractTable;
import com.qxcmp.web.view.modules.table.dictionary.CollectionValueCell;

import java.text.SimpleDateFormat;

/**
 * @author Aaric
 */
public abstract class AbstractNewsPage extends AbstractQxcmpAdminPage {

    private static final int MAX_COUNT = 99;

    protected void setMenuBadge(String menuId, Long count) {
        if (count > 0) {
            if (count > MAX_COUNT) {
                setMenuBadge(menuId, MAX_COUNT + "+");
            } else {
                setMenuBadge(menuId, String.valueOf(count));
            }
        }
    }

    /**
     * 获取文章详情表格组件
     *
     * @param article 文章
     *
     * @return 文章详情表格组件
     */
    protected AbstractTable getArticleDetailsTable(Article article) {
        return viewHelper.nextTable(objectObjectMap -> {
            objectObjectMap.put("所属栏目", new CollectionValueCell(article.getChannels(), "name"));
            objectObjectMap.put("文章摘要", article.getDigest());
            objectObjectMap.put("文章状态", article.getStatus().getName());
            objectObjectMap.put("创建日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateCreated()));
            objectObjectMap.put("上次修改", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateModified()));
            switch (article.getStatus()) {
                case NEW:
                    break;
                case AUDITING:
                    objectObjectMap.put("申请说明", article.getAuditRequest());
                    objectObjectMap.put("申请日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateAuditing()));
                    break;
                case REJECT:
                    objectObjectMap.put("驳回原因", article.getAuditResponse());
                    objectObjectMap.put("驳回日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDateRejected()));
                    userService.findOne(article.getAuditor()).ifPresent(user -> objectObjectMap.put("审核人", user.getDisplayName()));
                    break;
                case PUBLISHED:
                    objectObjectMap.put("发布说明", article.getAuditResponse());
                    objectObjectMap.put("发布日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDatePublished()));
                    userService.findOne(article.getAuditor()).ifPresent(user -> objectObjectMap.put("审核人", user.getDisplayName()));
                    break;
                case DISABLED:
                    objectObjectMap.put("禁用日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(article.getDatePublished()));
                    userService.findOne(article.getAuditor()).ifPresent(user -> objectObjectMap.put("审核人", user.getDisplayName()));
                    userService.findOne(article.getDisableUser()).ifPresent(user -> objectObjectMap.put("禁用人", user.getDisplayName()));
                    break;
                default:
                    break;
            }
        });
    }
}
