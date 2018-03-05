package com.qxcmp.config;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置服务
 *
 * @author aaric
 */
public interface SystemConfigService {

    /**
     * 列表分隔符
     */
    String SEPARATOR = "\n";

    /**
     * 获取配置值并转换为字符串
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<String> getString(String name);

    /**
     * 获取配置值并转换为整型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Integer> getInteger(String name);

    /**
     * 获取配置值并转换为短整型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Short> getShort(String name);

    /**
     * 获取配置值并转换为长整型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Long> getLong(String name);

    /**
     * 获取配置值并转换为单精度浮点型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Float> getFloat(String name);

    /**
     * 获取配置值并转换为双精度浮点型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Double> getDouble(String name);

    /**
     * 获取配置值并转换为布尔值类型，如果转换失败则返回空
     *
     * @param name 配置主键
     * @return 当前系统配置，或者{@link Optional#empty()}
     */
    Optional<Boolean> getBoolean(String name);

    /**
     * 获取配置值并转换为字段串数组
     *
     * @param name 配置主键
     * @return 如果没有配置则列表为空
     */
    List<String> getList(String name);

    /**
     * 创建系统配置，如果配置已经存在返回空
     *
     * @param name  配置主键
     * @param value 配置值
     * @return 创建后的系统配置，如果当前配置已经存在则返回{@link Optional#empty()}
     */
    Optional<SystemConfig> create(String name, String value);

    /**
     * 更改系统配置的值，如果配置不存在则返回空
     *
     * @param name  配置主键
     * @param value 新的配置值
     * @return 修改后的配置，如果当前配置不存在则返回{@link Optional#empty()}
     */
    Optional<SystemConfig> update(String name, String value);

    /**
     * 更改系统配置的值，如果配置不存在则返回空列表
     *
     * @param name  配置主键
     * @param value 新的配置值
     * @return 修改后的配置，如果当前配置不存在则返回{@link Optional#empty()}
     */
    List<String> update(String name, List<String> value);
}
