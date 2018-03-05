package com.qxcmp.web.model;

import lombok.Data;

/**
 * RESTFUL 响应实体
 *
 * @author Aaric
 */
@Data
public class RestfulResponse {

    private int status;

    private String code;

    private String message;

    private String developerMessage;

    private String moreInfo;

    public RestfulResponse(int status) {
        this.status = status;
    }

    public RestfulResponse(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public RestfulResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public RestfulResponse(int status, String code, String message, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public RestfulResponse(int status, String code, String message, String developerMessage, String moreInfo) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfo = moreInfo;
    }
}
