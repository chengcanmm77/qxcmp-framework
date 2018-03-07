package com.qxcmp.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.qxcmp.audit.Action;
import com.qxcmp.audit.ActionExecutor;
import com.qxcmp.audit.AuditLog;
import com.qxcmp.config.SiteService;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.config.UserConfigService;
import com.qxcmp.core.entity.EntityService;
import com.qxcmp.exception.CaptchaExpiredException;
import com.qxcmp.exception.CaptchaIncorrectException;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.util.Captcha;
import com.qxcmp.util.CaptchaService;
import com.qxcmp.util.IpAddressResolver;
import com.qxcmp.util.QxcmpPageResolver;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.page.AbstractLegacyPage;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.modules.form.AbstractForm;
import com.qxcmp.web.view.modules.table.EntityTable;
import com.qxcmp.web.view.modules.table.Table;
import com.qxcmp.web.view.page.QxcmpPage;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.utils.TableHelper;
import com.qxcmp.web.view.support.utils.ViewHelper;
import com.qxcmp.web.view.views.Overview;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_FILE_UPLOAD_TEMP_FOLDER;

/**
 * 平台页面路由基类
 * <p>
 * 负责解析页面类型并提供视图渲染和表单提交等一些基本支持
 *
 * @author Aaric
 */
public abstract class QxcmpController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ApplicationContext applicationContext;
    protected SiteService siteService;
    protected UserService userService;
    protected UserConfigService userConfigService;
    protected SystemConfigService systemConfigService;
    protected MessageService messageService;
    protected ViewHelper viewHelper;
    protected QxcmpPageResolver pageResolver;

    private TableHelper tableHelper;
    private CaptchaService captchaService;
    private ActionExecutor actionExecutor;
    private IpAddressResolver ipAddressResolver;
    private DeviceResolver deviceResolver;

    /**
     * 获取一个页面
     *
     * @param tClass 页面类型
     * @param models 页面数据
     * @param <T>    页面类型
     *
     * @return 渲染后的页面
     */
    protected <T extends QxcmpPage> ModelAndView qxcmpPage(Class<T> tClass, Object... models) {

        T t = applicationContext.getBean(tClass, models);

        Device device = deviceResolver.resolveDevice(request);

        if (device.isMobile()) {
            t.renderToMobile();
        } else if (device.isTablet()) {
            t.renderToTablet();
        } else if (device.isNormal()) {
            t.renderToNormal();
        } else {
            t.render();
        }

        return t.build();
    }


    /**
     * 根据请求获取一个页面
     *
     * @return 由页面解析器解析出来的页面
     *
     * @see QxcmpPageResolver
     */
    protected AbstractLegacyPage page() {
        return pageResolver.resolve(request, response);
    }

    /**
     * 根据请求获取一个页面并设置概览视图
     *
     * @param overview 概览组件
     *
     * @return 概览视图页面
     *
     * @see Overview
     */
    protected AbstractLegacyPage page(Overview overview) {
        return page().addComponent(new Grid().setTextContainer().setAlignment(Alignment.CENTER).setVerticallyPadded().addItem(new Col().addComponent(overview)));
    }

    /**
     * 获取一个重定向页面
     *
     * @param url 重定向链接
     *
     * @return 重定向页面
     */
    protected ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }

    protected AbstractForm convertToForm(Object object) {
        return viewHelper.nextForm(object);
    }

    protected ErrorMessage convertToErrorMessage(BindingResult bindingResult, Object object) {
        return viewHelper.nextFormErrorMessage(bindingResult, object);
    }

    protected EntityTable convertToTable(Pageable pageable, EntityService entityService) {
        return convertToTable("", pageable, entityService);
    }

    protected EntityTable convertToTable(String tableName, Pageable pageable, EntityService entityService) {
        return convertToTable(tableName, "", pageable, entityService);
    }

    @SuppressWarnings("unchecked")
    protected EntityTable convertToTable(String tableName, String action, Pageable pageable, EntityService entityService) {

        Page page;

        if (StringUtils.isNotBlank(request.getParameter("field")) && StringUtils.isNotBlank(request.getParameter("search"))) {
            String searchField = request.getParameter("field");
            String searchContent = request.getParameter("search");

            List<Field> fields = Lists.newArrayList();

            for (Field field : entityService.type().getDeclaredFields()) {
                if (StringUtils.equals(field.getName(), searchField)) {
                    fields.add(field);
                    break;
                }
            }

            page = entityService.findAll(pageable);
        } else {
            page = entityService.findAll(pageable);
        }

        return convertToTable(tableName, action, entityService.type(), page);
    }

    protected <T> EntityTable convertToTable(Class<T> tClass, Page<T> tPage) {
        return convertToTable("", tClass, tPage);
    }

    protected <T> EntityTable convertToTable(String tableName, Class<T> tClass, Page<T> tPage) {
        return convertToTable(tableName, "", tClass, tPage);
    }

    protected <T> EntityTable convertToTable(String tableName, String action, Class<T> tClass, Page<T> tPage) {
        return tableHelper.convert(tableName, action, tClass, tPage, request);
    }

    protected Table convertToTable(Map<Object, Object> dictionary) {
        return tableHelper.convert(dictionary);
    }

    protected Table convertToTable(Consumer<Map<Object, Object>> consumer) {
        Map<Object, Object> dictionary = Maps.newLinkedHashMap();
        consumer.accept(dictionary);
        return convertToTable(dictionary);
    }

    /**
     * 获取当前请求对应的认证用户
     *
     * @return 当前认证用户
     */
    protected Optional<User> currentUser() {
        return Optional.ofNullable(userService.currentUser());
    }

    /**
     * 刷新当前用户实体
     * <p>
     * 如果当前用户已经登录，则重新读取用户数据
     */
    protected void refreshUser() {
        currentUser().ifPresent(currentUser -> userService.findOne(currentUser.getId()).ifPresent(user -> {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }));
    }

    /**
     * 验证验证码是否有效，如果无效将错误信息放入 {@code BindingResult} 中
     *
     * @param captcha       用户输入的验证码
     * @param bindingResult 错误绑定
     */
    protected void verifyCaptcha(String captcha, BindingResult bindingResult) {
        if (Objects.isNull(request.getSession().getAttribute(CaptchaService.CAPTCHA_SESSION_ATTR))) {
            bindingResult.rejectValue("captcha", "Captcha.null");
        } else {
            try {
                Captcha c = (Captcha) request.getSession().getAttribute(CaptchaService.CAPTCHA_SESSION_ATTR);
                captchaService.verify(c, captcha);
            } catch (CaptchaExpiredException e) {
                bindingResult.rejectValue("captcha", "Captcha.expired");
            } catch (CaptchaIncorrectException e) {
                bindingResult.rejectValue("captcha", "Captcha.incorrect");
            }
        }
    }

    /**
     * 获取请求的IP地址
     *
     * @return 请求IP地址
     */
    protected String getRequestAddress() {
        return ipAddressResolver.resolve(request);
    }

    protected ModelAndView submitForm(Object form, Action action) {
        return submitForm(form, action, (stringObjectMap, overview) -> {
        });
    }

    protected ModelAndView submitForm(Object form, Action action, BiConsumer<Map<String, Object>, Overview> biConsumer) {
        return submitForm("", form, action, biConsumer);
    }

    /**
     * 提交一个表单并执行相应操作
     * <p>
     * 该操作会被记录到审计日志中
     *
     * @param title      操作名称
     * @param form       要提交的表单
     * @param action     要执行的操作
     * @param biConsumer 返回的结果页面
     *
     * @return 提交后的页面
     */
    protected ModelAndView submitForm(String title, Object form, Action action, BiConsumer<Map<String, Object>, Overview> biConsumer) {
        Form annotation = form.getClass().getAnnotation(Form.class);

        if (Objects.nonNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
            title = annotation.value();
        }

        if (StringUtils.isBlank(title)) {
            title = request.getRequestURL().toString();
        }

        AuditLog auditLog = actionExecutor.execute(title, request.getRequestURL().toString(), getRequestContent(request), currentUser().orElse(null), action);
        Overview overview = null;
        switch (auditLog.getStatus()) {
            case SUCCESS:
                overview = new Overview(new IconHeader(auditLog.getTitle(), new Icon("info circle")).setSubTitle("操作成功"));
                break;
            case FAILURE:
                overview = new Overview(new IconHeader(auditLog.getTitle(), new Icon("warning circle").setColor(Color.RED)).setSubTitle("操作失败")).addComponent(new P(auditLog.getComments()));
                break;
        }


        biConsumer.accept(auditLog.getActionContext(), overview);

        if (overview.getLinks().isEmpty()) {
            overview.addLink("返回", request.getRequestURL().toString());
        }

        return page(overview).build();
    }

    /**
     * 执行一个操作并记录到审计日志中
     *
     * @param title  操作名称
     * @param action 要执行的操作
     *
     * @return 操作结果实体
     */
    protected RestfulResponse audit(String title, Action action) {
        AuditLog auditLog = actionExecutor.execute(title, request.getRequestURL().toString(), getRequestContent(request), currentUser().orElse(null), action);

        switch (auditLog.getStatus()) {
            case SUCCESS:
                return new RestfulResponse(HttpStatus.OK.value(), "", auditLog.getTitle(), auditLog.getComments());
            case FAILURE:
                return new RestfulResponse(HttpStatus.BAD_GATEWAY.value(), "", auditLog.getTitle(), auditLog.getComments());
        }

        return new RestfulResponse(HttpStatus.NOT_ACCEPTABLE.value(), "", auditLog.getTitle(), auditLog.getComments());
    }

    /**
     * 获取上传后的文件
     *
     * @param keys 临时文件标识
     *
     * @return 文件列表
     */
    protected List<File> getUploadFiles(List<String> keys) {
        List<File> files = Lists.newArrayList();

        keys.forEach(s -> files.addAll(FileUtils.listFiles(new File(QXCMP_FILE_UPLOAD_TEMP_FOLDER + s), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)));

        return files;
    }

    /**
     * 获取单个上传后的文件
     *
     * @param key 临时文件标识
     *
     * @return 单个文件
     */
    protected File getUploadFile(String key) {
        return FileUtils.listFiles(new File(QXCMP_FILE_UPLOAD_TEMP_FOLDER + key), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream().findAny().orElse(null);
    }

    private String getRequestContent(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("get")) {
            return request.getQueryString();
        } else if (request.getMethod().equalsIgnoreCase("post")) {
            return new Gson().toJson(request.getParameterMap());
        } else {
            return "Unknown request method: " + request.toString();
        }
    }

    @Autowired
    public void setActionExecutor(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserConfigService(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @Autowired
    public void setSystemConfigService(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setViewHelper(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    @Autowired
    public void setTableHelper(TableHelper tableHelper) {
        this.tableHelper = tableHelper;
    }

    @Autowired
    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Autowired
    public void setPageResolver(QxcmpPageResolver pageResolver) {
        this.pageResolver = pageResolver;
    }

    @Autowired
    public void setIpAddressResolver(IpAddressResolver ipAddressResolver) {
        this.ipAddressResolver = ipAddressResolver;
    }

    @Autowired
    public void setDeviceResolver(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }
}
