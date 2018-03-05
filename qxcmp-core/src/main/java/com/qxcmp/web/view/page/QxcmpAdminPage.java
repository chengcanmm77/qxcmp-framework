package com.qxcmp.web.view.page;

import com.qxcmp.config.PlatformConfig;
import com.qxcmp.config.SiteService;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.message.InnerMessageService;
import com.qxcmp.message.SiteNotification;
import com.qxcmp.message.SiteNotificationService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.breadcrumb.AbstractBreadcrumb;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.menu.Menu;
import com.qxcmp.web.view.elements.menu.RightMenu;
import com.qxcmp.web.view.elements.menu.VerticalMenu;
import com.qxcmp.web.view.elements.menu.VerticalSubMenu;
import com.qxcmp.web.view.elements.menu.item.*;
import com.qxcmp.web.view.elements.message.*;
import com.qxcmp.web.view.modules.accordion.AccordionItem;
import com.qxcmp.web.view.modules.sidebar.AbstractSidebar;
import com.qxcmp.web.view.modules.sidebar.AccordionMenuSidebar;
import com.qxcmp.web.view.support.Fixed;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_PROFILE;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_SIDEBAR;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 平台后台页面抽象类
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public abstract class QxcmpAdminPage extends GenericQxcmpPage {

    protected UserService userService;
    protected SiteService siteService;
    protected InnerMessageService innerMessageService;
    protected NavigationService navigationService;
    protected PlatformConfig platformConfig;
    protected SiteNotificationService siteNotificationService;

    private AbstractSidebar sidebar = new AccordionMenuSidebar().setAttachEventsSelector(".ui.bottom.fixed.menu .sidebar.item");
    private AbstractBreadcrumb breadcrumb;
    private VerticalMenu verticalMenu;
    private Col content = new Col(Wide.SIXTEEN);

    @Override
    public QxcmpPage addComponent(Component component) {

        if (Objects.nonNull(component)) {
            content.addComponent(component);
        }

        return this;
    }

    @Override
    public QxcmpPage addComponent(Supplier<Component> supplier) {
        return addComponent(supplier.get());
    }

    @Override
    public QxcmpPage addComponents(Collection<Component> components) {
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

    private void buildSidebar() {
        final User user = userService.currentUser();
        buildSidebarTopFixedMenu(user);
        buildSidebarMenu();
        buildSideBottomFixedMenu(user);
    }

    private void buildSidebarTopFixedMenu(User user) {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.TOP);
        menu.addItem(new LogoImageItem(siteService.getLogo(), siteService.getTitle()));
        RightMenu rightMenu = new RightMenu();
        rightMenu.addItem(new BackendAccountAlarmItem(innerMessageService.countByUserId(user.getId())));
        rightMenu.addItem(new BackendAccountMenuItem(user, navigationService.get(NAVIGATION_ADMIN_PROFILE).getItems()));
        menu.setRightMenu(rightMenu);
        sidebar.setTopFixedMenu(menu);
    }

    private void buildSidebarMenu() {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.BOTTOM);
        menu.addItem(new SidebarIconItem());
        RightMenu rightMenu = new RightMenu();
        rightMenu.addItem(new TextItem("关于", platformConfig.getAdminUrl()));
        menu.setRightMenu(rightMenu);
        sidebar.setBottomFixedMenu(menu);
    }

    private void buildSideBottomFixedMenu(User user) {
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

        AbstractMessage message = buildSiteNotification();

        if (Objects.nonNull(message)) {
            container.addComponent(message);
        }

        if (Objects.nonNull(breadcrumb)) {
            grid.addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(breadcrumb)));
        }

        if (Objects.nonNull(verticalMenu)) {
            contentRow.addCol(new Col().setComputerWide(Wide.THREE).setMobileWide(Wide.SIXTEEN).addComponent(verticalMenu));
            contentRow.addCol(content.setComputerWide(Wide.THIRTEEN).setMobileWide(Wide.SIXTEEN));
        } else {
            contentRow.addCol(content.setGeneralWide(Wide.SIXTEEN));
        }

        grid.addItem(contentRow);
        container.addComponent(grid);
        sidebar.addContent(container);
    }

    private AbstractMessage buildSiteNotification() {

        AbstractMessage message = null;

        Optional<SiteNotification> activeNotifications = siteNotificationService.findActiveNotifications();

        if (activeNotifications.isPresent()) {

            SiteNotification siteNotification = activeNotifications.get();

            switch (siteNotification.getType()) {
                case "网站通知":
                    message = new InfoMessage(siteNotification.getTitle(), siteNotification.getContent());
                    break;
                case "网站警告":
                    message = new WarningMessage(siteNotification.getTitle(), siteNotification.getContent());
                    break;
                case "网站错误":
                    message = new ErrorMessage(siteNotification.getTitle(), siteNotification.getContent());
                    break;
                default:
                    message = new Message(siteNotification.getTitle(), siteNotification.getContent());
            }

            message.setCloseable(true);
        }

        return message;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    @Autowired
    public void setInnerMessageService(InnerMessageService innerMessageService) {
        this.innerMessageService = innerMessageService;
    }

    @Autowired
    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @Autowired
    public void setPlatformConfig(PlatformConfig platformConfig) {
        this.platformConfig = platformConfig;
    }

    @Autowired
    public void setSiteNotificationService(SiteNotificationService siteNotificationService) {
        this.siteNotificationService = siteNotificationService;
    }
}
