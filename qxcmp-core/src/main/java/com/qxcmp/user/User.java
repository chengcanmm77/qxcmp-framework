package com.qxcmp.user;

import com.google.common.collect.Sets;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.core.validation.Phone;
import com.qxcmp.core.validation.Username;
import com.qxcmp.security.Role;
import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 用户实体定义
 * <p>
 * 该实体定义了平台的用户，包含了用户的注册信息，个人信息和权限相关的信息
 *
 * @author aaric
 */
@EntityTable(value = "全部用户", name = "all", action = QXCMP_ADMIN_URL + "/user/all",
        rowActions = {@RowAction(value = "查看", action = "details", primary = true)})
@EntityTable(value = "微信用户", name = "weixin", action = QXCMP_ADMIN_URL + "/user/weixin", disableFilter = true,
        tableActions = @TableAction(value = "开始同步", action = "sync", method = FormMethod.POST, primary = true),
        rowActions = {@RowAction(value = "查看", action = "details", primary = true)})
@Entity
@Table
@Data
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    /**
     * 用户平台ID，由平台自动生成
     * <p>
     * 采用默认主键生成算法 {@link IDGenerator#next()}
     */
    @Id
    private String id;

    /**
     * 用户名，唯一，可用于登录使用
     */
    @Username
    @Column(length = 30, unique = true)
    @TableField("用户名")
    private String username;

    /**
     * 用户绑定邮箱，唯一，可用于登录使用
     */
    @Email
    @Column(length = 60, unique = true)
    private String email;

    /**
     * 用户绑定手机号，唯一，可用于登录使用
     */
    @Phone
    @Column(length = 20, unique = true)
    private String phone;

    /**
     * 用户公众号OpenID，用于与微信集成，单公众号时使用
     */
    @Column(unique = true)
    private String openID;

    /**
     * 用户多公众号之间统一UnionId，用于多公众号开发时使用
     */
    @Column(unique = true)
    private String unionId;

    /**
     * 用户角色，用户的角色角色了用户的权限
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles = Sets.newHashSet();

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码修改历史
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> passwordHistory = Sets.newHashSet();

    /**
     * 用户支付密码
     * <p>
     * 支付密码默认为空，且无法支付
     * <p>
     * 用户需要自行设置支付密码
     */
    private String payPassword;

    /**
     * 支付密码修改历史
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> payPasswordHistory = Sets.newHashSet();

    /**
     * 用户账户是否未过期
     */
    private boolean accountNonExpired;

    /**
     * 用户账户是否被锁定
     */
    private boolean accountNonLocked;

    /**
     * 用户密码是否未过期
     */
    private boolean credentialsNonExpired;

    /**
     * 用户账户是否可用
     */
    private boolean enabled;

    /**
     * 账户锁定时间
     */
    private Date dateLock = new Date();

    /**
     * 上次登陆时间
     */
    @TableField(value = "登录时间", name = "weixin")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLogin = new Date();

    /**
     * 账户创建时间
     */
    @TableField(value = "注册时间", name = "all")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated = new Date();

    /**
     * 上一次密码修改时间
     */
    private Date datePasswordModified = new Date();

    /**
     * 用户真实姓名
     */
    private String name;

    /**
     * 用户昵称
     */
    @TableField(value = "昵称", name = "weixin")
    private String nickname;

    /**
     * 用户头像URL
     */
    @TableField(value = "头像", image = true, order = Integer.MIN_VALUE)
    private String portrait;

    /**
     * 用户是否订阅平台所属的公众号
     */
    @TableField(value = "是否订阅", name = "weixin")
    private boolean subscribe;

    /**
     * 用户性别
     */
    @TableField(value = "性别", name = "weixin")
    private String sex;

    /**
     * 用户所使用语言
     */
    @TableField(value = "语言", name = "weixin")
    private String language;

    /**
     * 用户所在城市
     */
    @TableField(value = "城市", name = "weixin")
    private String city;

    /**
     * 用户所在省份
     */
    @TableField(value = "省份", name = "weixin")
    private String province;

    /**
     * 用户所在国家
     */
    @TableField(value = "国家", name = "weixin")
    private String country;

    /**
     * 用户关注平台公众号时间
     */
    private Long subscribeTime;

    /**
     * 用户性别代码
     */
    private Integer sexId;

    /**
     * 用户备注信息
     */
    private String remark;

    /**
     * 用户组ID
     */
    private Integer groupId;

    /**
     * 用户标签ID
     */
    private Long[] tagIds;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 用户个性签名
     */
    private String personalizedSignature;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 用户介绍
     */
    private String description;

    /**
     * 用户标签
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> labels = Sets.newHashSet();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = Sets.newHashSet();
        roles.forEach(role -> role.getPrivileges().forEach(privilege -> {
            if (!privilege.isDisabled()) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + privilege.getName()));
            }
        }));
        return grantedAuthorities;
    }

    /**
     * 从真实姓名、昵称、用户名中获取
     *
     * @return 用户显示名称
     */
    public String getDisplayName() {
        if (StringUtils.isNotBlank(name)) {
            return name;
        }

        if (StringUtils.isNotBlank(nickname)) {
            return nickname;
        }

        return username;
    }

    @TableFieldRender("subscribe")
    public TableData renderSubscribeField() {
        final TableData tableData = new TableData();
        if (subscribe) {
            tableData.addComponent(new Icon("check circle").setColor(Color.GREEN));
        } else {
            tableData.addComponent(new Icon("warning circle").setColor(Color.RED));
        }
        return tableData;
    }
}
