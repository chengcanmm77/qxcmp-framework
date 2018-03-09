package com.qxcmp.advertisement;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
@Component
public class AdvertisementModule {

    public static final String ADMIN_ADVERTISEMENT_URL = ADMIN_URL + "/advertisement";
    public static final List<String> SUPPORT_TYPES = ImmutableList.of("横幅", "弹框", "摩天楼");

}
