package com.qxcmp.finance;

import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.image.Avatar;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 平台充值订单
 * <p>
 * 使用 {@link DepositOrderService} 来创建订单，如果收到充值资金以后使用 {@link DepositOrderService#process(String)} 来处理该订单
 *
 * @author aaric
 */
@EntityTable(value = "充值订单")
@Entity
@Table
@Data
public class DepositOrder {

    /**
     * 订单号，由平台自动生成
     */
    @Id
    private String id;

    /**
     * 额外列用于展示用户头像
     */
    @Transient
    @TableField("头像")
    private String portrait;

    /**
     * 订单对应的用户ID
     */
    @TableField(value = "用户ID")
    private String userId;

    /**
     * 订单类型，用户业务拓展
     */
    @TableField("类型")
    private String type;

    /**
     * 订单金额，单位为分
     */
    @TableField("充值金额")
    private int fee;

    /**
     * 货币类型，默认为CNY、即人民币
     * <p>
     * 具体值参考 <a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">货币类型</a>
     */
    private String feeType = "CNY";

    /**
     * 订单描述
     */
    private String description;

    /**
     * 订单起始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeStart;

    /**
     * 订单结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    /**
     * 订单完成时间
     */
    @TableField(value = "完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateFinished;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    @TableFieldRender("fee")
    public TableData renderFeeFiled() {
        return new TableData(new DecimalFormat("￥0.00").format((double) fee / 100));
    }

    @TableFieldRender("userId")
    public TableData renderUserField(UserService userService) {
        User user = userService.findOne(userId).orElse(userService.next());
        return new TableData(new Anchor(user.getDisplayName(), QXCMP_ADMIN_URL + "/user/" + userId + "/details", AnchorTarget.BLANK.toString()));
    }

    @TableFieldRender("portrait")
    public TableData renderUserPortrait(UserService userService) {
        User user = userService.findOne(userId).orElse(userService.next());
        TableData tableData = new TableData();
        tableData.setCollapsing().addComponent(new Avatar(user.getPortrait()).setCentered());
        return tableData;
    }
}
