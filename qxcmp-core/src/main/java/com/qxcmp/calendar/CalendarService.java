package com.qxcmp.calendar;

import java.util.Date;

/**
 * 日期服务
 * <p>
 * 可以判断某一天是否为假期
 * <p>
 * 目前支持的日期查询返回 <li>2017年</li>
 *
 * @author aaric
 */
public interface CalendarService {

    /**
     * 获取一个日期的日期类型
     *
     * @param date 日期
     * @return 日期类型
     * @throws CalendarServiceException 如果日期不在支持范围能则抛出该异常
     */
    CalendarDate get(Date date) throws CalendarServiceException;

    /**
     * 获取一个日期的日期类型
     *
     * @param year  年，不用任何转换，比如2017年就直接传入参数2017
     * @param mouth 月份 1 - 12
     * @param day   日期 1 - 31
     * @return 日期类型
     * @throws CalendarServiceException 如果日期不在支持范围能则抛出该异常
     */
    CalendarDate get(int year, int mouth, int day) throws CalendarServiceException;
}
