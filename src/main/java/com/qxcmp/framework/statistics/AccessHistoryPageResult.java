package com.qxcmp.framework.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Aaric
 */
@Data
@AllArgsConstructor
public class AccessHistoryPageResult {
    private String url;
    private Long nbr;
}
