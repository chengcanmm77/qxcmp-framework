package com.qxcmp.shoppingmall;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 使用券事件
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public class OfferEvent {

    /**
     * 所使用的券
     */
    private Offer offer;
}
