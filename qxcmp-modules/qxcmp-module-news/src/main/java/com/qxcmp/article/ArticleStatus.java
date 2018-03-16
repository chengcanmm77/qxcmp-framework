package com.qxcmp.article;

/**
 * 文章状态
 *
 * @author Aaric
 */
public enum ArticleStatus {

    NEW("草稿"),

    AUDITING("审核中"),

    REJECT("被驳回"),

    PUBLISHED("已发布"),

    DISABLED("被禁用");

    private String name;

    ArticleStatus(String name) {
        this.name = name;
    }

    static ArticleStatus fromName(String name) {
        for (ArticleStatus version : values()) {
            if (version.getName().equals(name)) {
                return version;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
