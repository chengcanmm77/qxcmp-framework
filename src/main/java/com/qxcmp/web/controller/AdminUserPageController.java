package com.qxcmp.web.controller;

import com.qxcmp.audit.ActionException;
import com.qxcmp.core.event.AdminUserRoleEditEvent;
import com.qxcmp.core.event.AdminUserStatusEditEvent;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.security.RoleService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminUserRoleForm;
import com.qxcmp.web.form.AdminUserStatusForm;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.button.Buttons;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.message.InfoMessage;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.elements.statistic.Statistic;
import com.qxcmp.web.view.elements.statistic.Statistics;
import com.qxcmp.web.view.modules.pagination.Pagination;
import com.qxcmp.web.view.modules.table.dictionary.CollectionValueCell;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.ItemCount;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import com.qxcmp.weixin.WeixinService;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/user")
@RequiredArgsConstructor
public class AdminUserPageController extends QxcmpController {

    private final RoleService roleService;
    private final WeixinService weixinService;
    private final DepositOrderService depositOrderService;

    @GetMapping("")
    public ModelAndView userPage() {
        return page().addComponent(new PageHeader(HeaderType.H2, "用户管理").setDividing())
                .addComponent(() -> {

                    Date dateTarget = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();

                    return new Statistics().setCount(ItemCount.THREE)
                            .addStatistic(new Statistic("用户总数", userService.count().toString()))
                            .addStatistic(new Statistic("今日新增", Long.toString(userService.findByDateCreate(dateTarget).size())))
                            .addStatistic(new Statistic("今日登陆", Long.toString(userService.findByDateLogin(dateTarget).size())));
                })
                .setBreadcrumb("控制台", "", "用户管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_USER, "")
                .build();
    }

    @GetMapping("/all")
    public ModelAndView userAllPage(Pageable pageable) {
        return page().addComponent(convertToTable("all", new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateCreated"), userService))
                .setBreadcrumb("控制台", "", "用户管理", "user", "全部用户")
                .setVerticalNavigation(NAVIGATION_ADMIN_USER, NAVIGATION_ADMIN_USER_ALL)
                .build();
    }

    @GetMapping("/all/{id}/details")
    public ModelAndView userAllDetailsPage(@PathVariable String id) {
        return redirect(QXCMP_BACKEND_URL + "/user/" + id + "/details");
    }

    @GetMapping("/weixin")
    public ModelAndView userWeixinPage(Pageable pageable) {
        Page<User> users = userService.findWeixinUser(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "subscribeTime"));

        Grid grid = new Grid();
        Col col = new Col(Wide.SIXTEEN);

        if (weixinService.getSyncService().isWeixinUserSync()) {
            col.addComponent(new InfoMessage(String.format("微信用户正在同步中... 当前进度为 %d/%d，请稍后刷新查看", weixinService.getSyncService().getCurrentUserSync(), weixinService.getSyncService().getTotalUserSync())).setCloseable());
        }

        col.addComponent(convertToTable("weixin", User.class, users));

        return page().addComponent(grid.addItem(new Row().addCol(col)))
                .setBreadcrumb("控制台", "", "用户管理", "user", "微信用户")
                .setVerticalNavigation(NAVIGATION_ADMIN_USER, NAVIGATION_ADMIN_USER_WEIXIN)
                .build();
    }

    @GetMapping("/weixin/{id}/details")
    public ModelAndView userWeixinDetailsPage(@PathVariable String id) {
        return redirect(QXCMP_BACKEND_URL + "/user/" + id + "/details");
    }

    @PostMapping("/weixin/sync")
    public ResponseEntity<RestfulResponse> userWeixinSyncPage() {
        weixinService.getSyncService().syncUsers(currentUser().orElseThrow(RuntimeException::new));
        return ResponseEntity.ok(new RestfulResponse(HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/details")
    public ModelAndView userDetailsPage(@PathVariable String id, Pageable pageable) {
        return userService.findOne(id).map(user -> {

                    Page<DepositOrder> depositOrders = depositOrderService.findByUserId(user.getId(), pageable);

                    return page()
                            .addComponent(new VerticallyDividedGrid().setVerticallyPadded()
                                    .addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN)
                                            .addComponent(new Buttons()
                                                    .addButton(new Button("编辑用户角色", QXCMP_BACKEND_URL + "/user/" + id + "/role", AnchorTarget.BLANK).setBasic().setSecondary())
                                                    .addButton(new Button("编辑用户状态", QXCMP_BACKEND_URL + "/user/" + id + "/status", AnchorTarget.BLANK).setBasic().setSecondary())
                                            )))
                                    .addItem(new Row()
                                            .addCol(new Col().setComputerWide(Wide.FIVE).setMobileWide(Wide.SIXTEEN)
                                                    .addComponent(new ContentHeader("基本资料", Size.NONE).setDividing())
                                                    .addComponent(convertToTable(stringObjectMap -> {
                                                        stringObjectMap.put("用户名", user.getUsername());
                                                        stringObjectMap.put("邮箱", user.getEmail());
                                                        stringObjectMap.put("手机", user.getPhone());
                                                        stringObjectMap.put("真实姓名", user.getName());
                                                        stringObjectMap.put("昵称", user.getNickname());
                                                        stringObjectMap.put("性别", user.getSex());
                                                        stringObjectMap.put("生日", user.getBirthday());
                                                        stringObjectMap.put("语言", user.getLanguage());
                                                        stringObjectMap.put("城市", user.getCity());
                                                        stringObjectMap.put("省份", user.getProvince());
                                                        stringObjectMap.put("国家", user.getCountry());
                                                        stringObjectMap.put("个性签名", user.getPersonalizedSignature());
                                                        stringObjectMap.put("备注", user.getRemark());
                                                    })))
                                            .addCol(new Col().setComputerWide(Wide.SIX).setMobileWide(Wide.SIXTEEN)
                                                    .addComponent(new Image(user.getPortrait()).setCentered().setCircular().setSize(Size.SMALL))
                                                    .addComponent(new ContentHeader("账户资料", Size.NONE).setDividing())
                                                    .addComponent(convertToTable(stringObjectMap -> {
                                                        stringObjectMap.put("UUID", user.getId());
                                                        stringObjectMap.put("OpenID", user.getOpenID());
                                                        stringObjectMap.put("UnionID", user.getUnionId());
                                                        stringObjectMap.put("上次登录时间", user.getDateLogin());
                                                        stringObjectMap.put("拥有角色", new CollectionValueCell(user.getRoles(), "name"));
                                                        stringObjectMap.put("账户是否过期", !user.isAccountNonExpired());
                                                        stringObjectMap.put("账户是否锁定", !user.isAccountNonLocked());
                                                        stringObjectMap.put("账户密码是否过期", !user.isCredentialsNonExpired());
                                                        stringObjectMap.put("账户是否可用", !user.isEnabled());
                                                    })))
                                            .addCol(new Col().setComputerWide(Wide.FIVE).setMobileWide(Wide.SIXTEEN)
                                                    .addComponent(new ContentHeader("充值记录", Size.NONE).setDividing())
                                                    .addComponent(convertToTable(objectObjectMap -> {
                                                        objectObjectMap.put("完成时间", "充值金额");
                                                        depositOrders.forEach(depositOrder -> objectObjectMap.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(depositOrder.getDateFinished()), new DecimalFormat("￥0.00").format((double) depositOrder.getFee() / 100)));
                                                    }))
                                                    .addComponent(new Pagination("", depositOrders.getNumber() + 1, (int) depositOrders.getTotalElements(), depositOrders.getSize()))
                                            )
                                    )
                            )
                            .setBreadcrumb("控制台", "", "用户管理", "user", "用户详情")
                            .build();
                }
        ).orElse(page(new Overview(new IconHeader("用户不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/user")).build());
    }

    @GetMapping("/{id}/role")
    public ModelAndView userRolePage(@PathVariable String id, final AdminUserRoleForm form) {
        return userService.findOne(id).map(user -> {
            form.setUsername(user.getUsername());
            form.setRoles(user.getRoles());
            return page()
                    .addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                    .setBreadcrumb("控制台", "", "用户管理", "user", "用户详情", "user/" + id + "/details", "编辑用户角色")
                    .addObject("selection_items_roles", roleService.findAll())
                    .build();
        }).orElse(page(new Overview(new IconHeader("用户不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/user")).build());
    }

    @PostMapping("/{id}/role")
    public ModelAndView userRolePage(@PathVariable String id, @Valid final AdminUserRoleForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "用户管理", "user", "用户详情", "user/" + id + "/details", "编辑用户角色")
                    .addObject("selection_items_roles", roleService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {
                userService.update(id, user -> user.setRoles(form.getRoles()))
                        .ifPresent(user -> applicationContext.publishEvent(new AdminUserRoleEditEvent(currentUser().orElseThrow(RuntimeException::new), user)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/user/" + id + "/details"));
    }

    @GetMapping("/{id}/status")
    public ModelAndView userStatusPage(@PathVariable String id, final AdminUserStatusForm form) {
        return userService.findOne(id).map(user -> {

            form.setUsername(user.getUsername());
            form.setDisabled(!user.isEnabled());
            form.setLocked(!user.isAccountNonLocked());
            form.setExpired(!user.isAccountNonExpired());
            form.setCredentialExpired(!user.isCredentialsNonExpired());

            return page()
                    .addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                    .setBreadcrumb("控制台", "", "用户管理", "user", "用户详情", "user/" + id + "/details", "编辑用户状态")
                    .build();
        }).orElse(page(new Overview(new IconHeader("用户不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/user")).build());
    }

    @PostMapping("/{id}/status")
    public ModelAndView userStatusPage(@PathVariable String id, @Valid final AdminUserStatusForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "用户管理", "user", "用户详情", "user/" + id + "/details", "编辑用户状态")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                userService.update(id, user -> {
                    user.setEnabled(!form.isDisabled());
                    user.setAccountNonLocked(!form.isLocked());
                    user.setAccountNonExpired(!form.isExpired());
                    user.setCredentialsNonExpired(!form.isCredentialExpired());
                })
                        .ifPresent(user -> applicationContext.publishEvent(new AdminUserStatusEditEvent(currentUser().orElseThrow(RuntimeException::new), user)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/user/" + id + "/details"));
    }
}
