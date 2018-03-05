package com.qxcmp.account;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import org.springframework.stereotype.Service;

/**
 * 平台码服务
 * <p>
 * 提供额外以下服务： <ol> <li>判断一个平台码是否有效</li> <li>为指定用户生成一个账户激活码</li> <li>为指定用户生成一个账户重置码</li> </ol>
 *
 * @author aaric
 */
@Service
public class AccountCodeService extends AbstractEntityService<AccountCode, String, AccountCodeRepository> {

    /**
     * 验证码过期时间，默认为1天
     */
    private static final long CODE_EXPIRE_DURATION = 1000 * 60 * 60 * 24;

    /**
     * 检查平台码是否可用
     * <p>
     * 如果该平台码存在并且没有过期则返回{@code true}
     *
     * @param id 平台码ID
     *
     * @return 该平台码是否过期
     */
    public boolean isInvalidCode(String id) {
        return findOne(id).map(this::isInvalidCode).orElse(true);
    }

    /**
     * 生成一个账户激活码，并保存
     *
     * @param userId 要激活的用户主键
     *
     * @return 保存以后的激活码
     */
    public AccountCode nextActivateCode(String userId) {
        return create(() -> next(AccountCode.Type.ACTIVATE, userId));
    }

    /**
     * 生成一个账户重置码，并保存
     *
     * @param userId 要重置的用户主键
     *
     * @return 保存以后的重置码
     */
    public AccountCode nextPasswordCode(String userId) {
        return create(() -> next(AccountCode.Type.PASSWORD, userId));
    }

    /**
     * 生成一个平台码
     *
     * @param type   平台码类型
     * @param userId 平台码关联的用户主键
     *
     * @return 平台码实体
     */
    private AccountCode next(AccountCode.Type type, String userId) {
        AccountCode code = next();
        code.setId(IDGenerator.next());
        code.setType(type);
        code.setUserId(userId);
        return code;
    }

    private boolean isInvalidCode(AccountCode code) {
        return System.currentTimeMillis() - code.getDateCreated().getTime() > CODE_EXPIRE_DURATION;
    }
}
