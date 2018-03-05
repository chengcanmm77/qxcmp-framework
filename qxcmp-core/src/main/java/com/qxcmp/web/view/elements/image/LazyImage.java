package com.qxcmp.web.view.elements.image;

import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Transition;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 懒加载图片
 * <p>
 * 当浏览器滚动到图片位置时候才下载图片
 *
 * @author Aaric
 */
@Getter
@Setter
public class LazyImage extends Image {

    /**
     * 懒加载图片ID
     * <p>
     * 用于初始化懒加载
     */
    private String id = "lazy-image-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 图片出现动画
     */
    private Transition transition = Transition.FADE_IN;

    /**
     * 图片出现时长
     */
    private int duration = 1000;

    public LazyImage(String source) {
        super(source);
    }

    public LazyImage(String source, String url) {
        super(source, url);
    }

    public LazyImage(String source, String url, AnchorTarget target) {
        super(source, url, target);
    }

    @Override
    public String getFragmentName() {
        return "lazy-image";
    }

    public LazyImage setTransition(Transition transition) {
        this.transition = transition;
        return this;
    }

    public LazyImage setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
