package com.qxcmp.core.entity;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 实体服务抽象接口，该接口的实现支持以下服务
 * <p>
 * 实体对象的增删改查操作 实体对象的排序和分页查询 实体对象的条件查询
 *
 * @param <T>  模块实体类型
 * @param <ID> 模块实体主键类型
 *
 * @author aaric
 */
public interface EntityService<T, ID extends Serializable> {

    /**
     * 获取实体的类型
     *
     * @return 获取该实体服务对应的类型
     */
    Class<? super T> type();

    /**
     * 工厂方法，用于创建新的实体实例
     *
     * @return 新的实例实例
     */
    T next();

    /**
     * 获取一个实体的主键
     *
     * @param entity 实体
     *
     * @return 实体的主键
     */
    ID getEntityId(T entity);

    /**
     * 获取实体的个数
     *
     * @return 实体的个数
     */
    long count();

    /**
     * 获取与样本匹配的实体个数
     *
     * @param example 实体样本
     *
     * @return 与样本匹配的实体个数
     */
    long count(Example<T> example);

    /**
     * 获取符合查询条件的实体个数
     *
     * @param specification 查询条件
     *
     * @return 符合查询条件的实体个数
     */
    long count(Specification<T> specification);

    /**
     * 判断主键对应的实体是否存在
     *
     * @param id 实体主键
     *
     * @return 是否存在
     */
    boolean exist(ID id);

    /**
     * 判断是否存在与样本相匹配的实体
     *
     * @param example 实体样本
     *
     * @return 是否匹配
     */
    boolean exist(Example<T> example);

    /**
     * 保存一个实体
     * <p>
     * 如果实体存在则更新该实体
     * 如果实体不存在则创建该实体
     *
     * @param entity 实体
     *
     * @return 保存后的实体
     */
    T save(T entity);

    /**
     * 保存一些实体
     *
     * @param iterable 要保存的实体
     *
     * @return 保存后的实体
     */
    List<T> saveAll(Iterable<T> iterable);

    /**
     * 创建一个实体对象
     *
     * @param entity 实体对象
     *
     * @return 创建后的实体对象
     *
     * @throws IllegalStateException 如果实体已经存在，则抛出该异常
     */
    T create(T entity);

    /**
     * 创建一个实体对象
     *
     * @param supplier 实体对象
     *
     * @return 创建后的实体对象
     *
     * @throws IllegalStateException 如果实体已经存在，则抛出该异常
     */
    T create(Supplier<T> supplier);

    /**
     * 创建所有实体对象，如果实体对象已经存在，则跳过该对象的创建
     *
     * @param iterable 实体集合
     *
     * @return 创建后的实体对象列表
     */
    List<T> createAll(Iterable<T> iterable);

    /**
     * 更新一个实体
     *
     * @param id       实体主键
     * @param consumer 更新操作
     *
     * @return 更新后的实体
     *
     * @throws IllegalStateException 如果更新的实体不存在抛出该异常
     */
    T update(ID id, Consumer<T> consumer);

    /**
     * 更新一个与样本匹配的实体
     *
     * @param example  样本
     * @param consumer 更新操作
     *
     * @return 更新后的实体
     *
     * @throws IllegalStateException 如果更新的实体不存在，或者存在多个实体抛出该异常
     */
    T update(Example<T> example, Consumer<T> consumer);

    /**
     * 更新一个与查询条件匹配的实体
     *
     * @param specification 查询条件
     * @param consumer      更新操作
     *
     * @return 更新后的实体
     *
     * @throws IllegalStateException 如果更新的实体不存在，或者存在多个实体抛出该异常
     */
    T update(Specification<T> specification, Consumer<T> consumer);

    /**
     * 更新所有包括主键的实体
     *
     * @param iterable   主键集合
     * @param biConsumer 更新操作
     *
     * @return 更新后的实体列表
     */
    List<T> updateAll(Iterable<ID> iterable, BiConsumer<ID, T> biConsumer);

    /**
     * 更新所有与样本匹配的实体
     *
     * @param example    样本
     * @param biConsumer 更新操作
     *
     * @return 更新后的实体列表
     */
    List<T> updateAll(Example<T> example, BiConsumer<ID, T> biConsumer);

    /**
     * 更新所有与查询条件匹配的实体
     *
     * @param specification 查询条件
     * @param biConsumer    更新操作
     *
     * @return 更新后的实体列表
     */
    List<T> updateAll(Specification<T> specification, BiConsumer<ID, T> biConsumer);


    /**
     * 根据主键查询一个实体
     *
     * @param id 主键
     *
     * @return 查询到的实体
     */
    Optional<T> findOne(ID id);

    /**
     * 查询一个与样本匹配的实体
     * <p>
     * 如果匹配结果超过一个，则抛出异常
     *
     * @param example 样本
     *
     * @return 与样本匹配的实体
     */
    Optional<T> findOne(Example<T> example);

    /**
     * 按条件查询某个实体
     * <p>
     * 如果匹配结果超过一个，则抛出异常
     *
     * @param specification 查询条件
     *
     * @return 符合要求的第一个实体
     */
    Optional<T> findOne(Specification<T> specification);

    /**
     * 查询所有实体
     *
     * @return 所有实体
     */
    List<T> findAll();

    /**
     * 查询所有实体并排序
     *
     * @param sort 排序方式
     *
     * @return 排序后的实体
     */
    List<T> findAll(Sort sort);

    /**
     * 查询所有实体
     *
     * @param pageable 分页信息
     *
     * @return 查询结果
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 查询所有包含主键的实体
     *
     * @param iterable 主键集合
     *
     * @return 所有包含主键的实体
     */
    List<T> findAll(Iterable<ID> iterable);

    /**
     * 查询与样本匹配的所有实体
     *
     * @param example 样本
     *
     * @return 与样本匹配的所有实体
     */
    List<T> findAll(Example<T> example);

    /**
     * 查询与样本匹配的所有实体并排序
     *
     * @param example 样本
     * @param sort    排序方式
     *
     * @return 与样本匹配的所有实体
     */
    List<T> findAll(Example<T> example, Sort sort);

    /**
     * 查询与样本匹配的所有实体
     *
     * @param example  样本
     * @param pageable 分页信息
     *
     * @return 与样本匹配的实体
     */
    Page<T> findAll(Example<T> example, Pageable pageable);

    /**
     * 按条件查询所有实体
     *
     * @param specification 查询条件
     *
     * @return 符合条件的查询结果
     */
    List<T> findAll(Specification<T> specification);

    /**
     * 按条件查询所有实体
     *
     * @param specification 查询条件
     * @param sort          排序方式
     *
     * @return 符合条件的查询结果
     */
    List<T> findAll(Specification<T> specification, Sort sort);

    /**
     * 按条件查询所有实体
     *
     * @param specification 查询条件
     * @param pageable      分页信息
     *
     * @return 符合条件的查询结果
     */
    Page<T> findAll(Specification<T> specification, Pageable pageable);

    /**
     * 删除一个实体
     *
     * @param entity 实体
     */
    void delete(T entity);

    /**
     * 删除所有实体
     */
    void deleteAll();

    /**
     * 删除实体
     *
     * @param iterable 要删除的实体
     */
    void deleteAll(Iterable<T> iterable);

    /**
     * 用批处理调用删除所有实体
     */
    void deleteAllInBatch();

    /**
     * 删除指定主键的实体
     *
     * @param id 实体主键
     */
    void deleteById(ID id);

    /**
     * 用批处理调用删除实体
     *
     * @param iterable 要删除的实体
     */
    void deleteInBatch(Iterable<T> iterable);
}
