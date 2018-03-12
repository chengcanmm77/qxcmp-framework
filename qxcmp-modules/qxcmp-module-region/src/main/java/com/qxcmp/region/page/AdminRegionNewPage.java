package com.qxcmp.region.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.region.Region;
import com.qxcmp.region.form.AdminRegionNewForm;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.segment.Segment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.region.RegionModule.ADMIN_REGION_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminRegionNewPage extends AbstractQxcmpAdminFormPage<AdminRegionNewForm> {

    private final List<Region> inferiors;

    public AdminRegionNewPage(AdminRegionNewForm form, BindingResult bindingResult, List<Region> inferiors) {
        super(form, bindingResult);
        this.inferiors = inferiors;
    }

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(new Segment().addComponent(viewHelper.nextForm(form, bindingResult)).addComponent(viewHelper.nextTable(objectObjectMap -> {
            inferiors.forEach(region -> objectObjectMap.put(region.getName(), region.getCode()));
        }))));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("地区管理", ADMIN_REGION_URL, "添加地区");
    }
}
