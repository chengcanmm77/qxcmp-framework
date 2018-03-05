package com.qxcmp.shoppingmall.web;

import lombok.Data;

/**
 * Created by aaric on 2017/7/17.
 */
@Data
public class MallItemForm {

    private long id;

    private boolean selected;

    private int quantity;
}
