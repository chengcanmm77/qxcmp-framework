package com.qxcmp.admin.page;

import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

/**
 * 后台表单页面
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractQxcmpAdminFormPage extends AbstractQxcmpAdminPage {

    private final Object form;
    private final BindingResult bindingResult;

    @Override
    public void render() {
        preRender();
        addComponent(new TextContainer().addComponent(new Segment().addComponent(viewHelper.nextForm(form, bindingResult))));
        postRender();
    }

    /**
     * 渲染表单前的钩子函数
     */
    protected abstract void preRender();

    /**
     * 渲染表单后的钩子函数
     */
    protected abstract void postRender();
}
