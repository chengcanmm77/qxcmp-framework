package com.qxcmp.web.view.support.utils;

import com.qxcmp.core.entity.EntityService;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.modules.form.AbstractForm;
import com.qxcmp.web.view.modules.pagination.Pagination;
import com.qxcmp.web.view.modules.table.AbstractTable;
import com.qxcmp.web.view.modules.table.EntityTable;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 视图生成工具
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class ViewHelper {

    private final FormHelper formHelper;
    private final TableHelper tableHelper;
    private final OverviewHelper overviewHelper;

    public AbstractForm nextForm(Object form, BindingResult bindingResult) {
        AbstractForm abstractForm = formHelper.convert(form);
        if (Objects.nonNull(bindingResult) && bindingResult.hasErrors()) {
            abstractForm.setErrorMessage(formHelper.convertToErrorMessage(bindingResult, form));
        }
        return abstractForm;
    }

    public AbstractForm nextForm(Object form) {
        return nextForm(form, null);
    }

    public AbstractTable nextTable(Consumer<Map<Object, Object>> consumer) {
        return tableHelper.convert(consumer);
    }

    public AbstractTable nextTable(Map<Object, Object> objectObjectMap) {
        return tableHelper.convert(objectObjectMap);
    }

    public EntityTable nextEntityTable(Pageable pageable, EntityService entityService, HttpServletRequest request) {
        return nextEntityTable("", pageable, entityService, request);
    }

    public EntityTable nextEntityTable(String tableName, Pageable pageable, EntityService entityService, HttpServletRequest request) {
        return nextEntityTable(tableName, "", pageable, entityService, request);
    }

    @SuppressWarnings("unchecked")
    public EntityTable nextEntityTable(String tableName, String action, Pageable pageable, EntityService entityService, HttpServletRequest request) {
        Page page = entityService.findAll(pageable);
        return nextEntityTable(tableName, action, entityService.type(), page, request);
    }

    public <T> EntityTable nextEntityTable(Class<T> tClass, Page<T> tPage, HttpServletRequest request) {
        return nextEntityTable("", tClass, tPage, request);
    }

    public <T> EntityTable nextEntityTable(String tableName, Class<T> tClass, Page<T> tPage, HttpServletRequest request) {
        return nextEntityTable(tableName, "", tClass, tPage, request);
    }

    public <T> EntityTable nextEntityTable(String tableName, String action, Class<T> tClass, Page<T> tPage, HttpServletRequest request) {
        return tableHelper.convert(tableName, action, tClass, tPage, request);
    }

    public ErrorMessage nextFormErrorMessage(BindingResult bindingResult, Object form) {

        if (Objects.isNull(bindingResult)) {
            return null;
        }

        return formHelper.convertToErrorMessage(bindingResult, form);
    }

    public Overview nextOverview(String title) {
        return overviewHelper.nextOverview(title);
    }

    public Overview nextOverview(String title, String subTitle) {
        return overviewHelper.nextOverview(title, subTitle);
    }

    public Overview nextOverview(Icon icon, String title, String subTitle) {
        return overviewHelper.nextOverview(icon, title, subTitle);
    }

    public Overview nextOverview(String icon, String title, String subTitle) {
        return overviewHelper.nextOverview(new Icon(icon), title, subTitle);
    }

    public Overview nextInfoOverview(String title) {
        return nextInfoOverview(title, "");
    }

    public Overview nextInfoOverview(String title, String subTitle) {
        return nextOverview(new Icon("info circle"), title, subTitle);
    }

    public Overview nextSuccessOverview(String title) {
        return nextSuccessOverview(title, "");
    }

    public Overview nextSuccessOverview(String title, String subTitle) {
        return nextOverview(new Icon("check circle").setColor(Color.GREEN), title, subTitle);
    }

    public Overview nextWarningOverview(String title) {
        return nextWarningOverview(title, "");
    }

    public Overview nextWarningOverview(String title, String subTitle) {
        return nextOverview(new Icon("warning circle").setColor(Color.ORANGE), title, subTitle);
    }

    public Pagination nextPagination(Page<?> page) {
        return new Pagination("", page.getNumber() + 1, (int) page.getTotalElements(), page.getSize());
    }
}
