package com.qxcmp.web.view.elements.header;

public enum HeaderType {
    H1("h1"),
    H2("h2"),
    H3("h3"),
    H4("h4"),
    H5("h5"),
    H6("h6");

    private String tag;

    HeaderType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
