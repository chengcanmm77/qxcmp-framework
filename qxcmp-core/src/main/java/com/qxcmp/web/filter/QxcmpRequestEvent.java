package com.qxcmp.web.filter;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aaric
 */
@Getter
public class QxcmpRequestEvent {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    public QxcmpRequestEvent(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
