package com.qxcmp.web.view.page;

import com.qxcmp.exception.BlackListException;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 平台错误页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class QxcmpErrorPage extends GenericQxcmpPage {

    private final Map<String, Object> errors;

    @Override
    public void render() {

        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errors.get("status").toString()));
        String message = errors.get("message").toString();

        if (StringUtils.equals(BlackListException.class.getName(), String.valueOf(errors.get("exception")))) {
            status = HttpStatus.GONE;
            message = "你已经在网页大叔的黑名单中了";
        }

        addComponent(new VerticallyDividedGrid().setVerticallyPadded().setTextContainer().addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                .addComponent(viewHelper.nextWarningOverview(status.toString(), parseStatusCode(status)).addComponent(new P(message))))));
    }

    private String parseStatusCode(HttpStatus status) {

        if (status.equals(HttpStatus.FORBIDDEN)) {
            return "网页叔叔被抓去关起来了";
        }

        if (status.equals(HttpStatus.GONE)) {
            return "网页大叔已经离你远去了";
        }

        if (status.is4xxClientError()) {
            return "网页叔叔搭乘航班去追寻诗和远方了";
        } else if (status.is5xxServerError()) {
            return "服务器君搭乘航班去追寻诗和远方了";
        }

        return status.getReasonPhrase();
    }
}
