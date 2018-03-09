package com.qxcmp.message;

import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.core.entity.EntityService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Aaric
 */
@Service
@RequiredArgsConstructor
public class FeedService extends AbstractEntityService<Feed, Long, FeedRepository> {

    private final UserService userService;
    private final SiteService siteService;

    /**
     * 为一个权限组的用户生成动态
     *
     * @param privilege 权限
     * @param target    动态产生用户
     * @param consumer  动态操作函数
     */
    public void feedForUsers(String privilege, User target, Consumer<Feed> consumer) {
        List<User> users = userService.findByAuthority(privilege);
        users.add(target);
        users.forEach(user -> {
            Feed feed = next();
            consumer.accept(feed);
            feed.setId(null);
            feed.setOwner(user.getId());
            feed.setTarget(target);
            feed.setDateCreated(DateTime.now().toDate());
            create(feed);
        });
    }

    public <T, ID extends Serializable> void feedForEntityCreate(EntityCreateEvent<T> event, EntityService<T, ID> entityService, String privilege, String title, String url, String titleField) {
        T entity = event.getEntity();
        BeanWrapperImpl entityBean = new BeanWrapperImpl(entity);
        feedForUsers(privilege, event.getUser(), feed -> {
            String entityName = "未知" + title;
            try {
                Object value = entityBean.getPropertyValue(titleField);
                if (Objects.nonNull(value)) {
                    entityName = value.toString();
                }
            } catch (Exception ignored) {

            }
            feed.setContent(String.format("%s 新建了 %s <a href='https://%s'>%s</a>",
                    event.getUser().getDisplayName(),
                    title,
                    siteService.getDomain() + url + "/" + entityService.getEntityId(entity) + "/edit",
                    entityName));
        });
    }

    /**
     * 查询用户的Feed流
     *
     * @param userId 用户ID
     *
     * @return 用户Feed流列表
     */
    public Page<Feed> findByOwner(String userId, Pageable pageable) {
        return repository.findByOwnerAndDateCreatedAfterOrderByDateCreatedDesc(userId, DateTime.now().minusDays(30).toDate(), pageable);
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
