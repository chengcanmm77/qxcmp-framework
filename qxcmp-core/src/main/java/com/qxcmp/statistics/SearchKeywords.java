package com.qxcmp.statistics;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 搜索关键字实体
 *
 * 用于保存用户搜索关键字信息
 *
 * @author Aaric
 */
@Entity
@Table
@Data
public class SearchKeywords {

    @Id
    @GeneratedValue
    private Long id;

    private Date dateCreated;

    private String title;

    private String userId;
}
