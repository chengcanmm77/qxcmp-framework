package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.core.extension.AdminUserDetailsPageToolbarExtensionPoint;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.button.Buttons;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.modules.pagination.Pagination;
import com.qxcmp.web.view.modules.table.dictionary.CollectionValueCell;
import com.qxcmp.web.view.modules.table.dictionary.ComponentCell;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_USER_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminUserDetailsPage extends AbstractQxcmpAdminPage {

    private final User user;
    private final Page<DepositOrder> depositOrders;
    private AdminUserDetailsPageToolbarExtensionPoint toolbarExtensionPoint;

    @Override
    public void render() {
        addComponent(viewHelper.nextInfoOverview("用户详情", user.getDisplayName())
                .addComponent(renderToolbar())
                .addComponent(new Divider())
                .addComponent(renderUserDetails())
                .addLink("返回", ADMIN_USER_URL));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("用户管理", ADMIN_USER_URL, "用户详情");
    }

    @Autowired
    public void setToolbarExtensionPoint(AdminUserDetailsPageToolbarExtensionPoint toolbarExtensionPoint) {
        this.toolbarExtensionPoint = toolbarExtensionPoint;
    }

    private Buttons renderToolbar() {
        Buttons toolbar = new Buttons();
        toolbarExtensionPoint.getExtensions().forEach(extension -> toolbar.addButton(new Button(extension.getTitle(), ADMIN_USER_URL + "/" + user.getId() + "/" + extension.getSuffix(), extension.getTarget()).setBasic().setSecondary()));
        return toolbar;
    }

    private AbstractGrid renderUserDetails() {
        VerticallyDividedGrid grid = new VerticallyDividedGrid();
        grid.addItem(new Row()
                .addCol(new Col().setComputerWide(Wide.SIX).setMobileWide(Wide.SIXTEEN)
                        .addComponent(new Image(user.getPortrait()).setCentered().setCircular().setSize(Size.SMALL))
                        .addComponent(new ContentHeader("账户资料", Size.NONE).setDividing())
                        .addComponent(viewHelper.nextTable(table -> {
                            table.put("UUID", user.getId());
                            table.put("OpenID", user.getOpenID());
                            table.put("UnionID", user.getUnionId());
                            table.put("上次登录时间", user.getDateLogin());
                            table.put("拥有角色", new CollectionValueCell(user.getRoles(), "name"));
                            table.put("账户过期状态", getBooleanIcon(user.isAccountNonExpired()));
                            table.put("账户锁定状态", getBooleanIcon(user.isAccountNonLocked()));
                            table.put("账户密码状态", getBooleanIcon(user.isCredentialsNonExpired()));
                            table.put("账户可用状态", getBooleanIcon(user.isEnabled()));
                        })))
                .addCol(new Col().setComputerWide(Wide.FIVE).setMobileWide(Wide.SIXTEEN)
                        .addComponent(new ContentHeader("基本资料", Size.NONE).setDividing())
                        .addComponent(viewHelper.nextTable(table -> {
                            table.put("用户名", user.getUsername());
                            table.put("邮箱", user.getEmail());
                            table.put("手机", user.getPhone());
                            table.put("真实姓名", user.getName());
                            table.put("昵称", user.getNickname());
                            table.put("性别", user.getSex());
                            table.put("生日", user.getBirthday());
                            table.put("语言", user.getLanguage());
                            table.put("城市", user.getCity());
                            table.put("省份", user.getProvince());
                            table.put("国家", user.getCountry());
                            table.put("个性签名", user.getPersonalizedSignature());
                            table.put("备注", user.getRemark());
                        })))
                .addCol(new Col().setComputerWide(Wide.FIVE).setMobileWide(Wide.SIXTEEN)
                        .addComponent(new ContentHeader("充值记录", Size.NONE).setDividing())
                        .addComponent(viewHelper.nextTable(table -> {
                            table.put("完成时间", "充值金额");
                            if (depositOrders.getContent().isEmpty()) {
                                table.put("暂无记录", "");
                            }
                            depositOrders.forEach(depositOrder -> table.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(depositOrder.getDateFinished()), new DecimalFormat("￥0.00").format((double) depositOrder.getFee() / 100)));
                        }))
                        .addComponent(new Pagination("", depositOrders.getNumber() + 1, (int) depositOrders.getTotalElements(), depositOrders.getSize()))
                )
        );
        return grid;
    }

    private ComponentCell getBooleanIcon(boolean flag) {
        Icon icon = flag ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.RED);
        return new ComponentCell(icon);
    }
}
