package com.qxcmp.calendar;

import com.google.common.collect.Maps;
import com.qxcmp.core.QxcmpConfigurator;
import org.apache.commons.csv.CSVFormat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class CalendarServiceImpl implements CalendarService, QxcmpConfigurator {

    private Map<String, CalendarDate> calendarDateMap = Maps.newConcurrentMap();

    @Override
    public CalendarDate get(Date date) throws CalendarServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int mouth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return get(year, mouth + 1, day);
    }

    @Override
    public CalendarDate get(int year, int mouth, int day) throws CalendarServiceException {

        CalendarDate calendarDate = calendarDateMap.get(String.format("%04d%02d%02d", year, mouth, day));

        if (Objects.isNull(calendarDate)) {
            throw new CalendarServiceException();
        }

        return calendarDate;
    }

    @Override
    public void config() {
        try {
            Resource calendarCSVFile = new ClassPathResource("/calendar/Calendar.csv");
            CSVFormat.EXCEL.parse(new InputStreamReader(calendarCSVFile.getInputStream())).forEach(record -> {
                String date = record.get(0);
                String type = record.get(1);
                String comments = record.get(2);

                if (record.getRecordNumber() == 1) {
                    return;
                }

                int year = Integer.parseInt(date.substring(0, 4));
                int mouth = Integer.parseInt(date.substring(4, 6));
                int day = Integer.parseInt(date.substring(6, 8));
                CalendarDateType dateType;
                try {
                    dateType = CalendarDateType.valueOf(type);
                } catch (Exception e) {
                    dateType = CalendarDateType.WORKDAY;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, mouth - 1, day, 0, 0, 0);

                calendarDateMap.put(date, new CalendarDate(calendar.getTimeInMillis(), year, mouth, day, dateType, comments));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
