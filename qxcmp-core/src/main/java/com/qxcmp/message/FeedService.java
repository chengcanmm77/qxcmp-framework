package com.qxcmp.message;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Aaric
 */
@Service
@RequiredArgsConstructor
public class FeedService extends AbstractEntityService<Feed, Long, FeedRepository> {

    private final UserService userService;

    /**
     * 为用户一种权限的用户生成动态
     *
     * @param privilege 权限
     * @param target    产生动态的用户
     * @param content   动态主要内容
     */
    public void feedForUserGroup(String privilege, User target, String content) {
        feedForUserGroup(privilege, target, content, "");
    }

    /**
     * 为用户一种权限的用户生成动态
     *
     * @param privilege    权限
     * @param target       产生动态的用户
     * @param content      动态主要内容
     * @param extraContent 动态次要内容
     */
    public void feedForUserGroup(String privilege, User target, String content, String extraContent) {
        feedForUserGroup(ImmutableSet.of(privilege), target, content, extraContent);
    }

    /**
     * 为包含指定权限的用户生成动态
     *
     * @param privileges 权限集，当用户拥有其中任何一个权限的时候满足
     * @param target     产生动态的用户
     * @param content    动态主要内容
     */
    public void feedForUserGroup(Set<String> privileges, User target, String content) {
        feedForUserGroup(privileges, target, content, "");
    }

    /**
     * 为包含指定权限的用户生成动态
     *
     * @param privileges   权限集，当用户拥有其中任何一个权限的时候满足
     * @param target       产生动态的用户
     * @param content      动态主要内容
     * @param extraContent 动态次要内容
     */
    public void feedForUserGroup(Set<String> privileges, User target, String content, String extraContent) {
        List<User> users = userService.findByAuthorityContains(privileges);
        users.add(target);
        feedForUsers(users, target, content, extraContent);
    }

    /**
     * 为用户生成动态
     *
     * @param users   要生成动态的用户
     * @param target  产生动态的用户
     * @param content 动态次要内容
     */
    public void feedForUsers(Iterable<User> users, User target, String content) {
        feedForUsers(users, target, content, "");
    }

    /**
     * 为用户生成动态
     * <p>
     * 会取消重复的用户
     *
     * @param users        要生成动态的用户
     * @param target       产生动态的用户
     * @param content      动态主要内容
     * @param extraContent 动态次要内容
     */
    public void feedForUsers(Iterable<User> users, User target, String content, String extraContent) {
        Sets.newHashSet(users).forEach(user -> {
            Feed next = next();
            next.setId(null);
            next.setOwner(user.getId());
            next.setTarget(target);
            next.setDateCreated(DateTime.now().toDate());
            next.setContent(content);
            next.setExtraContent(extraContent);
            create(next);
        });
    }

    /**
     * 生成某一类型的动态，该类动态对所有用户生效
     *
     * @param type    动态类型
     * @param target  产生动态的用户
     * @param content 动态主要内容
     */
    public void feedForType(String type, User target, String content) {
        feedForType(type, target, content, "");
    }

    /**
     * 生成某一类型的动态，该类动态对所有用户生效
     *
     * @param type         动态类型
     * @param target       产生动态的用户
     * @param content      动态主要内容
     * @param extraContent 动态次要内容
     */
    public void feedForType(String type, User target, String content, String extraContent) {
        Feed next = next();
        next.setId(null);
        next.setType(type);
        next.setTarget(target);
        next.setDateCreated(DateTime.now().toDate());
        next.setContent(content);
        next.setExtraContent(extraContent);
        create(next);
    }

    /**
     * 查询用户的Feed流
     *
     * @param userId 用户ID
     *
     * @return 用户Feed流列表
     */
    public Page<Feed> findByOwner(String userId, Pageable pageable) {
        return repository.findByOwnerOrderByDateCreatedDesc(userId, pageable);
    }

    /**
     * 查询某一类型的动态
     *
     * @param type     动态类型
     * @param pageable 分页信息
     *
     * @return 查询结果
     */
    public Page<Feed> findByType(String type, Pageable pageable) {
        return repository.findByTypeOrderByDateCreatedDesc(type, pageable);
    }

}
