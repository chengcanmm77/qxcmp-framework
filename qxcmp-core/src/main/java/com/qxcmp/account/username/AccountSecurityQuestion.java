package com.qxcmp.account.username;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户密保问题
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class AccountSecurityQuestion {

    @Id
    private String id;

    private String userId;

    private String question1;

    private String answer1;

    private String question2;

    private String answer2;

    private String question3;

    private String answer3;

}