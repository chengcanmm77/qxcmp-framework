package com.qxcmp.web.view.elements.html;

import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 超链接
 *
 * @author aaric
 */
@Getter
@Setter
public class Anchor extends InlineElement {

    /**
     * 超链接地址
     */
    private String href;

    /**
     * 超链接打开方式
     *
     * @see AnchorTarget
     */
    private String target;

    /**
     * 若超链接表示下载文件，则该属性用来设置要下载的文件名称，浏览器将自动检测文件扩展名
     */
    private String download;

    public Anchor(String text, String href) {
        super(text);
        this.href = href;
    }

    public Anchor(String text, String href, String target) {
        super(text);
        this.href = href;
        this.target = target;
    }

    public Anchor(String text, String href, String target, String download) {
        super(text);
        this.href = href;
        this.target = target;
        this.download = download;
    }

    @Override
    public String getFragmentName() {
        return "anchor";
    }
}
