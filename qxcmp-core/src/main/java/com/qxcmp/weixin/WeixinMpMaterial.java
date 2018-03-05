package com.qxcmp.weixin;

import com.qxcmp.web.view.annotation.table.*;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.Alignment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * 微信素材实体
 *
 * @author Aaric
 */
@EntityTable(value = "公众号素材管理", action = QXCMP_BACKEND_URL + "/weixin/material", disableFilter = true,
        tableActions = @TableAction(value = "同步素材", action = "sync", method = FormMethod.POST, primary = true, showConfirmDialog = true, confirmDialogTitle = "微信素材同步", confirmDialogDescription = "素材同步将需要较长时间，点击是继续"),
        rowActions = @RowAction(value = "查看", action = "preview"))
@Entity
@Table
@Data
public class WeixinMpMaterial {

    /**
     * 素材主键
     * <p>
     * 图文素材为图文素材MediaId加上游标
     * <p>
     * 其他素材为图文素材MediaId
     */
    @Id
    private String id;

    /**
     * 素材类型
     */
    @TableField("素材类型")
    private WeixinMpMaterialType type;

    /**
     * 素材名称
     * <p>
     * 图文素材为图文标题
     * <p>
     * 其他素材为素材名称
     */
    @TableField("素材名称")
    private String title;

    private String thumbMediaId;

    @TableField(value = "封面", image = true, order = Integer.MIN_VALUE)
    private String thumbUrl;

    private boolean showCover;

    private String author;

    private String digest;

    @Lob
    private String content;

    /**
     * 图文Url或者图片Url
     */
    private String url;

    private String sourceUrl;

    @TableField("更新日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @TableFieldRender("type")
    public TableData renderTypeField() {
        final TableData tableData = new TableData();

        switch (type) {
            case NEWS:
                tableData.setContent("图文");
                break;
            case IMAGE:
                tableData.setContent("图片");
                break;
            case VOICE:
                tableData.setContent("语音");
                break;
            case VIDEO:
                tableData.setContent("视频");
                break;
            default:
                tableData.setContent("未知");
        }

        tableData.setAlignment(Alignment.CENTER);
        return tableData;
    }

    @RowActionCheck("查看")
    public boolean canPerformPreviewAction() {
        return type.equals(WeixinMpMaterialType.NEWS);
    }
}
