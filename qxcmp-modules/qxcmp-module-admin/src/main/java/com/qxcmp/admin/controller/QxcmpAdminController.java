package com.qxcmp.admin.controller;

import com.qxcmp.admin.page.AdminOverviewPage;
import com.qxcmp.audit.Action;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.entity.EntityService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.views.Overview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 后台页面路由基类
 *
 * @author Aaric
 */
public abstract class QxcmpAdminController extends QxcmpController {

    /**
     * 使用表单创建一个实体对象
     * <p>
     * 注意：
     * <ol>
     * <li>新建实体的url必须以 {@code /new}结尾</li>
     * </ol>
     *
     * @param entityService 实体服务
     * @param form          表单
     *
     * @return 操作结果页面
     */
    protected <T, ID extends Serializable> ModelAndView createEntity(EntityService<T, ID> entityService, Object form) {
        return execute(getFormSubmitActionTitle(form), context -> {
            try {
                entityService.create(() -> {
                    T next = entityService.next();
                    entityService.mergeToEntity(form, next);
                    return next;
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> {
            overview.addLink("返回", request.getRequestURL().toString().replaceAll("/new", ""));
            overview.addLink("继续新建", "");
        });
    }

    @Override
    protected ModelAndView execute(String title, Action action, BiConsumer<Map<String, Object>, Overview> context) {
        Overview overview = getExecuteOverview(title, action, context);
        return page(AdminOverviewPage.class, overview);
    }

    /**
     * 获取表单提交操作标题
     *
     * @param form 表单
     *
     * @return 表单标题
     */
    private String getFormSubmitActionTitle(Object form) {
        Form annotation = form.getClass().getAnnotation(Form.class);
        if (Objects.nonNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        }
        return request.getRequestURL().toString();
    }
}
