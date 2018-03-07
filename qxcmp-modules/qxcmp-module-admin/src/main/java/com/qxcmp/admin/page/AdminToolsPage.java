package com.qxcmp.admin.page;

import com.qxcmp.core.extension.AdminToolPageExtension;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.list.item.TextItem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 系统工具页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminToolsPage extends AbstractQxcmpAdminPage {

    private final List<AdminToolPageExtension> extensions;

    @Override
    public void render() {
        setBreadcrumb("控制台", "", "系统工具");
        addComponent(new TextContainer().addComponent(viewHelper.nextOverview("lab", "系统工具", "请选择系统工具")
                .addComponent(getToolListView())));
    }

    private com.qxcmp.web.view.elements.list.List getToolListView() {
        com.qxcmp.web.view.elements.list.List list = new com.qxcmp.web.view.elements.list.List();
        list.setLink();
        extensions.forEach(adminToolPageExtension -> list.addItem(new TextItem(adminToolPageExtension.getTitle()).setUrl(adminToolPageExtension.getUrl())));
        return list;
    }
}
