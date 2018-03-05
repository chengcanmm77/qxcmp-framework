package com.qxcmp.web.view.modules.pagination;

/**
 * 标准分页组件
 *
 * @author Aaric
 */
public class Pagination extends AbstractPagination {
    public Pagination(String url, int current, int total, int pageSize) {
        super(url, current, total, pageSize);
    }

    public Pagination(String url, String queryString, int current, int total, int pageSize) {
        super(url, queryString, current, total, pageSize);
    }
}
