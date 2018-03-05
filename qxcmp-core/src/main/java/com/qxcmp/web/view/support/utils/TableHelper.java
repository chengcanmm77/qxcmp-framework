package com.qxcmp.web.view.support.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qxcmp.web.view.annotation.table.RowActionCheck;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.button.Buttons;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.image.Avatar;
import com.qxcmp.web.view.elements.input.Input;
import com.qxcmp.web.view.elements.label.BasicLabel;
import com.qxcmp.web.view.elements.label.Label;
import com.qxcmp.web.view.modules.dropdown.Selection;
import com.qxcmp.web.view.modules.dropdown.SelectionMenu;
import com.qxcmp.web.view.modules.dropdown.item.TextItem;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.pagination.Pagination;
import com.qxcmp.web.view.modules.table.*;
import com.qxcmp.web.view.modules.table.dictionary.BaseDictionaryValueCell;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 表格视图生成工具类
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class TableHelper {

    private final DefaultFormattingConversionService conversionService;

    /**
     * 生成一个字典表格视图
     * <p>
     * 字典表格视图包含两列，第一列为键，第二列为值
     *
     * @param dictionary 渲染的字典对象
     * @return 表格视图
     */
    public Table convert(Map<Object, Object> dictionary) {
        final Table table = new Table();
        table.setCelled().setBasic().setSize(Size.SMALL);
        table.setBody(new TableBody());

        if (dictionary.isEmpty()) {
            TableRow tableRow = new TableRow();
            TableData tableData = new TableData();
            tableData.setAlignment(Alignment.CENTER);
            tableData.setColSpan(2);
            tableData.setContent("暂无内容");
            tableRow.addCell(tableData);
            table.getBody().addRow(tableRow);
        } else {
            dictionary.forEach((key, value) -> {
                TableRow tableRow = new TableRow();

                parseValueCell(tableRow, key);
                parseValueCell(tableRow, value);

                table.getBody().addRow(tableRow);
            });
        }

        if (dictionary.size() > 5) {
            table.setStriped();
        }

        return table;
    }

    public Table convert(Consumer<Map<Object, Object>> consumer) {
        Map<Object, Object> objectObjectMap = Maps.newLinkedHashMap();
        consumer.accept(objectObjectMap);
        return convert(objectObjectMap);
    }

    private void parseValueCell(TableRow tableRow, Object value) {

        if (Objects.isNull(value)) {
            tableRow.addCell(new TableData(""));
        } else {
            if (value instanceof Boolean) {
                tableRow.addCell(new TableData(Boolean.parseBoolean(value.toString()) ? "是" : "否"));
            } else if (value instanceof BaseDictionaryValueCell) {
                BaseDictionaryValueCell dictionaryValueCell = (BaseDictionaryValueCell) value;
                tableRow.addCell(dictionaryValueCell.render());
            } else {
                tableRow.addCell(new TableData(value.toString()));
            }
        }
    }

    public <T> com.qxcmp.web.view.modules.table.EntityTable convert(String tableName, String action, Class<T> tClass, Page<T> tPage, HttpServletRequest request) {
        checkNotNull(tableName);

        final com.qxcmp.web.view.modules.table.EntityTable table = new com.qxcmp.web.view.modules.table.EntityTable();

        com.qxcmp.web.view.annotation.table.EntityTable entityTable = Arrays.stream(tClass.getDeclaredAnnotationsByType(com.qxcmp.web.view.annotation.table.EntityTable.class)).filter(annotation -> StringUtils.equals(annotation.name(), tableName)).findAny().orElseThrow(() -> new IllegalStateException("No EntityTable definition"));

        table.setAction(action);

        configEntityTable(table, entityTable, tClass);

        renderTableContent(table, entityTable, tClass, tPage, request);

        return table;
    }

    private <T> void configEntityTable(com.qxcmp.web.view.modules.table.EntityTable table, com.qxcmp.web.view.annotation.table.EntityTable entityTable, Class<T> tClass) {

        if (StringUtils.isNotBlank(entityTable.value())) {
            table.setTableHeader(new PageHeader(HeaderType.H2, entityTable.value()).setDividing());
        }

        table.setEntityIndex(entityTable.entityIndex());

        if (StringUtils.isBlank(table.getAction())) {
            if (StringUtils.isNotBlank(entityTable.action())) {
                table.setAction(entityTable.action());
            } else {
                table.setAction(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, tClass.getSimpleName()));
            }
        }

        if (!StringUtils.endsWith(table.getAction(), "/")) {
            table.setAction(table.getAction() + "/");
        }

        table.setDisableFilter(entityTable.disableFilter());
        table.setCelled(entityTable.celled());
        table.setBasic(entityTable.basic());
        table.setVeryBasic(entityTable.veryBasic());
        table.setSingleLine(entityTable.singleLine());
        table.setFixed(entityTable.fixed());
        table.setSelectable(entityTable.selectable());
        table.setStriped(entityTable.striped());
        table.setInverted(entityTable.inverted());
        table.setCollapsing(entityTable.collapsing());
        table.setPadded(entityTable.padded());
        table.setVeryPadded(entityTable.veryPadded());
        table.setCompact(entityTable.compact());
        table.setVeryCompact(entityTable.veryCompact());
        table.setColumnCount(entityTable.columnCount());
        table.setStackDevice(entityTable.stackDevice());
        table.setColor(entityTable.color());
        table.setSize(entityTable.size());

        Arrays.stream(entityTable.tableActions()).forEach(tableAction -> {
            EntityTableAction entityTableAction = new EntityTableAction();

            entityTableAction.setTitle(tableAction.value());
            entityTableAction.setAction(table.getAction() + tableAction.action());
            entityTableAction.setMethod(tableAction.method());
            entityTableAction.setTarget(tableAction.target());
            entityTableAction.setColor(tableAction.color());
            entityTableAction.setPrimary(tableAction.primary());
            entityTableAction.setSecondary(tableAction.secondary());
            entityTableAction.setInverted(tableAction.inverted());
            entityTableAction.setBasic(tableAction.basic());

            if (Objects.equals(tableAction.method(), FormMethod.POST)) {
                entityTableAction.setShowConfirmDialog(tableAction.showConfirmDialog());
                entityTableAction.setConfirmDialogTitle(tableAction.confirmDialogTitle());
                entityTableAction.setConfirmDialogDescription(tableAction.confirmDialogDescription());
            } else {
                entityTableAction.setShowConfirmDialog(false);
            }

            table.getTableActions().add(entityTableAction);
        });

        Arrays.stream(entityTable.batchActions()).forEach(batchAction -> {
            EntityTableBatchAction entityTableBatchAction = new EntityTableBatchAction();

            entityTableBatchAction.setTitle(batchAction.value());
            entityTableBatchAction.setAction(table.getAction() + batchAction.action());
            entityTableBatchAction.setMethod(FormMethod.POST);
            entityTableBatchAction.setTarget(batchAction.target());
            entityTableBatchAction.setColor(batchAction.color());
            entityTableBatchAction.setPrimary(batchAction.primary());
            entityTableBatchAction.setSecondary(batchAction.secondary());
            entityTableBatchAction.setInverted(batchAction.inverted());
            entityTableBatchAction.setBasic(batchAction.basic());

            entityTableBatchAction.setShowConfirmDialog(batchAction.showConfirmDialog());
            entityTableBatchAction.setConfirmDialogTitle(batchAction.confirmDialogTitle());
            entityTableBatchAction.setConfirmDialogDescription(batchAction.confirmDialogDescription());

            table.getBatchActions().add(entityTableBatchAction);
        });

        Arrays.stream(entityTable.rowActions()).forEach(rowAction -> {
            EntityTableRowAction entityTableRowAction = new EntityTableRowAction();

            entityTableRowAction.setTitle(rowAction.value());
            entityTableRowAction.setAction(rowAction.action());
            entityTableRowAction.setMethod(rowAction.method());
            entityTableRowAction.setTarget(rowAction.target());
            entityTableRowAction.setColor(rowAction.color());
            entityTableRowAction.setPrimary(rowAction.primary());
            entityTableRowAction.setSecondary(rowAction.secondary());
            entityTableRowAction.setInverted(rowAction.inverted());
            entityTableRowAction.setBasic(rowAction.basic());

            if (Objects.equals(rowAction.method(), FormMethod.POST)) {
                entityTableRowAction.setShowConfirmDialog(rowAction.showConfirmDialog());
                entityTableRowAction.setConfirmDialogTitle(rowAction.confirmDialogTitle());
                entityTableRowAction.setConfirmDialogDescription(rowAction.confirmDialogDescription());
            } else {
                entityTableRowAction.setShowConfirmDialog(false);
            }

            table.getRowActions().add(entityTableRowAction);
        });

        table.setMultiple(!table.getBatchActions().isEmpty());
    }

    private <T> void renderTableContent(com.qxcmp.web.view.modules.table.EntityTable table, com.qxcmp.web.view.annotation.table.EntityTable entityTable, Class<T> tClass, Page<T> tPage, HttpServletRequest request) {
        final List<EntityTableField> entityTableFields = getEntityTableFields(table, entityTable, tClass);

        renderTableHeader(table, entityTableFields, request);

        renderTableBody(table, entityTableFields, tClass, tPage);

        renderTableFooter(table, entityTableFields, tPage, request);
    }

    private <T> List<EntityTableField> getEntityTableFields(com.qxcmp.web.view.modules.table.EntityTable table, com.qxcmp.web.view.annotation.table.EntityTable entityTable, Class<T> tClass) {
        final List<EntityTableField> entityTableFields = Lists.newArrayList();

        for (Field field : tClass.getDeclaredFields()) {
            Arrays.stream(field.getDeclaredAnnotationsByType(TableField.class)).filter(tableField -> StringUtils.isBlank(tableField.name()) || StringUtils.equals(tableField.name(), entityTable.name())).findAny().ifPresent(tableField -> {
                EntityTableField entityTableField = new EntityTableField();

                entityTableField.setField(field);
                entityTableField.setTitle(tableField.value());
                entityTableField.setDescription(tableField.description());
                entityTableField.setOrder(tableField.order());
                entityTableField.setFieldSuffix(tableField.fieldSuffix());
                entityTableField.setMaxCollectionCount(tableField.maxCollectionCount());
                entityTableField.setCollectionEntityIndex(tableField.collectionEntityIndex());
                entityTableField.setImage(tableField.image());
                entityTableField.setEnableUrl(tableField.enableUrl());

                if (StringUtils.isNotBlank(tableField.urlPrefix())) {
                    entityTableField.setUrlPrefix(tableField.urlPrefix());

                    if (!StringUtils.endsWith(entityTableField.getUrlPrefix(), "/")) {
                        entityTableField.setUrlPrefix(entityTableField.getUrlPrefix() + "/");
                    }
                } else {
                    entityTableField.setUrlPrefix(table.getAction());
                }

                if (StringUtils.isNotBlank(tableField.urlEntityIndex())) {
                    entityTableField.setUrlEntityIndex(tableField.urlEntityIndex());
                } else {
                    entityTableField.setUrlEntityIndex(entityTableField.getCollectionEntityIndex());
                }

                entityTableField.setUrlSuffix(tableField.urlSuffix());
                entityTableField.setUrlTarget(tableField.urlTarget());
                entityTableField.setAlignment(tableField.alignment());

                Arrays.stream(tClass.getDeclaredMethods())
                        .filter(method -> method.getReturnType().equals(TableData.class))
                        .filter(method -> {
                            for (TableFieldRender tableFieldRender : method.getAnnotationsByType(TableFieldRender.class)) {
                                if (StringUtils.equals(tableFieldRender.value(), entityTableField.getField().getName())) {
                                    return true;
                                }
                            }
                            return false;
                        }).findFirst().ifPresent(entityTableField::setRender);

                entityTableFields.add(entityTableField);
            });
        }

        entityTableFields.sort(Comparator.comparingInt(EntityTableField::getOrder));

        return entityTableFields;
    }

    private void renderTableHeader(com.qxcmp.web.view.modules.table.EntityTable table, List<EntityTableField> entityTableFields, HttpServletRequest request) {
        final TableHeader tableHeader = new TableHeader();
        final TableRow tableActionRow = new TableRow();
        final TableHead tableActionHead = new TableHead();

        tableActionRow.addCell(tableActionHead);

        int colSpan = getColSpan(table, entityTableFields);
        tableActionHead.setColSpan(colSpan);

        if (!table.isDisableFilter()) {
            renderTableFilter(table, tableHeader, entityTableFields, request);
        }

        if (!table.getTableActions().isEmpty()) {
            renderTableActionHeader(table, tableActionHead);
        }

        if (!table.getBatchActions().isEmpty()) {
            renderTableBatchActionHeader(table, tableActionHead);
        }

        if (!tableActionHead.getComponents().isEmpty()) {
            tableHeader.addRow(tableActionRow);
        }

        renderTableTitleHeader(table, entityTableFields, tableHeader);

        table.setHeader(tableHeader);
    }

    private void renderTableFilter(com.qxcmp.web.view.modules.table.EntityTable table, TableHeader tableHeader, List<EntityTableField> entityTableFields, HttpServletRequest request) {
        final TableRow tableRow = new TableRow();
        final TableHead tableHead = new TableHead();

        int colSpan = getColSpan(table, entityTableFields);
        tableHead.setColSpan(colSpan);

        Selection selection = new Selection();
        SelectionMenu menu = new SelectionMenu();
        selection.setMenu(menu);
        selection.setName("field");

        entityTableFields.forEach(entityTableField -> {
            TextItem textItem = new TextItem(entityTableField.getTitle());
            textItem.setValue(entityTableField.getField().getName());

            if (StringUtils.equals(entityTableField.getField().getName(), request.getParameter("field"))) {
                selection.setText(entityTableField.getTitle());
            }

            menu.addItem(textItem);
        });

        tableHead.addComponent(new EntityTableFilter(selection, new Input(StringUtils.isNotBlank(request.getParameter("search")) ? request.getParameter("search") : "输入要查找的内容", "search"), new Button(StringUtils.isNotBlank(request.getParameter("search")) ? "清空" : "搜索").setBasic()));
        tableRow.addCell(tableHead);
        tableHeader.addRow(tableRow);
    }

    private void renderTableActionHeader(com.qxcmp.web.view.modules.table.EntityTable table, TableHead tableHead) {
        final Buttons buttons = new Buttons();

        buttons.setSize(Size.MINI);

        table.getTableActions().forEach(entityTableAction -> {
            buttons.addButton(convertActionToButton(entityTableAction));
        });

        tableHead.addComponent(buttons);
    }

    private void renderTableBatchActionHeader(com.qxcmp.web.view.modules.table.EntityTable table, TableHead tableHead) {
        final Buttons buttons = new Buttons();

        buttons.setSize(Size.MINI);

        table.getBatchActions().forEach(entityTableBatchAction -> {
            buttons.addButton(convertActionToButton(entityTableBatchAction));
        });

        tableHead.addComponent(buttons);
    }

    private void renderTableTitleHeader(com.qxcmp.web.view.modules.table.EntityTable table, List<EntityTableField> entityTableFields, TableHeader tableHeader) {
        final TableRow tableRow = new TableRow();

        if (table.isMultiple()) {
            tableRow.addCell(new TableHeadCheckbox("root").setAlignment(Alignment.CENTER));
        }

        entityTableFields.forEach(entityTableField -> {
            final TableHead tableHead = new TableHead();
            tableHead.setContent(entityTableField.getTitle());
            tableHead.setAlignment(entityTableField.getAlignment());
            tableRow.addCell(tableHead);
        });

        if (!table.getRowActions().isEmpty()) {
            final TableHead tableHead = new TableHead();
            tableHead.setContent("操作");
            tableHead.setAlignment(Alignment.CENTER);
            tableRow.addCell(tableHead);
        }

        tableHeader.addRow(tableRow);
    }

    private <T> void renderTableBody(com.qxcmp.web.view.modules.table.EntityTable table, List<EntityTableField> entityTableFields, Class<T> tClass, Page<T> tPage) {
        final TableBody tableBody = new TableBody();

        if (tPage.getContent().isEmpty()) {
            final TableRow tableRow = new TableRow();
            tableRow.addCell(new TableData("暂无内容").setColSpan(getColSpan(table, entityTableFields)).setAlignment(Alignment.CENTER));
            tableBody.addRow(tableRow);
        }

        tPage.getContent().forEach(t -> {
            final TableRow tableRow = new TableRow();

            if (table.isMultiple()) {
                final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(t);
                tableRow.addCell(new TableDataCheckbox(beanWrapper.getPropertyValue(table.getEntityIndex()).toString()).setAlignment(Alignment.CENTER));
            }

            entityTableFields.forEach(entityTableField -> tableRow.addCell(renderTableCell(entityTableField, t)));

            if (!table.getRowActions().isEmpty()) {
                final TableData tableData = new TableData();
                tableData.setAlignment(Alignment.CENTER);
                renderTableActionCell(table, tableData, table.getRowActions(), tClass, t);
                tableRow.addCell(tableData);
            }

            tableBody.addRow(tableRow);
        });

        table.setBody(tableBody);
    }

    @SuppressWarnings("unchecked")
    private <T> TableData renderTableCell(EntityTableField entityTableField, T t) {
        TableData tableData = new TableData();

        /*
         * 判断是否由自定义渲染器渲染
         * */
        if (Objects.nonNull(entityTableField.getRender())) {
            try {
                tableData = (TableData) entityTableField.getRender().invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            final BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
            final TypeDescriptor typeDescriptor = beanWrapper.getPropertyTypeDescriptor(entityTableField.getField().getName());

            final Object value = beanWrapper.getPropertyValue(entityTableField.getField().getName() + entityTableField.getFieldSuffix());

            /*
             * 判断是否为集合类型
             * */
            if (typeDescriptor.isCollection()) {
                String collectionEntityIndex = entityTableField.getCollectionEntityIndex();
                List list = (List) ((Collection) value).stream().limit(entityTableField.getMaxCollectionCount()).collect(Collectors.toList());
                List<com.qxcmp.web.view.Component> components = Lists.newArrayList();
                list.forEach(item -> {

                    final BeanWrapper itemWrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);

                    String labelText;

                    if (StringUtils.isNotBlank(collectionEntityIndex)) {
                        Object itemValue = itemWrapper.getPropertyValue(collectionEntityIndex);
                        labelText = Objects.nonNull(itemValue) ? itemValue.toString() : "";
                    } else {
                        labelText = item.toString();
                    }

                    if (entityTableField.isEnableUrl()) {
                        String url = entityTableField.getUrlPrefix() + itemWrapper.getPropertyValue(entityTableField.getUrlEntityIndex());

                        if (StringUtils.isNotBlank(entityTableField.getUrlSuffix())) {
                            url += "/" + entityTableField.getUrlSuffix();
                        }

                        components.add(new BasicLabel(labelText).setUrl(url));
                    } else {
                        components.add(new Label(labelText));
                    }
                });
                tableData.addComponents(components);
            } else {
                if (entityTableField.isImage()) {
                    tableData.setCollapsing().addComponent(new Avatar(Objects.nonNull(value) ? value.toString() : "").setCentered());
                } else if (entityTableField.isEnableUrl()) {
                    String textValue = getTableCellTextValue(typeDescriptor, value);

                    String url = entityTableField.getUrlPrefix() +
                            beanWrapper.getPropertyValue(StringUtils.isNotBlank(entityTableField.getUrlEntityIndex()) ? entityTableField.getUrlEntityIndex() : entityTableField.getField().getName()) +
                            entityTableField.getUrlSuffix();

                    tableData.addComponent(new Anchor(textValue, url, entityTableField.getUrlTarget().toString()));

                } else {

                    String textValue = getTableCellTextValue(typeDescriptor, value);

                    tableData.setContent(StringUtils.isNotBlank(textValue) ? textValue : "");
                }
            }
        }

        tableData.setAlignment(entityTableField.getAlignment());

        return tableData;
    }

    private String getTableCellTextValue(TypeDescriptor typeDescriptor, Object value) {
        String textValue;

        try {
            final TypeDescriptor strTypeDescriptor = TypeDescriptor.valueOf(String.class);
            textValue = (String) conversionService.convert(value, typeDescriptor, strTypeDescriptor);
        } catch (Exception e) {
            textValue = value.toString();
        }
        return textValue;
    }

    private <T> void renderTableActionCell(com.qxcmp.web.view.modules.table.EntityTable table, TableData tableData, List<EntityTableRowAction> rowActions, Class<T> tClass, T t) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(t);
        final Buttons buttons = new Buttons();

        buttons.setSize(Size.MINI);

        rowActions.forEach(entityTableRowAction -> {

            Method checkMethod = Arrays.stream(tClass.getDeclaredMethods()).filter(method -> {
                if (method.getReturnType().equals(boolean.class)) {
                    for (RowActionCheck rowActionCheck : method.getAnnotationsByType(RowActionCheck.class)) {
                        if (StringUtils.equals(rowActionCheck.value(), entityTableRowAction.getTitle())) {
                            return true;
                        }
                    }
                }
                return false;
            }).findFirst().orElse(null);

            boolean canPerform = true;

            if (Objects.nonNull(checkMethod)) {
                try {
                    canPerform = (boolean) checkMethod.invoke(t);
                } catch (Exception ignored) {

                }
            }

            if (canPerform) {
                AbstractButton button = convertActionToButton(entityTableRowAction);
                button.getAnchor().setHref(table.getAction() + beanWrapper.getPropertyValue(table.getEntityIndex()) + "/" + entityTableRowAction.getAction());
                button.getAnchor().setTarget(entityTableRowAction.getTarget().toString());
                buttons.addButton(button);
            }
        });

        tableData.addComponent(buttons);
    }

    private <T> void renderTableFooter(com.qxcmp.web.view.modules.table.EntityTable table, List<EntityTableField> entityTableFields, Page<T> tPage, HttpServletRequest request) {
        final TableFooter tableFooter = new TableFooter();
        final TableRow tableRow = new TableRow();
        final TableHead tableHead = new TableHead();

        int colSpan = getColSpan(table, entityTableFields);

        String queryString = "";

        if (StringUtils.isNotBlank(request.getParameter("field")) && StringUtils.isNotBlank(request.getParameter("search"))) {
            queryString += String.format("&field=%s&search=%s", request.getParameter("field"), request.getParameter("search"));
        }

        tableHead.setColSpan(colSpan);
        tableHead.setAlignment(Alignment.CENTER);
        tableHead.addComponent(new Pagination("", queryString, tPage.getNumber() + 1, (int) tPage.getTotalElements(), tPage.getSize()).setShowSizeChanger().setShowQuickJumper().setShowTotal());

        tableRow.addCell(tableHead);
        tableFooter.addRow(tableRow);
        table.setFooter(tableFooter);
    }

    private int getColSpan(com.qxcmp.web.view.modules.table.EntityTable table, List<EntityTableField> entityTableFields) {
        int colSpan = entityTableFields.size();

        if (table.isMultiple()) {
            ++colSpan;
        }

        if (!table.getRowActions().isEmpty()) {
            ++colSpan;
        }
        return colSpan;
    }

    private AbstractButton convertActionToButton(AbstractEntityTableAction tableAction) {
        AbstractTableActionButton button;

        if (tableAction instanceof EntityTableAction) {
            button = new EntityTableActionButton(tableAction.getTitle(), tableAction.getAction(), tableAction.getTarget());
        } else if (tableAction instanceof EntityTableBatchAction) {
            button = new EntityTableBatchActionButton(tableAction.getTitle(), tableAction.getAction(), tableAction.getTarget());
        } else {
            button = new EntityTableRowActionButton(tableAction.getTitle(), tableAction.getAction(), tableAction.getTarget());
        }

        button.setMethod(tableAction.getMethod());

        button.setShowConfirm(tableAction.isShowConfirmDialog());
        button.setConfirmDialogTitle(tableAction.getConfirmDialogTitle());
        button.setConfirmDialogDescription(tableAction.getConfirmDialogDescription());

        button.setColor(tableAction.getColor());
        button.setPrimary(tableAction.isPrimary());
        button.setSecondary(tableAction.isSecondary());
        button.setInverted(tableAction.isInverted());
        button.setBasic(tableAction.isBasic());

        return button;
    }
}
