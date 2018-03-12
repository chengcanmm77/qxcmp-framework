package com.qxcmp.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 本地日期
 * <p>
 * 包含了一个具体日期信息
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public class CalendarDate {

    /**
     * 该本地日期代表的零点时间戳
     */
    private long timestamp;

    /**
     * 直接可用年份, 2017表示2017年
     */
    private int year;

    /**
     * 月份 1-12
     */
    private int mouth;

    /**
     * 号数 1-31
     */
    private int day;

    /**
     * 该本地日期类型
     */
    private CalendarDateType type;

    /**
     * 描述信息
     */
    private String comments;
}
