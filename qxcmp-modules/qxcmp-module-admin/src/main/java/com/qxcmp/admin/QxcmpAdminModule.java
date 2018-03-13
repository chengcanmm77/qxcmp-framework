package com.qxcmp.admin;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * 后台模块全局配置
 *
 * @author Aaric
 */
public class QxcmpAdminModule {

    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_PROFILE_URL = ADMIN_URL + "/profile";
    public static final String ADMIN_AUDIT_LOG_URL = ADMIN_URL + "/audit";
    public static final String ADMIN_TOOLS_URL = ADMIN_URL + "/tools";
    public static final String ADMIN_SETTINGS_URL = ADMIN_URL + "/settings";
    public static final String ADMIN_SETTINGS_SITE_URL = ADMIN_SETTINGS_URL + "/site";
    public static final String ADMIN_SETTINGS_DICTIONARY_URL = ADMIN_SETTINGS_URL + "/dictionary";
    public static final String ADMIN_SETTINGS_EMAIL_URL = ADMIN_SETTINGS_URL + "/email";
    public static final String ADMIN_SETTINGS_SMS_URL = ADMIN_SETTINGS_URL + "/sms";
    public static final String ADMIN_SECURITY_URL = ADMIN_URL + "/security";
    public static final String ADMIN_STATISTIC_URL = ADMIN_URL + "/statistic";

    public static final String EMAIL_BINDING_SESSION_ATTR = "EMAIL_BINDING_CAPTCHA";
    public static final String EMAIL_BINDING_CONTENT_SESSION_ATTR = "EMAIL_BINDING_CONTENT";
    public static final List<String> QUESTIONS_LIST_1 = ImmutableList.of("您高中三年级班主任的名字", "您小学六年级班主任的名字", "您大学时的学号", "您大学本科时的上/下铺叫什么名字", "您大学的导师叫什么名字");
    public static final List<String> QUESTIONS_LIST_2 = ImmutableList.of("您父母称呼您的昵称", "您出生的医院名称", "您最好的朋友叫什么名字", "您母亲的姓名是", "您配偶的生日是");
    public static final List<String> QUESTIONS_LIST_3 = ImmutableList.of("您第一个宠物的名字", "您的第一任男朋友/女朋友姓名", "您第一家任职的公司名字");
    public static final List<String> WATERMARK_POSITIONS = ImmutableList.of("左上", "中上", "右上", "左中", "居中", "右中", "左下", "中下", "右下");
}
