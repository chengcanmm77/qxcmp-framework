package com.qxcmp.admin.page;

import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

/**
 * 后台表单页面
 *
 * @param <T> 表单对象类型
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractQxcmpAdminFormPage<T> extends AbstractQxcmpAdminPage {

    protected final T form;
    protected final BindingResult bindingResult;

    @Override
    public void render() {
        preRender();
        addComponent(new TextContainer().addComponent(new Segment().addComponent(viewHelper.nextForm(form, bindingResult))));
        postRender();
    }

    /**
     * 渲染表单前的钩子函数
     */
    protected void preRender() {

    }

    /**
     * 渲染表单后的钩子函数
     */
    protected void postRender() {

    }
}
