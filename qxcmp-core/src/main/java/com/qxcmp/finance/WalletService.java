package com.qxcmp.finance;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.exception.NoBalanceException;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * 钱包实体服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class WalletService extends AbstractEntityService<Wallet, String, WalletRepository> {

    private final UserService userService;
    private final WalletRecordService walletRecordService;


    /**
     * 根据用户ID获取钱包 如果用户存在但是钱包不存在，则返回新创建的钱包
     *
     * @param userId 用户ID
     *
     * @return 属于用于的钱包，如果用于不存在则返回empty
     */
    public Optional<Wallet> findByUserId(String userId) {
        return userService.findOne(userId)
                .map(user -> repository.findByUserId(userId)
                        .orElseGet(() -> create(() -> {
                            Wallet wallet = next();
                            wallet.setUserId(userId);
                            return wallet;
                        })));
    }

    /**
     * 改变一个用户钱包的余额
     *
     * @param userId   用户主键
     * @param amount   改变数量，整数为增加，负数为减小
     * @param comments 备注信息
     * @param url      查看消费详情的url
     *
     * @return 修改后的钱包
     */
    public Wallet changeBalance(String userId, int amount, String comments, String url) throws NoBalanceException {
        Wallet wallet = findByUserId(userId).orElseThrow(() -> new RuntimeException("Can't get user wallet"));
        if (amount < 0 && wallet.getBalance() < Math.abs(amount)) {
            throw new NoBalanceException("Not enough balance");
        }
        if (amount != 0) {
            walletRecordService.create(() -> {
                WalletRecord walletRecord = walletRecordService.next();
                walletRecord.setUserId(userId);
                walletRecord.setType(WalletRecordType.BALANCE.name());
                walletRecord.setAmount(amount);
                walletRecord.setDate(new Date());
                walletRecord.setComments(comments);
                walletRecord.setUrl(url);
                return walletRecord;
            });
        }
        return update(wallet.getId(), w -> w.setBalance(w.getBalance() + amount));
    }

    /**
     * 改变一个用户的钱包积分
     *
     * @param userId   用户主键
     * @param amount   改变数量，整数为增加，负数为减小
     * @param comments 备注信息
     * @param url      查看消费详情的url
     *
     * @return 修改后的钱包
     */
    public Wallet changePoints(String userId, int amount, String comments, String url) throws NoBalanceException {
        Wallet wallet = findByUserId(userId).orElseThrow(() -> new RuntimeException("Can't get user wallet"));
        if (amount < 0 && wallet.getPoints() < Math.abs(amount)) {
            throw new NoBalanceException("Not enough points");
        }
        if (amount != 0) {
            walletRecordService.create(() -> {
                WalletRecord walletRecord = walletRecordService.next();
                walletRecord.setUserId(userId);
                walletRecord.setType(WalletRecordType.POINT.name());
                walletRecord.setAmount(amount);
                walletRecord.setDate(new Date());
                walletRecord.setComments(comments);
                walletRecord.setUrl(url);
                return walletRecord;
            });
        }
        return update(wallet.getId(), w -> w.setPoints(w.getPoints() + amount));
    }

    @Override
    public Wallet create(Wallet entity) {
        entity.setId(IDGenerator.next());
        return super.create(entity);
    }
}
