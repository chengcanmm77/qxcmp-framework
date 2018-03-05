package com.qxcmp.message;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 短信模板实体
 */
@Entity
@Table
@Data
public class SmsTemplate {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 短信业务名称
     */
    private String title;

    /**
     * 业务对应短信模板代码
     */
    private String template;
}
