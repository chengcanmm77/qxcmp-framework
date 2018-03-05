package com.qxcmp.web.view.modules.form.field;

import com.qxcmp.web.view.modules.form.support.DateTimeFieldType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateTimeField extends BaseHTMLField {

    private DateTimeFieldType type = DateTimeFieldType.DATETIME;

    private boolean showToday;

    private boolean showAmPm;

    private boolean disableYear;

    private boolean disableMonth;

    private boolean disableMinute;

    private Date minDate;

    private Date maxDate;

    @Override
    public String getFragmentName() {
        return "field-date-time";
    }
}
