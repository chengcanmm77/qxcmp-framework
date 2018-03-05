package com.qxcmp.web.view.modules.table.dictionary;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.label.Label;
import com.qxcmp.web.view.modules.table.AbstractTableCell;
import com.qxcmp.web.view.modules.table.TableData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 集合单元格
 * <p>
 * 把集合元素渲染为标签单元格
 *
 * @author Aaric
 */
@Getter
@Setter
public class CollectionValueCell extends BaseDictionaryValueCell<Collection<?>> {

    /**
     * 集合元素索引
     * <p>
     * 如果索引为空则去元素 toString() 方法
     */
    private String entityIndex;

    public CollectionValueCell(Collection<?> object) {
        super(object);
    }

    public CollectionValueCell(Collection<?> object, String entityIndex) {
        super(object);
        this.entityIndex = entityIndex;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AbstractTableCell render() {
        final TableData tableData = new TableData();

        List<Component> components = Lists.newArrayList();

        Lists.newArrayList(object).forEach(item -> {
            final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(item);

            if (StringUtils.isNotBlank(entityIndex)) {
                Object itemValue = beanWrapper.getPropertyValue(entityIndex);
                components.add(new Label(Objects.nonNull(itemValue) ? itemValue.toString() : ""));
            } else {
                components.add(new Label(item.toString()));
            }
        });

        tableData.addComponents(components);
        return tableData;
    }
}
