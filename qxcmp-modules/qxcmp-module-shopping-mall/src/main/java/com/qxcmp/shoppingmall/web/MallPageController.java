package com.qxcmp.shoppingmall.web;

import com.google.common.collect.Maps;
import com.qxcmp.shoppingmall.Commodity;
import com.qxcmp.shoppingmall.CommodityService;
import com.qxcmp.shoppingmall.CommodityVersion;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

@Controller
@RequestMapping("/mall")
@RequiredArgsConstructor
public class MallPageController extends QxcmpController {

    private final CommodityService commodityService;

    private final MallPageHelper mallPageHelper;

    @GetMapping("/item/{id}.html")
    public ModelAndView commodityDetailsPage(@PathVariable String id, Device device) {
        return commodityService.findOne(id)
                .filter(commodity -> !commodity.isDisabled())
                .map(commodity -> page().addComponent(device.isMobile() ? mallPageHelper.nextMobileCommodityDetails(commodity) : mallPageHelper.nextMobileCommodityDetails(commodity))
                        .setTitle(String.format("%s_%s", commodity.getTitle(), siteService.getTitle()))
                        .hideMobileBottomMenu()
                        .build()).orElse(page(new Overview(new IconHeader("商品已下架或被删除", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build());
    }

    @GetMapping("/item/version-select")
    public ModelAndView commodityVersionSelectPage(@RequestParam String id) {
        return commodityService.findOne(id)
                .filter(commodity -> !commodity.isDisabled())
                .map(commodity -> {
                    List<Commodity> commodityList = commodityService.findByParentId(commodity.getParentId());

                    if (commodityList.size() <= 1) {
                        return page(viewHelper.nextWarningOverview("暂无关联商品", "").addLink("返回", String.format("/mall/item/%d.html", commodity.getId()))).build();
                    }

                    Map<String, String> currentVersions = Maps.newHashMap();
                    commodity.getVersions().forEach(commodityVersion -> currentVersions.put(commodityVersion.getName(), commodityVersion.getValue()));

                    AbstractGrid grid = new Grid().setVerticallyPadded().setContainer();
                    grid.setCustomClass("qxcmp-commodity-select");
                    grid.addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                            .addComponent(new PageHeader(HeaderType.H2,
                                    new DecimalFormat("￥0.00").format((double) commodity.getSellPrice() / 100))
                                    .setSubTitle("商品编号：" + commodity.getId())
                                    .setImage(new Image(commodity.getCover()).setBordered().setRounded())
                            )
                    ));

                    List<CommodityVersion> commodityVersionList = commodityList.stream().flatMap(c -> c.getVersions().stream()).distinct().collect(Collectors.toList());

                    Map<String, List<String>> versions = commodityVersionList.stream().collect(Collectors.groupingBy(CommodityVersion::getName, Collectors.mapping(CommodityVersion::getValue, Collectors.toList())));


                    versions.forEach((name, values) -> {
                        Col col = new Col(Wide.SIXTEEN);

                        values.forEach(s -> {
                            String buttonUrl = getButtonUrl(currentVersions, name, s, commodityList);

                            AbstractButton button = new Button(s, buttonUrl).setBasic().setSize(Size.MINI);

                            button.setCustomClass("qxcmp-commodity-version");

                            if (StringUtils.isNotBlank(buttonUrl)) {
                                if (commodity.getVersions().stream().anyMatch(commodityVersion ->
                                        StringUtils.equals(commodityVersion.getName(), name) && StringUtils.equals(commodityVersion.getValue(), s))) {
                                    button.setPrimary();
                                } else {
                                    button.setSecondary();
                                }
                            } else {
                                button.setDisabled();
                            }

                            col.addComponent(button);
                        });

                        grid.addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new P(name))).addCol(col));
                    });


                    Col col = (Col) new Col(Wide.SIXTEEN).setAlignment(Alignment.CENTER);

                    col.addComponent(new Divider());
                    col.addComponent(new Button("返回", String.format("/mall/item/%d.html", commodity.getId())).setBasic());

                    grid.addItem(new Row().addCol(col));

                    return page().addComponent(grid)
                            .hideMobileBottomMenu()
                            .build();

                }).orElse(page(viewHelper.nextWarningOverview("商品不存在", "").addLink("返回", "/mall")).build());
    }

    /**
     * 计算按钮对应的商品链接
     *
     * @param currentVersions 当前的商品版本信息
     * @param name            分类名称
     * @param value           分类值
     * @param commodityList   候选商品
     * @return 商品Url
     */
    private String getButtonUrl(Map<String, String> currentVersions, String name, String value, List<Commodity> commodityList) {
        return commodityList.stream().filter(commodity -> {
            List<CommodityVersion> versions = commodity.getVersions();
            Map<String, String> targetVersions = Maps.newLinkedHashMap();

            currentVersions.forEach((n, v) -> {
                if (StringUtils.equals(n, name)) {
                    targetVersions.put(name, value);
                } else {
                    targetVersions.put(n, v);
                }
            });

            return versions.stream().allMatch(commodityVersion -> StringUtils.equals(targetVersions.get(commodityVersion.getName()), commodityVersion.getValue()));

        }).map(commodity -> "/mall/item/version-select?id=" + commodity.getId()).findAny().orElse("");
    }
}
