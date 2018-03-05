package com.qxcmp.shoppingmall;

import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableAction;
import com.qxcmp.web.view.annotation.table.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 商城店铺实体
 *
 * @author Aaric
 */
@EntityTable(value = "店铺管理", name = "store", action = QXCMP_BACKEND_URL + "/mall/store",
        tableActions = @TableAction(value = "创建店铺", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "编辑", action = "edit")
        })
@Entity
@Table
@Data
public class Store {

    @Id
    private String id;

    @TableField(value = "封面", image = true)
    private String cover;

    @TableField("店铺名称")
    @Column(unique = true)
    private String name;

    @ManyToOne
    @TableField(value = "所有者", name = "store", fieldSuffix = ".username")
    private User owner;

    @TableField("创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateCreated;

    private Date dateModified;

    /**
     * 是否为外部店铺
     * <p>
     * 外部店铺名称将会显示在商品页面
     */
    @TableField(value = "是否为外部店铺", name = "store")
    private boolean external;
}
