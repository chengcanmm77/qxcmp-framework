package com.qxcmp.redeem.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminPage;
import com.qxcmp.redeem.RedeemKey;
import com.qxcmp.redeem.RedeemKeyStatus;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.container.TextContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;

/**
 * @author Aaric
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminRedeemDetailsPage extends AbstractQxcmpAdminPage {

    private final RedeemKey redeemKey;

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(viewHelper.nextInfoOverview("兑换码详情")
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("ID", redeemKey.getId());
                    objectObjectMap.put("业务名称", redeemKey.getType());
                    objectObjectMap.put("业务数据", redeemKey.getContent());
                    objectObjectMap.put("状态", redeemKey.getStatus().getValue());
                    objectObjectMap.put("创建时间", redeemKey.getDateCreated());
                    objectObjectMap.put("过期时间", redeemKey.getDateExpired());

                    if (redeemKey.getStatus().equals(RedeemKeyStatus.USED)) {
                        objectObjectMap.put("使用时间", redeemKey.getDateUsed());
                        objectObjectMap.put("使用者ID", redeemKey.getUserId());
                        objectObjectMap.put("使用者名称", userService.findOne(redeemKey.getUserId()).map(User::getDisplayName).orElse(""));
                    }
                }))
                .addLink("返回", ADMIN_REDEEM_URL)));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("兑换码管理", ADMIN_REDEEM_URL, "兑换码详情");
    }
}
