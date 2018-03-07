package com.qxcmp.statistics;

import com.qxcmp.web.view.annotation.table.EntityTable;
import com.qxcmp.web.view.annotation.table.RowAction;
import com.qxcmp.web.view.annotation.table.TableField;
import com.qxcmp.web.view.annotation.table.TableFieldRender;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Alignment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * 访问地址实体
 * <p>
 * 该实体可用于定义IP黑白名单
 * <p>
 * 用户可以通过后台管理访问地址
 * <p>
 * 第三方业务可以调用相应接口进行业务逻辑处理
 *
 * @author Aaric
 */
@EntityTable(value = "访问地址管理", action = QXCMP_ADMIN_URL + "/statistic/access/address", entityIndex = "address",
        rowActions = {
                @RowAction(value = "普通", action = "normal", method = FormMethod.POST),
                @RowAction(value = "黑名单", action = "black", method = FormMethod.POST),
                @RowAction(value = "白名单", action = "white", method = FormMethod.POST),
                @RowAction(value = "爬虫", action = "spider", method = FormMethod.POST)
        })
@Entity
@Table
@Data
public class AccessAddress {

    @Id
    @TableField("地址")
    private String address;

    @TableField("类型")
    private AccessAddressType type = AccessAddressType.NORMAL;

    @TableField("最近访问时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @TableFieldRender("type")
    public TableData renderTypeField() {
        final TableData tableData = new TableData();

        switch (type) {
            case BLACK:
                tableData.setContent("黑名单");
                break;
            case WHITE:
                tableData.setContent("白名单");
                break;
            case SPIDER:
                tableData.setContent("网络蜘蛛");
                break;
            default:
                tableData.setContent("普通地址");
        }

        tableData.setAlignment(Alignment.CENTER);
        return tableData;
    }
}
