package com.qxcmp.audit;

import com.qxcmp.user.User;

import java.util.Optional;

/**
 * 操作实行器
 * <p>
 * 执行一个具体操作，并保存操作结果
 *
 * @author aaric
 */
public interface ActionExecutor {

    /**
     * 执行一个Web请求操作，并保存操作的审计日志
     *
     * @param title   操作名称
     * @param url     操作执行的Url
     * @param content 操作内容
     * @param user    执行操作的用户
     * @param action  要执行的操作
     *
     * @return 如果审计日志保存失败，则返回 {@link Optional#empty()}
     */
    AuditLog execute(String title, String url, String content, User user, Action action);
}
