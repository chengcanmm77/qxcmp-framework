package com.qxcmp.web.view.support.utils;

import com.google.common.base.CaseFormat;
import com.qxcmp.core.support.ReflectionUtils;
import com.qxcmp.web.view.annotation.form.*;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.IconHeaderItem;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.modules.form.AbstractForm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@RequiredArgsConstructor
public class FormHelper {

    public static final String SELF_ACTION = "$SELF";

    private static final String DEFAULT_SELECTION_ITEM_PREFIX = "selection_items_";

    private final ApplicationContext applicationContext;

    /**
     * 将表单错误对象转换为错误消息组件
     *
     * @param bindingResult 错误对象
     * @param object        表单对象
     *
     * @return 错误消息组件
     */
    public ErrorMessage convertToErrorMessage(BindingResult bindingResult, Object object) {
        List errLists = new List();
        bindingResult.getAllErrors().forEach(objectError -> {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;

                String fieldName = fieldError.getField();

                for (Field field : ReflectionUtils.getAllFields(object.getClass())) {
                    if (field.getName().equals(fieldName)) {
                        for (Annotation annotation : field.getAnnotations()) {
                            for (Method method : annotation.getClass().getDeclaredMethods()) {
                                if (method.getName().equals("value")) {
                                    try {
                                        fieldName = method.invoke(annotation).toString();
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                }
                            }
                        }

                        break;
                    }
                }

                String errMessage = "";

                for (String code : fieldError.getCodes()) {
                    try {
                        String message = applicationContext.getMessage(code, null, null);
                        if (StringUtils.isNotBlank(message)) {
                            errMessage = message;
                            break;
                        }
                    } catch (Exception ignored) {

                    }
                }

                if (StringUtils.isBlank(errMessage)) {
                    errMessage = fieldError.getDefaultMessage();
                }


                errLists.addItem(new IconHeaderItem(new Icon("warning sign"), fieldName, errMessage));
            }
        });
        return (ErrorMessage) new ErrorMessage("你输入的信息有误，请检查后重试", errLists).setCloseable();
    }

    /**
     * 将一个对象转换为表单
     *
     * @param object 对象
     *
     * @return 转换后的表单
     */
    public AbstractForm convert(Object object) {

        Form annotation = object.getClass().getAnnotation(Form.class);
        checkNotNull(annotation, "No Form definition in " + object.getClass().getName());

        final AbstractForm form = createForm(object, annotation);

        configFormField(form, object);

        return form;
    }

