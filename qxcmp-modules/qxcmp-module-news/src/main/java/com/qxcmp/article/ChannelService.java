package com.qxcmp.article;

import com.google.common.collect.Lists;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 文章栏目服务
 *
 * @author aaric
 */
@Service
public class ChannelService extends AbstractEntityService<Channel, Long, ChannelRepository> {

    /**
     * 获取用户拥有的所有栏目
     *
     * @param user     用户
     * @param pageable 分页查询
     *
     * @return 用户拥有的所有栏目
     */
    public Page<Channel> findByUser(User user, Pageable pageable) {
        return repository.findByUser(user, pageable);
    }

    public List<Channel> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Channel> findByOwner(User user) {
        return repository.findByOwner(user);
    }

    public List<Channel> findByAdmin(User user) {
        return repository.findByAdminsContains(user);
    }

    /**
     * 获取用户ID拥有的栏目
     * <p>
     * 当用户为栏目所有者或者管理员的时候将会返回该栏目
     *
     * @param user 用户ID
     *
     * @return 用户栏目列表
     */
    public List<Channel> findByUserId(User user) {
        List<Channel> channels = Lists.newArrayList(repository.findByOwner(user));
        channels.addAll(repository.findByAdminsContains(user));
        return channels;
    }

    public List<Channel> findByCatalog(String catalog) {
        return repository.findByCatalog(catalog);
    }

    public List<Channel> findByCatalogContains(Set<String> catalogs) {
        return repository.findByCatalogContains(catalogs);
    }

    public Optional<Channel> findByName(String name) {
        return repository.findByName(name);
    }

}
