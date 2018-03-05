package com.qxcmp.util;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ProfilePageHelper {

    public static final String EMAIL_BINDING_SESSION_ATTR = "EMAIL_BINDING_CAPTCHA";
    public static final String EMAIL_BINDING_CONTENT_SESSION_ATTR = "EMAIL_BINDING_CONTENT";

    public static final List<String> QUESTIONS_LIST_1 = ImmutableList.of("您高中三年级班主任的名字", "您小学六年级班主任的名字", "您大学时的学号", "您大学本科时的上/下铺叫什么名字", "您大学的导师叫什么名字");
    public static final List<String> QUESTIONS_LIST_2 = ImmutableList.of("您父母称呼您的昵称", "您出生的医院名称", "您最好的朋友叫什么名字", "您母亲的姓名是", "您配偶的生日是");
    public static final List<String> QUESTIONS_LIST_3 = ImmutableList.of("您第一个宠物的名字", "您的第一任男朋友/女朋友姓名", "您第一家任职的公司名字");

}
