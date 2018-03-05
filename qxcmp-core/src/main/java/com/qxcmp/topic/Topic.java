package com.qxcmp.topic;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * 主题
 * <p>
 * 主题用于给订阅者发送消息
 *
 * @author Aaric
 */
@Entity
@Table
@Data
public class Topic {

    /**
     * 主题ID
     */
    @Id
    private String id;

    /**
     * 该主题的订阅者
     */
    @ElementCollection
    private List<String> subscriber;

}
