package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 动态字段
 * <p>
 * 该字段类型为列表，可以动态增减列表项目
 *
 * @author Aaric
 */
@Getter
@Setter
public class DynamicField extends AbstractFormField {

    /**
     * 最大项目数量
     */
    private int maxCount;

    /**
     * 项目表格标题
     * <p>
     * 需要与项目字段对齐
     */
    private List<String> itemHeaders;

    /**
     * 项目字段
     * <p>
     * 需要编辑的项目字段
     */
    private List<String> itemFields;

    /**
     * 是否为原始类型
     * <p>
     * 如果为原始类型，则不用填写 itemFields
     */
    private boolean rawType;

    @Override
    public String getFragmentName() {
        return "field-dynamic";
    }
}
