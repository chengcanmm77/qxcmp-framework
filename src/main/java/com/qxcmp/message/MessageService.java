package com.qxcmp.message;

import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 消息服务总接口
 * <p>
 * 该服务用户向用户发送消息
 *
 * @author Aaric
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final FeedService feedService;

    /**
     * 为用户组中的每一个用户生成一个Feed
     *
     * @param userIds 用户组用户ID列表
     * @param target  Feed目标用户
     * @param content Feed内容
     */
    public void feed(Collection<String> userIds, User target, String content) {
        feed(userIds, target, content, "");
    }

    /**
     * 为用户组中的每一个用户生成一个Feed
     *
     * @param userIds      用户组用户ID列表
     * @param target       Feed目标用户
     * @param content      Feed内容
     * @param extraContent Feed额外文本
     */
    public void feed(Collection<String> userIds, User target, String content, String extraContent) {
        userIds.stream().distinct().forEach(s -> feed(feed -> {
            feed.setOwner(s);
            feed.setTarget(target);
            feed.setContent(content);
            feed.setExtraContent(extraContent);
        }));
    }

    /**
     * 为用户生成一个Feed
     * <p>
     * 必须指定Feed Owner
     * 不用指定Feed 时间
     *
     * @param feed 要生成的Feed
     */
    public void feed(Feed feed) {
        feed.setId(null);
        feed.setDateCreated(DateTime.now().toDate());
        feedService.save(feed);
    }

    /**
     * 为用户生成一个Feed
     * <p>
     * 必须指定Feed Owner
     * 不用指定Feed 时间
     *
     * @param feedConsumer Feed消费者
     */
    public void feed(Consumer<Feed> feedConsumer) {
        Feed feed = feedService.next();
        feedConsumer.accept(feed);
        feed(feed);
    }
}
