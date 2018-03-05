package com.qxcmp.core.support;

import org.joda.time.Interval;
import org.joda.time.Period;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 时间周日处理工具
 *
 * @author Aaric
 */
@Component
public class TimeDurationHelper {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;

    /**
     * 将时间转换为可显示文本
     *
     * @param duration 时间长度（毫秒）
     * @return 可显示文本
     */
    public String convert(long duration) {
        long days = duration / DAY;
        long hours = (duration - days * DAY) / HOUR;
        long minutes = (duration - days * DAY - hours * HOUR) / MINUTE;
        long seconds = (duration - days * DAY - hours * HOUR - minutes * MINUTE) / SECOND;

        if (days > 0) {
            return String.format("%s天%s小时%s分%s秒", days, hours, minutes, seconds);
        }
        if (hours > 0) {
            return String.format("%s小时%s分%s秒", hours, minutes, seconds);
        }
        if (minutes > 0) {
            return String.format("%s分%s秒", minutes, seconds);
        }
        return String.format("%s秒", seconds);
    }

    /**
     * 显示时间距离当前时间的文本
     *
     * @param date 要显示的时间
     * @return 时间距离文本
     */
    public String fromNow(Date date) {
        Interval interval = new Interval(date.getTime(), System.currentTimeMillis());
        Period period = interval.toPeriod();
        if (period.getYears() > 0) {
            return period.getYears() + "年前";
        } else if (period.getMonths() > 0) {
            return period.getMonths() + "月前";
        } else if (period.getWeeks() > 0) {
            return period.getWeeks() + "周前";
        } else if (period.getDays() > 0) {
            return period.getDays() + "天前";
        } else if (period.getHours() > 0) {
            return period.getHours() + "小时前";
        } else if (period.getMinutes() > 0) {
            return period.getMinutes() + "分钟前";
        } else {
            return "刚刚";
        }
    }
}