package com.qxcmp.admin.page;

import com.qxcmp.core.entity.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

/**
 * 后台实体表单提交页面
 * <p>
 * 负责处理实体表单的提交并返回提交结果
 * <p>
 * 可以处理实体的增改操作
 *
 * @param <T> 表单类型
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractQxcmpAdminEntityFormSubmitPage<T> extends AbstractQxcmpAdminPage {

    protected final EntityService entityService;
    protected final T form;
    protected final BindingResult bindingResult;
}
