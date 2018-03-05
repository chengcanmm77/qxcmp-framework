package com.qxcmp.web.view.modules.table;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.elements.header.AbstractHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

@Getter
@Setter
public class EntityTable extends AbstractTable {

    private String id = "table-" + RandomStringUtils.randomAlphanumeric(10);

    private AbstractHeader tableHeader;

    private String entityIndex;

    private String action;

    private boolean multiple;

    private List<EntityTableAction> tableActions = Lists.newArrayList();

    private List<EntityTableBatchAction> batchActions = Lists.newArrayList();

    private List<EntityTableRowAction> rowActions = Lists.newArrayList();

    @Override
    public String getFragmentName() {
        return "entity-table";
    }

    public EntityTable setTableHeader(AbstractHeader tableHeader) {
        this.tableHeader = tableHeader;
        return this;
    }
}
