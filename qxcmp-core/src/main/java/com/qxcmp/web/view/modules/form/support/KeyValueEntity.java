package com.qxcmp.web.view.modules.form.support;

import com.qxcmp.web.view.annotation.form.DynamicField;
import lombok.Data;

/**
 * 用于动态列表字段和哈希表的转换
 *
 * @author Aaric
 * @see DynamicField
 */
@Data
public class KeyValueEntity {

    private String key;

    private String value;
}
