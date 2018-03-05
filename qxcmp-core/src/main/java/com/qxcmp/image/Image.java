package com.qxcmp.image;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * 图片实体
 * <p>
 * 用于保存平台所有图片内容
 *
 * @author aaric
 * @see ImageService
 */
@Entity
@Table
@Data
public class Image {

    /**
     * 图片主键，由平台自动生成
     */
    @Id
    private String id;

    /**
     * 图片类型
     */
    private String type;

    /**
     * 图片内容
     */
    @Lob
    private byte[] content;

    /**
     * 图片描述
     */
    private String description;

    /**
     * 图片创建日期
     */
    private Date dateCreated;

}
