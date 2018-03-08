package com.qxcmp.admin.page;

import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.table.EntityTable;

/**
 * 后台实体表格页面
 *
 * @author Aaric
 */
public abstract class AbstractQxcmpAdminEntityTablePage extends AbstractQxcmpAdminPage {

    @Override
    public void render() {
        addComponent(new Segment().addComponent(renderEntityTable()));
    }

    /**
     * 获取实体表格组件
     *
     * @return 实体表格组件
     */
    protected abstract EntityTable renderEntityTable();
}
