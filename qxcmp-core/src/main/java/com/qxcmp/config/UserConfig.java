package com.qxcmp.config;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户配置
 * <p>
 * 用于保存用于个性化配置
 *
 * @author aaric
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "name"}))
@Data
public class UserConfig {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 配置所对应的用户Id
     */
    private String userId;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置值
     */
    private String value;

    /**
     * 创建日期
     */
    private Date dateCreated = new Date();

    /**
     * 修改日期
     */
    private Date dateModified = new Date();
}
