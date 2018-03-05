package com.qxcmp.web.view.modules.pagination;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

/**
 * 分页组件抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractPagination extends AbstractComponent {

    /**
     * 分页组件ID
     */
    private String id = "pagination-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 分页数据查找链接
     */
    private String url;

    /**
     * 额外查询参数，不包括 size, page 这两个选项（自动生成）
     */
    private String queryString = "";

    /**
     * 当前页数
     */
    private int current;

    /**
     * 数据总数
     */
    private int total;

    /**
     * 每一页显示几条数据
     */
    private int pageSize;

    /**
     * 每一页显示多少条数据选项
     */
    private List<String> pageSizeOptions = Lists.newArrayList("10", "20", "50", "100");

    /**
     * 是否显示可改变分页数量组件
     */
    private boolean showSizeChanger;

    /**
     * 是否显示快速跳转至某页
     */
    private boolean showQuickJumper;

    /**
     * 是否显示总数
     */
    private boolean showTotal;

    /**
     * 数据总量显示的文本
     * <p>
     * ${total} 为总数占位符
     */
    private String showTotalText = "共${total}条";

    /**
     * 默认显示的项目数量
     * <p>
     * 推荐使用单数
     */
    private int itemCount = 5;

    /**
     * 大小
     */
    private Size size = Size.TINY;

    public AbstractPagination(String url, int current, int total, int pageSize) {
        this.url = url;
        this.current = current;
        this.total = total;
        this.pageSize = pageSize;
    }

    public AbstractPagination(String url, String queryString, int current, int total, int pageSize) {
        this.url = url;
        this.queryString = queryString;
        this.current = current;
        this.total = total;
        this.pageSize = pageSize;
    }

    /**
     * @return 计算该分页组件的页面总数
     */
    public int getTotalPage() {

        if (total == 0) {
            return 1;
        }

        return total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    /**
     * @return 计算该组件分页项目
     */
    public List<PaginationItem> getItemList() {
        List<PaginationItem> paginationItems = Lists.newArrayList();

        int centerCount = itemCount / 2 + 1;
        int start;
        int end;
        int totalPage = getTotalPage();

        if (current < centerCount) {
            start = 1;
            end = itemCount < totalPage ? itemCount : totalPage;
        } else if (current > totalPage - centerCount + 1) {
            start = itemCount > totalPage ? 1 : (totalPage - itemCount + 1);
            end = totalPage;
        } else {
            start = current - centerCount + 1;
            end = current + (itemCount - centerCount);
        }

        for (int i = start; i <= end; i++) {
            PaginationItem paginationItem = new PaginationItem();

            paginationItem.setUrl(getItemUrl(i));
            paginationItem.setPage(i);
            paginationItem.setActive(i == current);

            paginationItems.add(paginationItem);
        }

        return paginationItems;
    }

    /**
     * @param page 目标页面
     *
     * @return 分页项目的超链接
     */
    public String getItemUrl(int page) {
        int targetPage;
        int totalPage = getTotalPage();

        if (page < 1) {
            targetPage = 0;
        } else if (page > totalPage) {
            targetPage = totalPage - 1;
        } else {
            targetPage = page - 1;
        }

        return String.format("%s?size=%d&page=%d", url, pageSize, targetPage) + queryString;
    }

    public String getChangeSizeUrl(String pageSize) {
        return String.format("%s?size=%s&page=%d", url, pageSize, current - 1) + queryString;
    }

    public String getTotalText() {
        return showTotalText.replaceAll("\\$\\{total}", String.valueOf(total));
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/pagination";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return size.toString();
    }

    @Override
    public String getClassSuffix() {
        return "pagination menu";
    }

    public AbstractPagination setPageSizeOptions(List<String> pageSizeOptions) {
        this.pageSizeOptions = pageSizeOptions;
        return this;
    }

    public AbstractPagination setShowSizeChanger() {
        setShowSizeChanger(true);
        return this;
    }

    public AbstractPagination setShowQuickJumper() {
        setShowQuickJumper(true);
        return this;
    }

    public AbstractPagination setShowTotal() {
        setShowTotal(true);
        return this;
    }

    public AbstractPagination setShowTotalText(String showTotalText) {
        this.showTotalText = showTotalText;
        return this;
    }

    public AbstractPagination setItemCount(int itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public AbstractPagination setSize(Size size) {
        this.size = size;
        return this;
    }
}
