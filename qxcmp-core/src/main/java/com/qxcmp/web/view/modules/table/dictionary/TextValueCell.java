package com.qxcmp.web.view.modules.table.dictionary;

import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.modules.table.AbstractTableCell;
import com.qxcmp.web.view.modules.table.TableData;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 文本单元格，支持超链接
 *
 * @author Aaric
 */
@Getter
@Setter
public class TextValueCell extends BaseDictionaryValueCell<String> {

    private String url;

    private String target = AnchorTarget.SELF.toString();

    public TextValueCell(String object) {
        super(object);
    }

    public TextValueCell(String object, String url) {
        super(object);
        this.url = url;
    }

    public TextValueCell(String object, String url, String target) {
        super(object);
        this.url = url;
        this.target = target;
    }

    @Override
    public AbstractTableCell render() {
        final TableData tableData = new TableData();

        if (StringUtils.isNotBlank(url)) {
            tableData.addComponent(new Anchor(object, url, target));
        } else {
            tableData.setContent(object);
        }

        return tableData;
    }
}
