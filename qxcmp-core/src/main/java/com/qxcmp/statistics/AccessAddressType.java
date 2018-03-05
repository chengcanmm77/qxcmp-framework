package com.qxcmp.statistics;

/**
 * 访问地址类型
 *
 * @author Aaric
 */
public enum AccessAddressType {

    /**
     * 普通地址
     */
    NORMAL,

    /**
     * 表明该地址为黑名单
     */
    BLACK,

    /**
     * 表明该地址为白名单
     */
    WHITE,

    /**
     * 表明该地址为网络爬虫
     * <p>
     * 爬虫的访问将不会记录统计信息
     */
    SPIDER
}
