package com.qxcmp.core.navigation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 平台导航组件模型
 * <p>
 * 该模型可以渲染为多种组件
 * <p>
 * 侧边导航栏、业内菜单、顶部菜单等等
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractNavigation {

    /**
     * ID 全局唯一
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标 - 可选
     */
    private Icon icon;

    /**
     * 图片 - 可选
     */
    private Image image;

    /**
     * 超链接
     */
    private Anchor anchor;

    /**
     * 顺序
     */
    private int order;

    /**
     * 用于访问该菜单项所需要的权限并集
     * <p>
     * 需要有所有的权限才能访问
     */
    private Set<String> privilegesAnd = Sets.newHashSet();

    /**
     * 用于方位该菜单项所需要的权限交集
     * <p>
     * 只有有其中任何一个权限，则可以访问
     */
    private Set<String> privilegesOr = Sets.newHashSet();

    /**
     * 子菜单项
     * <p>
     * 最多支持二级子菜单
     */
    private List<AbstractNavigation> items = Lists.newArrayList();

    public AbstractNavigation(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public AbstractNavigation(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.anchor = new Anchor(title, url);
    }

    public AbstractNavigation(String id, String title, String url, AnchorTarget target) {
        this.id = id;
        this.title = title;
        this.anchor = new Anchor(title, url, target.toString());
    }

    /**
     * 判断该导航对用户是否可见
     * <p>
     * 若该导航有子菜单，同样需要判断子菜单是否可见
     *
     * @param user 用户
     * @return 是否可见
     */
    public boolean isVisible(User user) {
        Set<String> userPrivileges = Sets.newHashSet();

        if (Objects.nonNull(user)) {
            user.getAuthorities().forEach(auth -> userPrivileges.add(auth.getAuthority().replaceAll("ROLE_", "")));
        }

        if (!items.isEmpty()) {
            return items.stream().anyMatch(navigation -> navigation.isVisible(user));
        } else {
            return (privilegesAnd.isEmpty() || privilegesAnd.stream().allMatch(userPrivileges::contains)) && (privilegesOr.isEmpty() || privilegesOr.stream().anyMatch(userPrivileges::contains));
        }
    }

    public AbstractNavigation addItem(AbstractNavigation item) {
        items.add(item);
        items.sort(Comparator.comparingInt(AbstractNavigation::getOrder));
        return this;
    }

    public AbstractNavigation addItems(Collection<? extends AbstractNavigation> items) {
        this.items.addAll(items);
        this.items.sort(Comparator.comparingInt(AbstractNavigation::getOrder));
        return this;
    }

    public AbstractNavigation setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbstractNavigation setImage(Image image) {
        this.image = image;
        return this;
    }

    public AbstractNavigation setOrder(int order) {
        this.order = order;
        return this;
    }

    public AbstractNavigation setPrivilegesAnd(Set<String> privilegeAnd) {
        this.privilegesAnd = privilegeAnd;
        return this;
    }

    public AbstractNavigation setPrivilegesOr(Set<String> privilegeOr) {
        this.privilegesOr = privilegeOr;
        return this;
    }
}
