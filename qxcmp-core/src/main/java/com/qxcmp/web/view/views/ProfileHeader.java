package com.qxcmp.web.view.views;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileHeader extends AbstractComponent {

    private String portrait;

    private String url;

    private Component content;

    public ProfileHeader(String portrait) {
        this.portrait = portrait;
    }

    public ProfileHeader(String portrait, String url) {
        this.portrait = portrait;
        this.url = url;
    }

    public ProfileHeader setContent(String title) {
        this.content = new ContentHeader(title, Size.TINY);
        return this;
    }

    public ProfileHeader setContent(String title, String subTitle) {
        this.content = new ContentHeader(title, Size.TINY).setSubTitle(subTitle);
        return this;
    }

    public ProfileHeader setContent(Component content) {
        this.content = content;
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/views/profile";
    }
}
