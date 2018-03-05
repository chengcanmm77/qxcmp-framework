package com.qxcmp.web.view.elements.segment;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 片段组
 */
@Getter
@Setter
public class Segments extends AbstractComponent implements Segmentable {

    /**
     * 是否为紧凑模式
     * <p>
     * 该模式的片段宽度为内容实际宽度
     */
    private boolean compact;

    /**
     * 片段组内容
     */
    private List<Segmentable> items = Lists.newArrayList();

    public Segments addItem(Segmentable item) {
        items.add(item);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/segment";
    }

    @Override
    public String getFragmentName() {
        return "segments";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return compact ? " compact" : "";
    }

    @Override
    public String getClassSuffix() {
        return "segments";
    }

    public Segments setCompact() {
        this.compact = true;
        return this;
    }
}
