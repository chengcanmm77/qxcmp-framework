package com.qxcmp.core.entity;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.qxcmp.core.support.ReflectionUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * 实体服务抽象实现
 *
 * @author aaric
 * @see EntityService
 */
public abstract class AbstractEntityService<T, ID extends Serializable, R extends JpaRepository<T, ID> & JpaSpecificationExecutor<T>> implements EntityService<T, ID> {

    protected R repository;

    private TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
    };

    @Override
    public Class<? super T> type() {
        return typeToken.getRawType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        try {
            return (T) typeToken.getRawType().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ID getEntityId(T entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (Objects.nonNull(field.getAnnotation(Id.class))) {
                field.setAccessible(true);
                try {
                    return (ID) field.get(entity);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        throw new IllegalStateException("No @Id definition in entity class");
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long count(Example<T> example) {
        return repository.count(example);
    }

    @Override
    public long count(Specification<T> specification) {
        return repository.count(specification);
    }

    @Override
    public boolean exist(ID id) {
        return Objects.nonNull(id) && repository.existsById(id);
    }

    @Override
    public boolean exist(Example<T> example) {
        return repository.exists(example);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> iterable) {
        return repository.saveAll(iterable);
    }

    @Override
    public T create(T entity) {
        checkNotNull(entity, "Entity is null");
        checkState(!exist(getEntityId(entity)), "Entity already exist");
        return repository.save(entity);
    }

    @Override
    public T create(Supplier<T> supplier) {
        return create(supplier.get());
    }

    @Override
    public List<T> createAll(Iterable<T> iterable) {
        List<T> filtered = Lists.newArrayList();

        iterable.forEach(t -> {
            if (!exist(getEntityId(t))) {
                filtered.add(t);
            }
        });

        return saveAll(filtered);
    }

    @Override
    public T update(ID id, Consumer<T> consumer) {
        return findOne(id).map(t -> update(t, consumer)).orElseThrow(() -> new IllegalStateException("Entity not exist"));
    }

    @Override
    public T update(Example<T> example, Consumer<T> consumer) {
        return findOne(example).map(t -> update(t, consumer)).orElseThrow(() -> new IllegalStateException("Entity not exist"));
    }

    @Override
    public T update(Specification<T> specification, Consumer<T> consumer) {
        return findOne(specification).map(t -> update(t, consumer)).orElseThrow(() -> new IllegalStateException("Entity not exist"));
    }

    @Override
    public List<T> updateAll(Iterable<ID> iterable, BiConsumer<ID, T> biConsumer) {
        return findAll(iterable).stream().map(t -> update(t, biConsumer)).collect(Collectors.toList());
    }

    @Override
    public List<T> updateAll(Example<T> example, BiConsumer<ID, T> biConsumer) {
        return findAll(example).stream().map(t -> update(t, biConsumer)).collect(Collectors.toList());
    }

    @Override
    public List<T> updateAll(Specification<T> specification, BiConsumer<ID, T> biConsumer) {
        return findAll(specification).stream().map(t -> update(t, biConsumer)).collect(Collectors.toList());
    }

    @Override
    public Optional<T> findOne(ID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<T> findOne(Example<T> example) {
        return repository.findOne(example);
    }

    @Override
    public Optional<T> findOne(Specification<T> specification) {
        return repository.findOne(specification);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Iterable<ID> iterable) {
        return repository.findAllById(iterable);
    }

    @Override
    public List<T> findAll(Example<T> example) {
        return repository.findAll(example);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return repository.findAll(specification);
    }

    @Override
    public List<T> findAll(Specification<T> specification, Sort sort) {
        return repository.findAll(specification, sort);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<T> iterable) {
        repository.deleteAll(iterable);
    }

    @Override
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteInBatch(Iterable<T> iterable) {
        repository.deleteInBatch(iterable);
    }

    @Override
    public void mergeToEntity(Object object, T entity) {
        BeanWrapperImpl formBean = new BeanWrapperImpl(object);
        BeanWrapperImpl entityBean = new BeanWrapperImpl(entity);
        ReflectionUtils.getAllFields(object.getClass()).stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        entityBean.setPropertyValue(fieldName, formBean.getPropertyValue(fieldName));
                    } catch (Exception ignored) {

                    }
                });
    }

    @Override
    public void mergeToObject(T entity, Object object) {
        BeanWrapperImpl entityBean = new BeanWrapperImpl(entity);
        BeanWrapperImpl formBean = new BeanWrapperImpl(object);
        ReflectionUtils.getAllFields(object.getClass()).stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        formBean.setPropertyValue(fieldName, entityBean.getPropertyValue(fieldName));
                    } catch (Exception ignored) {

                    }
                });
    }

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    private T update(T entity, Consumer<T> consumer) {
        ID originId = getEntityId(entity);
        consumer.accept(entity);
        checkState(Objects.equals(originId, getEntityId(entity)), "Entity id is changed when update");
        return repository.save(entity);
    }

    private T update(T entity, BiConsumer<ID, T> biConsumer) {
        ID originId = getEntityId(entity);
        biConsumer.accept(originId, entity);
        checkState(Objects.equals(originId, getEntityId(entity)), "Entity id is changed when update");
        return repository.save(entity);
    }
}
