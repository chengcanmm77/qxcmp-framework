package com.qxcmp.audit;

import java.util.Map;

/**
 * 用户操作抽象接口，命令模式
 * <p>
 * 该接口的使用说明如下
 * <p>
 * <ol> <li>如果用户的某些行为需要记录在审计日志当中，则需要使用操作接口</li> <li>操作的执行权限应该有外层来控制</li> <li>操作应该使用Lambda表达式</li>
 * <li>操作由操作执行器来执行，执行器会自动创建审计日志</li> <li>如果在操作中抛出操作异常，则会被操作执行器捕获，并将审计日志标记为失败状态</li> <li>一个操作在Web层通常表现为表单的提交</li> </ol>
 *
 * @author aaric
 * @see ActionExecutor
 */
public interface Action {

    /**
     * 操作执行接口
     * <p>
     * 执行该操作
     *
     * @param context 操作上下文，用于保存操作中用到的信息，也用于结果页面来获取
     * @throws ActionException 当操作当中的某些步骤失败的时候应该抛出该异常，抛出异常的操作会被标记为操作失败
     */
    void execute(Map<String, Object> context) throws ActionException;
}
