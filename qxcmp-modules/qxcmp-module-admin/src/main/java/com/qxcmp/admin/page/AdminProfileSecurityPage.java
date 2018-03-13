package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.table.*;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_PROFILE_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminProfileSecurityPage extends AbstractQxcmpAdminPage {

    private final User user;
    private final Boolean hasSecurityQuestion;

    @Override
    public void render() {
        addComponent(viewHelper.nextOverview("安全设置")
                .addComponent(new Table().setBasic().setHeader((AbstractTableHeader) new TableHeader()
                        .addRow(getTableRow("登录密码", "安全性高的密码可以使帐号更安全", StringUtils.isNotBlank(user.getPassword()), "修改", ADMIN_PROFILE_URL + "/security/password"))
                        .addRow(getTableRow("手机绑定", "手机号可以直接用于登录", StringUtils.isNotBlank(user.getPhone()), "绑定", ADMIN_PROFILE_URL + "/security/phone"))
                        .addRow(getTableRow("邮箱绑定", "邮箱可以直接用于登录", StringUtils.isNotBlank(user.getEmail()), "绑定", ADMIN_PROFILE_URL + "/security/email"))
                        .addRow(getTableRow("密保问题", "建议设置三个容易记住，且最不容易被他人获取的问题及答案，更有效保障您的密码安全", hasSecurityQuestion, "设置", ADMIN_PROFILE_URL + "/security/question"))
                )));
    }

    private TableRow getTableRow(String title, String description, boolean flag, String buttonText, String buttonUrl) {
        TableRow tableRow = new TableRow();
        AbstractTableCell tableHead = new TableHead(title).setAlignment(Alignment.CENTER);
        TableData descCell = new TableData(description);
        AbstractTableCell iconCell = new TableData(getIcon(flag)).setAlignment(Alignment.CENTER);
        AbstractTableCell buttonCell = new TableData(new Button(buttonText, buttonUrl).setBasic()).setAlignment(Alignment.CENTER);

        tableRow.addCell(tableHead);
        tableRow.addCell(descCell);
        tableRow.addCell(iconCell);
        tableRow.addCell(buttonCell);
        return tableRow;
    }

    private Icon getIcon(boolean flag) {
        return flag ? getGreenIcon() : getOrangeIcon();
    }

    private Icon getOrangeIcon() {
        return new Icon("warning circle").setColor(Color.ORANGE);
    }

    private Icon getGreenIcon() {
        return new Icon("check circle").setColor(Color.GREEN);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("个人中心", ADMIN_PROFILE_URL, "安全设置");
    }
}
