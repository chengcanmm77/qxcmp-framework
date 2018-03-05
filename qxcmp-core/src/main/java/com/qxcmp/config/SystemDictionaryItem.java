package com.qxcmp.config;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * 系统字典配置子项
 *
 * @author aaric
 */
@Entity
@Table
@Data
@ToString(exclude = "parent")
public class SystemDictionaryItem {

    /**
     * 主键，由框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 顺序
     */
    private int priority;

    @ManyToOne
    private SystemDictionary parent;
}
