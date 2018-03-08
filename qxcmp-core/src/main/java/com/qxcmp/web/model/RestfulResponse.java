package com.qxcmp.web.model;

import lombok.Builder;
import lombok.Data;

/**
 * RESTFUL 响应实体
 *
 * @author Aaric
 */
@Data
@Builder
public class RestfulResponse {

    private int status;

    private String code;

    private String message;

    private String developerMessage;

    private String moreInfo;
}
