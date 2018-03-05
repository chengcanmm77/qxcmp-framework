package com.qxcmp.security;

import com.qxcmp.audit.ActionException;
import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 权限服务
 *
 * @author aaric
 */
@Service
public class PrivilegeService extends AbstractEntityService<Privilege, Long, PrivilegeRepository> {

    public Optional<Privilege> create(String name, String description) {
        if (repository.findByName(name).isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(repository.save(new Privilege(name, description)));
        }

    }

    public Optional<Privilege> findByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Privilege> findOne(String id) {
        try {
            return findOne(Long.parseLong(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void enable(String id) throws ActionException {
        try {
            Long pId = Long.parseLong(id);

            Optional<Privilege> privilege = repository.findById(pId);

            if (privilege.isPresent()) {
                if (privilege.get().isDisabled()) {
                    privilege.get().setDisabled(false);
                    repository.save(privilege.get());
                } else {
                    throw new ActionException("Privilege already enabled");
                }
            } else {
                throw new ActionException("Privilege not exist");
            }

        } catch (NumberFormatException e) {
            throw new ActionException("Privilege not exist");
        }
    }

    public void disable(String id) throws ActionException {
        try {
            Long pId = Long.parseLong(id);

            Optional<Privilege> privilege = repository.findById(pId);

            if (privilege.isPresent()) {
                if (!privilege.get().isDisabled()) {
                    privilege.get().setDisabled(true);
                    repository.save(privilege.get());
                } else {
                    throw new ActionException("Privilege already disabled");
                }
            } else {
                throw new ActionException("Privilege not exist");
            }

        } catch (NumberFormatException e) {
            throw new ActionException("Privilege not exist");
        }
    }

}
