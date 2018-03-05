package com.qxcmp.topic;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Aaric
 */
@Service
public class TopicService extends AbstractEntityService<Topic, String, TopicRepository> {

    /**
     * 为一个主题增加一个订阅者
     * <p>
     * 如果主题不存在则创建该主题
     *
     * @param topicId    主题ID
     * @param subscriber 订阅者
     */
    public void addSubscriber(String topicId, String subscriber) {
        Topic topic = findOne(topicId).orElse(new Topic());
        topic.getSubscriber().add(subscriber);
        save(topic);
    }

    /**
     * 为一个主题增加一个订阅者
     *
     * @param topicId     主题ID
     * @param subscribers 订阅者列表
     */
    public void addSubscriber(String topicId, Collection<String> subscribers) {
        Topic topic = findOne(topicId).orElse(new Topic());
        topic.getSubscriber().addAll(subscribers);
        save(topic);
    }

    /**
     * 为一个主题移除一个订阅者
     * <p>
     * 如果主题不存在则不执行任何操作
     *
     * @param topicId    主题ID
     * @param subscriber 订阅者
     */
    public void removeSubscriber(String topicId, String subscriber) {
        update(topicId, topic -> topic.getSubscriber().remove(subscriber));
    }

    /**
     * 为一个主题移除一些订阅者
     * <p>
     * 如果主题不存在则不执行任何操作
     *
     * @param topicId     主题ID
     * @param subscribers 订阅者列表
     */
    public void removeSubscriber(String topicId, Collection<String> subscribers) {
        update(topicId, topic -> topic.getSubscriber().removeAll(subscribers));
    }

    /**
     * 获取一个主题的所有订阅者
     * <p>
     * 如果主题不存在返回空列表
     *
     * @param topicId 主题ID
     *
     * @return 主题对应的订阅者列表
     */
    public List<String> getSubscribers(String topicId) {
        return findOne(topicId).map(Topic::getSubscriber).orElse(Collections.emptyList());
    }

}
