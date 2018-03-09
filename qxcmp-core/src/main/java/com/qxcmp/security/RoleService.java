package com.qxcmp.security;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 角色服务
 *
 * @author aaric
 */
@Service
public class RoleService extends AbstractEntityService<Role, Long, RoleRepository> {

    public Optional<Role> findOne(String id) {
        try {
            return findOne(Long.parseLong(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Role> findByName(String name) {
        return repository.findByName(name);
    }

}