    private AbstractForm createForm(Object object, Form annotation) {

        String formName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, object.getClass().getSimpleName()).replaceAll("-form", "").replaceAll("-", ".");
        String objectName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, object.getClass().getSimpleName());

        final AbstractForm form = new com.qxcmp.web.view.modules.form.Form();

        form.setName(annotation.name());
        form.setMethod(annotation.method());
        form.setEnctype(annotation.enctype());
        form.setDisableAutoComplete(annotation.disableAutoComplete());
        form.setTarget(annotation.target());
        form.setSize(annotation.size());
        form.setInverted(annotation.inverted());

        if (StringUtils.isBlank(annotation.objectName())) {
            form.setObject(objectName);
        } else {
            form.setObject(annotation.objectName());
        }

        if (StringUtils.isBlank(annotation.action())) {
            form.setAction("/" + formName.replaceAll("\\.", "/"));
        } else if (StringUtils.equals(annotation.action(), SELF_ACTION)) {
            form.setAction("");
        } else {
            form.setAction(annotation.action());
        }

        if (StringUtils.isNotBlank(annotation.submitText())) {
            form.setSubmitButton(annotation.submitText());
        }

        if (StringUtils.isNotBlank(annotation.submitIcon())) {
            form.setSubmitButton(StringUtils.isBlank(annotation.submitText()) ? "提交" : annotation.submitText(), annotation.submitIcon());
        }

        if (StringUtils.isNotBlank(annotation.value())) {
            form.setHeader(new PageHeader(HeaderType.H2, annotation.value()).setDividing());
        }

        return form;
    }

    private void configFormField(AbstractForm form, Object object) {
        for (Field field : ReflectionUtils.getAllFields(object.getClass())) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof HiddenField) {
                    addHiddenField(form, field, (HiddenField) annotation);
                } else if (annotation instanceof TextInputField) {
                    addTextInputField(form, field, (TextInputField) annotation);
                } else if (annotation instanceof PasswordField) {
                    addPasswordInputField(form, field, (PasswordField) annotation);
                } else if (annotation instanceof TextAreaField) {
                    addTextAreaField(form, field, (TextAreaField) annotation);
                } else if (annotation instanceof ImageCaptchaField) {
                    addImageCaptchaField(form, field, (ImageCaptchaField) annotation);
                } else if (annotation instanceof PhoneCaptchaField) {
                    addPhoneCaptchaField(form, field, (PhoneCaptchaField) annotation);
                } else if (annotation instanceof EmailField) {
                    addEmailField(form, field, (EmailField) annotation);
                } else if (annotation instanceof PhoneField) {
                    addPhoneField(form, field, (PhoneField) annotation);
                } else if (annotation instanceof AvatarField) {
                    addAvatarField(form, field, (AvatarField) annotation);
                } else if (annotation instanceof AlbumField) {
                    addAlbumField(form, field, (AlbumField) annotation);
                } else if (annotation instanceof BooleanField) {
                    addBooleanField(form, field, (BooleanField) annotation);
                } else if (annotation instanceof TextSelectionField) {
                    addTextSelectionField(form, field, (TextSelectionField) annotation);
                } else if (annotation instanceof NumberField) {
                    addNumberField(form, field, (NumberField) annotation);
                } else if (annotation instanceof DynamicField) {
                    addDynamicField(form, field, (DynamicField) annotation);
                } else if (annotation instanceof HtmlField) {
                    addHtmlField(form, field, (HtmlField) annotation);
                } else if (annotation instanceof DateTimeField) {
                    addDateTimeField(form, field, (DateTimeField) annotation);
                } else if (annotation instanceof FileUploadField) {
                    addFileUploadField(form, field, (FileUploadField) annotation);
                }
            }
        }
    }

    private void addHiddenField(AbstractForm form, Field field, HiddenField annotation) {
        final com.qxcmp.web.view.modules.form.field.HiddenField hiddenField = new com.qxcmp.web.view.modules.form.field.HiddenField();
        hiddenField.setName(field.getName());
        form.addItem(hiddenField, annotation.section());
    }

    private void addTextInputField(AbstractForm form, Field field, TextInputField annotation) {
        final com.qxcmp.web.view.modules.form.field.TextInputField textInputField = new com.qxcmp.web.view.modules.form.field.TextInputField();

        textInputField.setName(field.getName());
        textInputField.setLabel(annotation.value());
        textInputField.setTooltip(annotation.tooltip());
        textInputField.setRequired(annotation.required());
        textInputField.setDisableAutoComplete(annotation.disableAutoComplete());
        textInputField.setAutoFocus(annotation.autoFocus());
        textInputField.setReadOnly(annotation.readOnly());
        textInputField.setPlaceholder(annotation.placeholder());
        textInputField.setMaxLength(annotation.maxLength());

        form.addItem(textInputField, annotation.section());
    }

    private void addPasswordInputField(AbstractForm form, Field field, PasswordField annotation) {
        final com.qxcmp.web.view.modules.form.field.PasswordField passwordField = new com.qxcmp.web.view.modules.form.field.PasswordField();

        passwordField.setName(field.getName());
        passwordField.setLabel(annotation.value());
        passwordField.setTooltip(annotation.tooltip());
        passwordField.setRequired(annotation.required());
        passwordField.setDisableAutoComplete(annotation.disableAutoComplete());
        passwordField.setAutoFocus(annotation.autoFocus());
        passwordField.setReadOnly(annotation.readOnly());
        passwordField.setPlaceholder(annotation.placeholder());
        passwordField.setMaxLength(annotation.maxLength());

        form.addItem(passwordField, annotation.section());
    }

    private void addTextAreaField(AbstractForm form, Field field, TextAreaField annotation) {
        final com.qxcmp.web.view.modules.form.field.TextAreaField textAreaField = new com.qxcmp.web.view.modules.form.field.TextAreaField();

        textAreaField.setName(field.getName());
        textAreaField.setLabel(annotation.value());
        textAreaField.setTooltip(annotation.tooltip());
        textAreaField.setRequired(annotation.required());
        textAreaField.setDisableAutoComplete(annotation.disableAutoComplete());
        textAreaField.setAutoFocus(annotation.autoFocus());
        textAreaField.setReadOnly(annotation.readOnly());
        textAreaField.setPlaceholder(annotation.placeholder());
        textAreaField.setMaxLength(annotation.maxLength());
        textAreaField.setRows(annotation.rows());

        form.addItem(textAreaField, annotation.section());
    }

    private void addImageCaptchaField(AbstractForm form, Field field, ImageCaptchaField annotation) {
        final com.qxcmp.web.view.modules.form.field.ImageCaptchaField imageCaptchaField = new com.qxcmp.web.view.modules.form.field.ImageCaptchaField();

        imageCaptchaField.setName(field.getName());
        imageCaptchaField.setLabel(annotation.value());
        imageCaptchaField.setTooltip(annotation.tooltip());
        imageCaptchaField.setRequired(annotation.required());
        imageCaptchaField.setDisableAutoComplete(annotation.disableAutoComplete());
        imageCaptchaField.setAutoFocus(annotation.autoFocus());
        imageCaptchaField.setReadOnly(annotation.readOnly());
        imageCaptchaField.setPlaceholder(annotation.placeholder());
        imageCaptchaField.setCaptchaUrl(annotation.captchaUrl());

        form.addItem(imageCaptchaField, annotation.section());
    }

    private void addPhoneCaptchaField(AbstractForm form, Field field, PhoneCaptchaField annotation) {
        final com.qxcmp.web.view.modules.form.field.PhoneCaptchaField phoneCaptchaField = new com.qxcmp.web.view.modules.form.field.PhoneCaptchaField();

        phoneCaptchaField.setName(field.getName());
        phoneCaptchaField.setLabel(annotation.value());
        phoneCaptchaField.setTooltip(annotation.tooltip());
        phoneCaptchaField.setRequired(annotation.required());
        phoneCaptchaField.setDisableAutoComplete(annotation.disableAutoComplete());
        phoneCaptchaField.setAutoFocus(annotation.autoFocus());
        phoneCaptchaField.setReadOnly(annotation.readOnly());
        phoneCaptchaField.setPlaceholder(annotation.placeholder());
        phoneCaptchaField.setCaptchaUrl(annotation.captchaUrl());
        phoneCaptchaField.setPhoneField(annotation.phoneField());
        phoneCaptchaField.setInterval(annotation.interval());

        form.addItem(phoneCaptchaField, annotation.section());
    }

    private void addEmailField(AbstractForm form, Field field, EmailField annotation) {
        final com.qxcmp.web.view.modules.form.field.EmailField emailField = new com.qxcmp.web.view.modules.form.field.EmailField();

        emailField.setName(field.getName());
        emailField.setLabel(annotation.value());
        emailField.setTooltip(annotation.tooltip());
        emailField.setRequired(annotation.required());
        emailField.setDisableAutoComplete(annotation.disableAutoComplete());
        emailField.setAutoFocus(annotation.autoFocus());
        emailField.setReadOnly(annotation.readOnly());
        emailField.setPlaceholder(annotation.placeholder());
        emailField.setMaxLength(annotation.maxLength());

        if (annotation.suffixList().length > 0) {
            emailField.setSuffixList(Arrays.asList(annotation.suffixList()));
        }

        form.addItem(emailField, annotation.section());
    }

    private void addPhoneField(AbstractForm form, Field field, PhoneField annotation) {
        final com.qxcmp.web.view.modules.form.field.PhoneField phoneField = new com.qxcmp.web.view.modules.form.field.PhoneField();

        phoneField.setName(field.getName());
        phoneField.setLabel(annotation.value());
        phoneField.setTooltip(annotation.tooltip());
        phoneField.setRequired(annotation.required());
        phoneField.setDisableAutoComplete(annotation.disableAutoComplete());
        phoneField.setAutoFocus(annotation.autoFocus());
        phoneField.setReadOnly(annotation.readOnly());
        phoneField.setPlaceholder(annotation.placeholder());
        phoneField.setMaxLength(annotation.maxLength());

        form.addItem(phoneField, annotation.section());
    }

    private void addAvatarField(AbstractForm form, Field field, AvatarField annotation) {
        final com.qxcmp.web.view.modules.form.field.AvatarField avatarField = new com.qxcmp.web.view.modules.form.field.AvatarField();

        avatarField.setName(field.getName());
        avatarField.setLabel(annotation.value());
        avatarField.setTooltip(annotation.tooltip());
        avatarField.setRequired(annotation.required());
        avatarField.setMaxSize(annotation.maxSize());

        form.addItem(avatarField, annotation.section());
    }

    private void addAlbumField(AbstractForm form, Field field, AlbumField annotation) {

        final com.qxcmp.web.view.modules.form.field.AlbumField albumField = new com.qxcmp.web.view.modules.form.field.AlbumField();

        albumField.setName(field.getName());
        albumField.setLabel(annotation.value());
        albumField.setTooltip(annotation.tooltip());
        albumField.setRequired(annotation.required());
        albumField.setMaxSize(annotation.maxSize());
        albumField.setMaxCount(annotation.maxCount());

        form.addItem(albumField, annotation.section());
    }

    private void addBooleanField(AbstractForm form, Field field, BooleanField annotation) {
        final com.qxcmp.web.view.modules.form.field.BooleanField booleanField = new com.qxcmp.web.view.modules.form.field.BooleanField();

        booleanField.setName(field.getName());
        booleanField.setLabel(annotation.value());
        booleanField.setTooltip(annotation.tooltip());
        booleanField.setRequired(annotation.required());
        booleanField.setStyle(annotation.style());

        form.addItem(booleanField, annotation.section());
    }

    private void addTextSelectionField(AbstractForm form, Field field, TextSelectionField annotation) {
        final com.qxcmp.web.view.modules.form.field.TextSelectionField textSelectionField = new com.qxcmp.web.view.modules.form.field.TextSelectionField();

        textSelectionField.setName(field.getName());
        textSelectionField.setLabel(annotation.value());
        textSelectionField.setTooltip(annotation.tooltip());
        textSelectionField.setRequired(annotation.required());
        textSelectionField.setSearch(annotation.search());

        textSelectionField.setItemValueIndex(annotation.itemValueIndex());
        textSelectionField.setItemTextIndex(annotation.itemTextIndex());

        if (StringUtils.isNotBlank(annotation.itemIndex())) {
            textSelectionField.setItemIndex(annotation.itemIndex());
        } else {
            textSelectionField.setItemIndex(DEFAULT_SELECTION_ITEM_PREFIX + field.getName());
        }

        if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
            textSelectionField.setMultiple(true);
        }

        form.addItem(textSelectionField, annotation.section());
    }

    private void addNumberField(AbstractForm form, Field field, NumberField annotation) {
        final com.qxcmp.web.view.modules.form.field.NumberField numberField = new com.qxcmp.web.view.modules.form.field.NumberField();

        numberField.setName(field.getName());
        numberField.setLabel(annotation.value());
        numberField.setTooltip(annotation.tooltip());
        numberField.setRequired(annotation.required());
        numberField.setDisableAutoComplete(annotation.disableAutoComplete());
        numberField.setAutoFocus(annotation.autoFocus());
        numberField.setReadOnly(annotation.readOnly());
        numberField.setPlaceholder(annotation.placeholder());
        numberField.setMin(annotation.min());
        numberField.setMax(annotation.max());
        numberField.setStep(annotation.step());

        form.addItem(numberField, annotation.section());
    }

    private void addDynamicField(AbstractForm form, Field field, DynamicField annotation) {
        final com.qxcmp.web.view.modules.form.field.DynamicField dynamicField = new com.qxcmp.web.view.modules.form.field.DynamicField();

        dynamicField.setName(field.getName());
        dynamicField.setLabel(annotation.value());
        dynamicField.setTooltip(annotation.tooltip());
        dynamicField.setRequired(annotation.required());
        dynamicField.setMaxCount(annotation.maxCount());
        dynamicField.setItemHeaders(Arrays.asList(annotation.itemHeaders()));
        dynamicField.setItemFields(Arrays.asList(annotation.itemFields()));

        if (dynamicField.getItemFields().isEmpty()) {
            dynamicField.setRawType(true);
        }

        form.addItem(dynamicField, annotation.section());
    }

    private void addHtmlField(AbstractForm form, Field field, HtmlField annotation) {
        final com.qxcmp.web.view.modules.form.field.HtmlField htmlField = new com.qxcmp.web.view.modules.form.field.HtmlField();

        htmlField.setName(field.getName());
        htmlField.setLabel(annotation.value());
        htmlField.setTooltip(annotation.tooltip());
        htmlField.setRequired(annotation.required());
        htmlField.setDisableAutoComplete(annotation.disableAutoComplete());
        htmlField.setAutoFocus(annotation.autoFocus());
        htmlField.setReadOnly(annotation.readOnly());
        htmlField.setPlaceholder(annotation.placeholder());
        htmlField.setRows(annotation.rows());

        form.addItem(htmlField, annotation.section());
    }

    private void addDateTimeField(AbstractForm form, Field field, DateTimeField annotation) {
        final com.qxcmp.web.view.modules.form.field.DateTimeField dateTimeField = new com.qxcmp.web.view.modules.form.field.DateTimeField();

        dateTimeField.setName(field.getName());
        dateTimeField.setLabel(annotation.value());
        dateTimeField.setTooltip(annotation.tooltip());
        dateTimeField.setRequired(annotation.required());
        dateTimeField.setDisableAutoComplete(annotation.disableAutoComplete());
        dateTimeField.setAutoFocus(annotation.autoFocus());
        dateTimeField.setReadOnly(annotation.readOnly());
        dateTimeField.setPlaceholder(annotation.placeholder());
        dateTimeField.setType(annotation.type());
        dateTimeField.setShowToday(annotation.showToday());
        dateTimeField.setShowAmPm(annotation.showAmPm());
        dateTimeField.setDisableYear(annotation.disableYear());
        dateTimeField.setDisableMonth(annotation.disableMonth());
        dateTimeField.setDisableMinute(annotation.disableMinute());

        if (annotation.enableDateRange()) {
            LocalDateTime minDate = LocalDateTime.now().plusDays(annotation.dateRangeBaseOffset());
            LocalDateTime maxDate = minDate.plusDays(annotation.dateRangeLength() > 0 ? annotation.dateRangeLength() - 1 : 0);
            dateTimeField.setMinDate(minDate.toDate());
            dateTimeField.setMaxDate(maxDate.toDate());
        }

        form.addItem(dateTimeField, annotation.section());
    }

    private void addFileUploadField(AbstractForm form, Field field, FileUploadField annotation) {
        final com.qxcmp.web.view.modules.form.field.FileUploadField fileUploadField = new com.qxcmp.web.view.modules.form.field.FileUploadField();

        fileUploadField.setName(field.getName());
        fileUploadField.setLabel(annotation.value());
        fileUploadField.setTooltip(annotation.tooltip());
        fileUploadField.setRequired(annotation.required());
        fileUploadField.setMaxSize(annotation.maxSize());
        fileUploadField.setMaxCount(annotation.maxCount());
        fileUploadField.setText(annotation.text());

        form.addItem(fileUploadField, annotation.section());
    }
}
