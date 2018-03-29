package com.qxcmp.message;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.qxcmp.message.SmsAliyunService.ALIYUM_SMS_CODE_SUCCSEE;

/**
 * @author Aaric
 */
@Service
public class SmsSendRecordService extends AbstractEntityService<SmsSendRecord, String, SmsSendRecordRepository> {

    public Long countByDateAfter(Date date) {
        return repository.countByDateCreatedAfterAndCode(date, ALIYUM_SMS_CODE_SUCCSEE);
    }
}
