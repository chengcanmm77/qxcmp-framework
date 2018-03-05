package com.qxcmp.web.view.modules.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.modules.form.field.AbstractFormField;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 表单字段组
 * <p>
 * 一个表单根据复杂程度可以有一个到多个字段组
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractFormSection {

    /**
     * 字段组名称
     * <p>
     * 当表单只有一个字段组的时候将不会显示名称
     */
    private String name;

    private List<AbstractFormField> fields = Lists.newArrayList();

    public AbstractFormSection addField(AbstractFormField field) {
        fields.add(field);
        return this;
    }

    public AbstractFormSection addFields(Collection<? extends AbstractFormField> fields) {
        this.fields.addAll(fields);
        return this;
    }

    public AbstractFormSection() {
    }

    public AbstractFormSection(String name) {
        this.name = name;
    }

    public AbstractFormSection setName(String name) {
        this.name = name;
        return this;
    }

}
