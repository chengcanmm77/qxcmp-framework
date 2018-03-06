package com.qxcmp.config;

import com.qxcmp.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统配置实体定义
 * <p>
 * 保存了平台需要的配置信息
 * <p>
 * 可以使用{@link QxcmpSystemConfig}标注的Spring Bean进行自动创建
 * <p>
 * 可以使用{@link SystemConfigService}对系统配置进行读取和修改
 *
 * @author aaric
 * @see SystemConfigService
 * @see QxcmpSystemConfig
 */
@Entity
@Table
@Data
public class SystemConfig {

    /**
     * 系统配置主键
     * <p>
     * 标识了一个唯一的平台系统配置
     */
    @Id
    private String name;

    /**
     * 系统配置描述信息
     * <p>
     * 对该配置的相应描述
     */
    private String description;

    /**
     * 系统配置的值
     * <p>
     * 系统配置的值以字符串的形式储存在数据库中，如果要转换为其他原始类型，请参考{@link SystemConfigService}
     */
    private String value;

    /**
     * 配置的创建日期
     */
    private Date dataCreated = new Date();

    /**
     * 配置的修改日期
     */
    private Date dateModified = new Date();

    /**
     * 配置的修改人
     */
    @ManyToOne
    private User userModified;
}
