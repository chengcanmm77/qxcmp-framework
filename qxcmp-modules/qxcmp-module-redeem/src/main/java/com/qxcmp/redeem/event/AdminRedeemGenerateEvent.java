package com.qxcmp.redeem.event;

import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class AdminRedeemGenerateEvent {

    private final User target;
    private final int count;
    private final String name;
    private final String content;

}
