package com.qxcmp.web.view.elements.image;

import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 头像
 *
 * @author aaric
 */
@Getter
@Setter
public class Avatar extends AbstractImage {

    private boolean centered;

    public Avatar(String source) {
        super(source);
    }

    public Avatar(String source, String url) {
        super(source, url);
    }

    public Avatar(String source, String url, AnchorTarget target) {
        super(source, url, target);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + (centered ? " centered" : "") + " avatar";
    }

    public Avatar setCentered() {
        setCentered(true);
        return this;
    }
}
