package com.qxcmp.admin.page;

import com.google.common.collect.Maps;
import com.qxcmp.admin.view.AdminTopMenuMobileItem;
import com.qxcmp.admin.view.AdminTopMenuProfileItem;
import com.qxcmp.user.User;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.breadcrumb.Breadcrumb;
import com.qxcmp.web.view.elements.breadcrumb.BreadcrumbItem;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.label.AbstractLabel;
import com.qxcmp.web.view.elements.label.Label;
import com.qxcmp.web.view.elements.menu.Menu;
import com.qxcmp.web.view.elements.menu.RightMenu;
import com.qxcmp.web.view.elements.menu.VerticalMenu;
import com.qxcmp.web.view.elements.menu.VerticalSubMenu;
import com.qxcmp.web.view.elements.menu.item.*;
import com.qxcmp.web.view.modules.accordion.AccordionItem;
import com.qxcmp.web.view.modules.sidebar.AbstractSidebar;
import com.qxcmp.web.view.modules.sidebar.AccordionMenuSidebar;
import com.qxcmp.web.view.page.AbstractPage;
import com.qxcmp.web.view.page.AbstractQxcmpPage;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Fixed;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.function.Supplier;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_PROFILE;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 后台页面抽象类
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public abstract class AbstractQxcmpAdminPage extends AbstractQxcmpPage {

    private AbstractSidebar sidebar = new AccordionMenuSidebar().setAttachEventsSelector(".ui.bottom.fixed.menu .sidebar.item");
    private boolean isMobile;
    private String menuNavigationId;
    private String menuNavigationActivateId;
    private Map<String, AbstractLabel> menuBadge = Maps.newLinkedHashMap();
    private RightMenu topFixedRightMenu = new RightMenu();
    private Col content = new Col(Wide.SIXTEEN);

    /**
     * 设置后台页面面包屑导航
     *
     * @return 如果返回空则不生成面包屑导航
     */
    protected List<String> getBreadcrumb() {
        return Collections.emptyList();
    }

    /**
     * 设置页面菜单
     * <p>
     * 页面菜单在移动端会隐藏至页面顶部
     *
     * @param id       导航ID
     * @param activeId 当前激活的导航ID
     */
    protected void setMenu(String id, String activeId) {
        menuNavigationId = id;
        menuNavigationActivateId = activeId;
    }

    public void setMenuBadge(String id, String text) {
        setMenuBadge(id, text, Color.NONE);
    }

    public void setMenuBadge(String id, String text, Color color) {
        setMenuBadge(id, new Label(text).setColor(color));
    }

    /**
     * 设置页面菜单项徽章
     *
     * @param id    导航ID
     * @param label 徽章
     */
    public void setMenuBadge(String id, AbstractLabel label) {
        menuBadge.put(id, label);
    }

    @Override
    public AbstractPage addComponent(Component component) {

        if (Objects.nonNull(component)) {
            content.addComponent(component);
        }

        return this;
    }

    @Override
    public AbstractPage addComponent(Supplier<Component> supplier) {
        return addComponent(supplier.get());
    }

    @Override
    public AbstractPage addComponents(Collection<Component> components) {
        content.addComponents(components);
        return this;
    }

    @Override
    public ModelAndView build() {
        buildSidebar();
        buildPageContent();
        super.addComponent(sidebar);
        return super.build();
    }

    @Override
    public void renderToMobile() {
        super.renderToMobile();
        isMobile = true;
    }

    private void buildSidebar() {
        final User user = userService.currentUser();
        buildSidebarTopFixedMenu(user);
        buildSidebarBottomFixedMenu();
        buildSidebarMenu(user);
    }

    private void buildSidebarTopFixedMenu(User user) {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.TOP);
        menu.addItem(new LogoImageItem(siteService.getLogo(), siteService.getTitle()));

        if (isMobile) {
            buildMenuForMobile();
        }

        topFixedRightMenu.addItem(new AdminTopMenuProfileItem(user, navigationService.get(NAVIGATION_ADMIN_PROFILE).getItems()));
        menu.setRightMenu(topFixedRightMenu);
        sidebar.setTopFixedMenu(menu);
    }

    private void buildSidebarBottomFixedMenu() {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.BOTTOM);
        menu.addItem(new SidebarIconItem());
        RightMenu rightMenu = new RightMenu();
        rightMenu.addItem(new TextItem("关于", ADMIN_URL + "/about"));
        menu.setRightMenu(rightMenu);
        sidebar.setBottomFixedMenu(menu);
    }

    private void buildSidebarMenu(User user) {
        navigationService.get(NAVIGATION_ADMIN_SIDEBAR).getItems().stream()
                .filter(navigation -> navigation.isVisible(user))
                .forEach(navigation -> {
                    if (navigation.getItems().isEmpty()) {
                        if (Objects.nonNull(navigation.getIcon())) {
                            sidebar.addSideContent(new LabeledIconItem(navigation.getTitle(), navigation.getIcon(), navigation.getAnchor()));
                        } else {
                            sidebar.addSideContent(new TextItem(navigation.getTitle(), navigation.getAnchor().getHref()).setLink());
                        }
                    } else {
                        if (navigation.getItems().stream().anyMatch(n -> n.isVisible(user))) {

                            VerticalSubMenu verticalMenu = new VerticalSubMenu();

                            navigation.getItems().forEach(item -> {
                                if (item.isVisible(user)) {
                                    verticalMenu.addItem(new TextItem(item.getTitle(), item.getAnchor().getHref()));
                                }
                            });

                            AccordionItem accordionItem = new AccordionItem();
                            accordionItem.setTitle(navigation.getTitle());
                            accordionItem.setContent(verticalMenu);

                            sidebar.addSideContent(new AccordionMenuItem(accordionItem).setLink());
                        }
                    }
                });
    }

    private void buildPageContent() {
        final Container container = new Container();
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        final Row contentRow = new Row();

        buildPageBreadcrumb(grid);

        if (!isMobile) {
            buildMenuForNormal(contentRow);
        } else {
            contentRow.addCol(content.setGeneralWide(Wide.SIXTEEN));
        }

        grid.addItem(contentRow);
        container.addComponent(grid);
        sidebar.addContent(container);
    }

    private void buildPageBreadcrumb(AbstractGrid grid) {
        List<String> contents = getBreadcrumb();
        if (Objects.nonNull(contents) && !contents.isEmpty()) {
            Breadcrumb breadcrumb = new Breadcrumb();

            breadcrumb.addItem(new BreadcrumbItem("控制台", ADMIN_URL));

            for (int i = 0; i < contents.size(); i += 2) {
                String text = contents.get(i);
                if (i + 1 == contents.size()) {
                    breadcrumb.addItem(new BreadcrumbItem(text));
                } else {
                    String url = contents.get(i + 1);
                    if (Objects.nonNull(url)) {
                        breadcrumb.addItem(new BreadcrumbItem(text, url));
                    } else {
                        breadcrumb.addItem(new BreadcrumbItem(text));
                    }
                }
            }
            grid.addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(breadcrumb)));
        }
    }

    private void buildMenuForMobile() {
        VerticalMenu verticalMenu = getVerticalMenu();
        if (!verticalMenu.getItems().isEmpty()) {
            topFixedRightMenu.addItem(new AdminTopMenuMobileItem(verticalMenu));
        }
    }

    private void buildMenuForNormal(Row contentRow) {

        VerticalMenu verticalMenu = getVerticalMenu();

        if (verticalMenu.getItems().isEmpty()) {
            contentRow.addCol(content.setGeneralWide(Wide.SIXTEEN));
        } else {
            contentRow.addCol(new Col().setComputerWide(Wide.THREE).setMobileWide(Wide.SIXTEEN).addComponent(verticalMenu));
            contentRow.addCol(content.setComputerWide(Wide.THIRTEEN).setMobileWide(Wide.SIXTEEN));
        }
    }

    private VerticalMenu getVerticalMenu() {
        VerticalMenu verticalMenu = new VerticalMenu().setFluid();
        verticalMenu.setTabular();

        if (StringUtils.isNotBlank(menuNavigationId)) {
            navigationService.get(menuNavigationId).getItems().forEach(navigation -> {
                if (navigation.isVisible(userService.currentUser())) {
                    if (navigation.getItems().isEmpty()) {
                        TextItem textItem = new TextItem(navigation.getTitle(), navigation.getAnchor().getHref());
                        if (StringUtils.equals(menuNavigationActivateId, navigation.getId())) {
                            textItem.setActive();
                        }
                        textItem.addContext("navigation-id", navigation.getId());
                        verticalMenu.addItem(textItem);
                    }
                }
            });
        }

        menuBadge.forEach((s, label) ->
                verticalMenu.getItems()
                        .forEach(menuItem -> {
                            if (Objects.nonNull(menuItem.getContext("navigation-id")) && StringUtils.equals(menuItem.getContext("navigation-id").toString(), s)) {
                                if (menuItem instanceof TextItem) {
                                    TextItem textItem = (TextItem) menuItem;
                                    textItem.setBadge(label);
                                }
                            }
                        }));

        return verticalMenu;
    }
}
