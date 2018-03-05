package com.qxcmp.audit;

import com.google.common.collect.Maps;
import com.qxcmp.user.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 操作执行器默认实现
 *
 * @author aaric
 * @see ActionExecutor
 */
@Service
@AllArgsConstructor
public class ActionExecutorImpl implements ActionExecutor {

    private AuditLogService auditLogService;

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
    @Override
    public AuditLog execute(String title, String url, String content, User user, Action action) {
        checkArgument(StringUtils.isNotBlank(title));
        checkArgument(StringUtils.isNotBlank(url));
        checkArgument(Objects.nonNull(action));
        return auditLogService.create(() -> {
            AuditLog auditLog = auditLogService.next();
            auditLog.setDateCreated(new Date());
            auditLog.setTitle(title);
            auditLog.setUrl(url);
            auditLog.setContent(content);
            auditLog.setOwner(user);

            try {
                auditLog.setActionContext(Maps.newLinkedHashMap());
                action.execute(auditLog.getActionContext());

                auditLog.setComments("");
                auditLog.setStatus(AuditLog.Status.SUCCESS);
            } catch (ActionException e) {

                if (StringUtils.isNotBlank(e.getMessage())) {
                    auditLog.setComments(e.getMessage());
                } else if (Objects.nonNull(e.getCause())) {
                    auditLog.setComments(e.getCause().toString());
                } else {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    printWriter.flush();
                    stringWriter.flush();
                    auditLog.setComments(stringWriter.toString());
                }

                auditLog.setStatus(AuditLog.Status.FAILURE);
            }

            return auditLog;
        });
    }
}
