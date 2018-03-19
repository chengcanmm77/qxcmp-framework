package com.qxcmp.web.view.page;

import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.icon.CircularIcon;
import com.qxcmp.web.view.elements.menu.Menu;
import com.qxcmp.web.view.elements.menu.RightMenu;
import com.qxcmp.web.view.elements.menu.item.IconItem;
import com.qxcmp.web.view.elements.menu.item.LabeledIconItem;
import com.qxcmp.web.view.elements.menu.item.TextItem;
import com.qxcmp.web.view.modules.sidebar.AbstractSidebar;
import com.qxcmp.web.view.modules.sidebar.AccordionMenuSidebar;
import com.qxcmp.web.view.modules.sidebar.MobileSidebarLogoutButton;
import com.qxcmp.web.view.support.Fixed;
import com.qxcmp.web.view.views.ProfileHeader;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 平台一般页面
 *
 * @author Aaric
 */
public abstract class AbstractQxcmpPage extends AbstractPage {

    private final static String DEFAULT_STYLESHEET = "/assets/scripts/qxcmp.css";
    private final static String DEFAULT_SCRIPT = "/assets/scripts/qxcmp.js";

    private AbstractSidebar mobileSidebar;

    @Setter
    private ProfileHeader profileHeader;
    @Setter
    private String mobileSidebarIcon = "user";
    @Setter
    private String mobileActionIcon;
    @Setter
    private String mobileActionTitle;
    @Setter
    private String mobileActionUrl;
    @Setter
    private String mobileTitle;
    @Setter
    private String mobileTitleUrl;
    @Setter
    private String mobileNavigation;

    public AbstractQxcmpPage() {
        addStylesheet(DEFAULT_STYLESHEET);
        addJavascript(DEFAULT_SCRIPT);
    }

    @Override
    public void renderToMobile() {
        super.renderToMobile();
        MobilePage qxcmpMobilePage = this.getClass().getAnnotation(MobilePage.class);
        if (Objects.nonNull(qxcmpMobilePage)) {
            buildMobileSidebar();
        }
    }

    private void buildMobileSidebar() {
        mobileSidebar = new AccordionMenuSidebar().setAttachEventsSelector(String.format(".ui.top.fixed.menu .%s.icon", mobileSidebarIcon.replaceAll("\\s", ".")));
        buildMobileSidebarTopMenu();
        buildMobileSidebarContent();
        getComponents().forEach(mobileSidebar::addContent);
        getComponents().clear();
        addComponent(mobileSidebar);
        mobileSidebar.setCustomClass("mobile");
    }

    private void buildMobileSidebarTopMenu() {
        Menu menu = new Menu();
        menu.setFixed(Fixed.TOP).setInverted();
        menu.addItem(new IconItem(new CircularIcon(mobileSidebarIcon)));
        if (StringUtils.isNotBlank(mobileTitle)) {
            if (StringUtils.isNotBlank(mobileTitleUrl)) {
                menu.addItem(new TextItem(mobileTitle, mobileTitleUrl));
            } else {
                menu.addItem(new TextItem(mobileTitle));
            }
        }
        RightMenu rightMenu = new RightMenu();
        if (StringUtils.isNotBlank(mobileActionIcon)) {
            rightMenu.addItem(new IconItem(mobileActionIcon, new Anchor("", mobileActionUrl)));
        } else if (StringUtils.isNotBlank(mobileActionTitle)) {
            rightMenu.addItem(new TextItem(mobileActionTitle, mobileActionUrl));
        }

        if (!rightMenu.getItems().isEmpty()) {
            menu.setRightMenu(rightMenu);
        }
        mobileSidebar.setTopFixedMenu(menu);
    }

    private void buildMobileSidebarContent() {
        User user = userService.currentUser();
        if (Objects.nonNull(profileHeader)) {
            mobileSidebar.addSideContent(profileHeader);
        } else if (Objects.nonNull(user)) {
            mobileSidebar.addSideContent(new ProfileHeader(user.getPortrait(), "/profile").setContent(user.getDisplayName()));
        }
        if (StringUtils.isNotBlank(mobileNavigation)) {
            try {
                navigationService.get(mobileNavigation).getItems().stream().filter(navigation -> navigation.isVisible(user)).forEach(navigation -> {
                    if (Objects.isNull(navigation.getIcon())) {
                        mobileSidebar.addSideContent(new TextItem(navigation.getTitle(), navigation.getAnchor().getHref()));
                    } else {
                        mobileSidebar.addSideContent(new LabeledIconItem(navigation.getTitle(), navigation.getIcon(), navigation.getAnchor()));
                    }
                });
            } catch (Exception ignored) {

            }
        }
        if (Objects.nonNull(user)) {
            mobileSidebar.addSideContent(new MobileSidebarLogoutButton());
        }
    }
}
