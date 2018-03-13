package com.qxcmp.admin;

import com.google.common.base.CaseFormat;
import com.qxcmp.admin.form.IgnoredSystemConfig;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.admin.page.AdminPageRevolver;
import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.core.support.ReflectionUtils;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.views.Overview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.qxcmp.config.QxcmpSystemConfigLoader.DEFAULT_VALUE_SUFFIX;

/**
 * 后台页面路由基类
 *
 * @author Aaric
 */
public abstract class QxcmpAdminController extends QxcmpController {

    private AdminPageRevolver adminPageRevolver;

    /**
     * 获取系统配置修改表单页面
     *
     * @param qClass        表单页面
     * @param form          表单
     * @param bindingResult 错误对象
     * @param tClass        系统配置所在类
     * @param <Q>           表单页面类型
     * @param <T>           系统配置所在类
     *
     * @return 系统配置修改表单页面
     */
    protected <Q extends AbstractQxcmpAdminFormPage, T> ModelAndView systemConfigPage(Class<Q> qClass, Object form, BindingResult bindingResult, Class<T> tClass) {
        List<Field> systemConfigFields = ReflectionUtils.getAllFields(tClass);
        BeanWrapperImpl formBean = new BeanWrapperImpl(form);
        ReflectionUtils.getAllFields(form.getClass()).stream().filter(field -> {
            try {
                field.setAccessible(true);
                return Objects.isNull(field.get(form));
            } catch (IllegalAccessException e) {
                return true;
            }
        }).forEach(field -> {
            String matchedValue = getMatchedSystemConfigValue(systemConfigFields, field);
            try {
                formBean.setPropertyValue(field.getName(), convertSystemConfigValue(matchedValue, field.getType()));
            } catch (Exception ignored) {

            }
        });
        return page(qClass, form, bindingResult);
    }

    /**
     * 更新系统配置并返回结果页面
     *
     * @param tClass 系统配置所在类
     * @param form   系统配置表单
     * @param <T>    系统配置所在类
     *
     * @return 更新结果页面
     */
    protected <T> ModelAndView updateSystemConfig(Class<T> tClass, Object form) {
        AtomicInteger counter = new AtomicInteger();
        List<Field> systemConfigFields = ReflectionUtils.getAllFields(tClass);
        BeanWrapperImpl formBean = new BeanWrapperImpl(form);
        ReflectionUtils.getAllFields(form.getClass()).stream()
                .filter(field -> Objects.isNull(field.getAnnotation(IgnoredSystemConfig.class)))
                .forEach(field -> {
                    try {
                        String matchFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
                        String systemConfigName = getMatchedField(systemConfigFields, matchFieldName).map(this::getSystemConfigName).orElse("");
                        String matchedValue = getMatchedSystemConfigValue(systemConfigFields, field);
                        Object currentValue = formBean.getPropertyValue(field.getName());
                        if (Objects.nonNull(currentValue) && !StringUtils.equals(matchedValue, currentValue.toString())) {
                            String currentFormValue = String.valueOf(formBean.getPropertyValue(field.getName()));
                            systemConfigService.update(systemConfigName, currentFormValue).ifPresent(systemConfig -> {
                                counter.incrementAndGet();
                                applicationContext.publishEvent(new SystemConfigChangeEvent(tClass.getName(), currentUser().orElse(null), systemConfigName, matchedValue, currentFormValue));
                            });
                        }
                    } catch (Exception ignored) {

                    }
                });
        return overviewPage(viewHelper.nextSuccessOverview("操作成功", counter.toString() + "项配置已修改").addLink("返回", request.getRequestURL().toString()));
    }

    /**
     * 获取后台错误页面
     *
     * @param errors 错误信息
     *
     * @return 后台错误页面
     */
    protected ModelAndView adminErrorPage(Map<String, Object> errors) {
        return page(adminPageRevolver.getAdminErrorPage(), errors);
    }

    @Override
    protected ModelAndView overviewPage(Overview overview) {
        return page(adminPageRevolver.getAdminOverviewPage(), overview);
    }

    @Autowired
    public void setAdminPageRevolver(AdminPageRevolver adminPageRevolver) {
        this.adminPageRevolver = adminPageRevolver;
    }

    /**
     * 获取匹配的系统配置值
     * <p>
     * 1. 查询与要匹配字段转换为大写后的字段，如果不存在返回空
     * 2. 如果存在则查询该字段对应的系统配置
     * 3. 如果不存在则返回该值的默认值
     * 4. 如果还是不存在返回空
     *
     * @param systemConfigFields 要查询的字段
     * @param formField          要匹配的字段
     *
     * @return 匹配到的值
     */
    private String getMatchedSystemConfigValue(List<Field> systemConfigFields, Field formField) {
        /*
         * 与表单字段对应的查询字段
         * */
        String matchFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, formField.getName());
        return getMatchedField(systemConfigFields, matchFieldName).map(matchedField -> {
            /*
             * 系统配置主键
             * */
            String systemConfigName = getSystemConfigName(matchedField);
            return systemConfigService.getString(systemConfigName).orElseGet(() -> {
                String defaultValueMatchFieldName = matchFieldName + DEFAULT_VALUE_SUFFIX;
                return getMatchedField(systemConfigFields, defaultValueMatchFieldName).map(field -> {
                    try {
                        return field.get(null).toString();
                    } catch (Exception e) {
                        return "";
                    }
                }).orElse("");
            });
        }).orElse("");
    }

    /**
     * 获取匹配的字段
     *
     * @param systemConfigFields 查询的字段
     * @param matchFieldName     要匹配的字段名
     *
     * @return 匹配的字段
     */
    private Optional<Field> getMatchedField(List<Field> systemConfigFields, String matchFieldName) {
        return systemConfigFields.stream().filter(field -> StringUtils.equals(matchFieldName, field.getName())).findAny();
    }

    /**
     * 获取系统配置主键
     *
     * @param matchedField 匹配的字段
     *
     * @return 系统配置主键
     */
    private String getSystemConfigName(Field matchedField) {
        String systemConfigName = "";
        try {
            matchedField.setAccessible(true);
            systemConfigName = matchedField.get(null).toString();
        } catch (Exception ignored) {

        }
        return systemConfigName;
    }

    /**
     * 转换系统配置值到匹配类型
     *
     * @param value 系统配置主键
     * @param type  表单字段类型
     *
     * @return 匹配类型
     */
    private Object convertSystemConfigValue(String value, Class<?> type) {

        if (StringUtils.equals(type.getName(), Boolean.class.getName())) {
            return Boolean.parseBoolean(value);
        }

        if (StringUtils.equals(type.getName(), Integer.class.getName())) {
            return Integer.parseInt(value);
        }

        if (StringUtils.equals(type.getName(), Short.class.getName())) {
            return Short.parseShort(value);
        }

        if (StringUtils.equals(type.getName(), Long.class.getName())) {
            return Long.parseLong(value);
        }

        if (StringUtils.equals(type.getName(), Float.class.getName())) {
            return Float.parseFloat(value);
        }

        if (StringUtils.equals(type.getName(), Double.class.getName())) {
            return Double.parseDouble(value);
        }

        return value;
    }
}
