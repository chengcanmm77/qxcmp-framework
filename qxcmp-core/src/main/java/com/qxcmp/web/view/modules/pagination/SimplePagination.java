package com.qxcmp.web.view.modules.pagination;

/**
 * 简洁分页组件
 *
 * @author Aaric
 */
public class SimplePagination extends AbstractPagination {

    public SimplePagination(String url, int current, int total, int pageSize) {
        super(url, current, total, pageSize);
    }

    public SimplePagination(String url, String queryString, int current, int total, int pageSize) {
        super(url, queryString, current, total, pageSize);
    }

    @Override
    public String getFragmentName() {
        return "simple";
    }
}
