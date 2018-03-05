package com.qxcmp.config;

import java.util.List;
import java.util.Optional;

/**
 * 用户配置服务
 *
 * @author aaric
 */
public interface UserConfigService {

    /**
     * 列表分隔符
     */
    String SEPARATOR = "\n";

    /**
     * 获取配置值并转换为字符串
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<String> getString(String userId, String name);

    /**
     * 获取配置值并转换为整型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Integer> getInteger(String userId, String name);

    /**
     * 获取配置值并转换为短整型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Short> getShort(String userId, String name);

    /**
     * 获取配置值并转换为长整型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Long> getLong(String userId, String name);

    /**
     * 获取配置值并转换为单精度浮点型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Float> getFloat(String userId, String name);

    /**
     * 获取配置值并转换为双精度浮点型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Double> getDouble(String userId, String name);

    /**
     * 获取配置值并转换为布尔值类型，如果转换失败则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Boolean> getBoolean(String userId, String name);

    /**
     * 获取配置值并转换为字段串数组
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @return 如果没有配置则列表为空
     */
    List<String> getList(String userId, String name);

    /**
     * 创建系统配置，如果配置已经存在返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @param value  配置值
     * @return 创建后的系统配置，如果当前配置已经存在则返回{@link Optional#empty()}
     */
    Optional<UserConfig> create(String userId, String name, String value);

    /**
     * 更改系统配置的值，如果配置不存在则返回空
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @param value  新的配置值
     * @return 修改后的配置，如果当前配置不存在则返回{@link Optional#empty()}
     */
    Optional<UserConfig> update(String userId, String name, String value);

    /**
     * 更改系统配置的值，如果配置不存在则返回空列表
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @param value  新的配置值
     * @return 修改后的配置，如果当前配置不存在则返回{@link Optional#empty()}
     */
    List<String> update(String userId, String name, List<String> value);

    /**
     * 保存一个用户配置，如果不存在则自动创建
     *
     * @param userId 用户ID
     * @param name   配置主键
     * @param value  配置值
     * @return 保存后的系统配置
     */
    Optional<UserConfig> save(String userId, String name, String value);
}
