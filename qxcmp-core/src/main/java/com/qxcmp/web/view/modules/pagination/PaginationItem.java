package com.qxcmp.web.view.modules.pagination;

import lombok.Data;

/**
 * 分页项目数据对象
 *
 * @author Aaric
 */
@Data
public class PaginationItem {

    /**
     * 超链接
     */
    private String url;

    /**
     * 当前页数
     */
    private int page;

    /**
     * 是否为选中状态
     */
    private boolean active;
}
