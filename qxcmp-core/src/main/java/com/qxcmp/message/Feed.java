package com.qxcmp.message;

import com.qxcmp.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Feed流
 * <p>
 * Feed的产生可源于主题订阅模型、事件等
 * <p>
 * 如后台发布了一篇文章，文章管理员，栏目管理员会生成Feed
 * 如果用户评论了一篇文章，文章作者已经该文章下所有的评论者都可以收到Feed
 *
 * @author Aaric
 */
@Entity
@Table
@Data
public class Feed {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Feed所有者
     */
    private String owner;

    /**
     * Feed相关的用户，产生该Feed事件的用户
     */
    @ManyToOne
    private User target;

    /**
     * Feed创建时间
     */
    private Date dateCreated;

    /**
     * Feed内容支持Html嵌入超链接
     */
    @Lob
    private String content;

    /**
     * Feed额外信息支持Html嵌入超链接
     */
    @Lob
    private String extraContent;

    /**
     * 类型
     * <p>
     * 用于非用户流的情况
     */
    private String type;

}
