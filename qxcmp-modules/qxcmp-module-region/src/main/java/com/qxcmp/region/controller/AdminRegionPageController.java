package com.qxcmp.region.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.audit.ActionException;
import com.qxcmp.region.Region;
import com.qxcmp.region.RegionLevel;
import com.qxcmp.region.RegionService;
import com.qxcmp.region.event.RegionDisableEvent;
import com.qxcmp.region.event.RegionEnableEvent;
import com.qxcmp.region.form.AdminRegionNewForm;
import com.qxcmp.region.page.AdminRegionNewPage;
import com.qxcmp.region.page.AdminRegionTablePage;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.qxcmp.region.RegionModule.ADMIN_REGION_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_REGION_URL)
@RequiredArgsConstructor
public class AdminRegionPageController extends QxcmpAdminController {

    private final RegionService regionService;

    @GetMapping("")
    public ModelAndView table(Pageable pageable) {
        return page(AdminRegionTablePage.class, regionService, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "level"));
    }

    @GetMapping("/new")
    public ModelAndView newGet(@RequestParam String id, final AdminRegionNewForm form, BindingResult bindingResult) {
        return regionService.findOne(id)
                .filter(region -> region.getLevel().equals(RegionLevel.CITY))
                .map(region -> {
                    form.setParent(String.format("%s - %s", region.getName(), region.getCode()));
                    List<Region> inferiors = regionService.findInferiors(region);
                    if (!inferiors.isEmpty()) {
                        Region infer = inferiors.get(inferiors.size() - 1);
                        try {
                            form.setCode(String.valueOf(Integer.parseInt(infer.getCode()) + 1));
                        } catch (Exception ignored) {

                        }
                    }
                    return page(AdminRegionNewPage.class, form, bindingResult, inferiors);
                })
                .orElse(overviewPage(viewHelper.nextWarningOverview("地区不存在")));
    }

    @PostMapping("/new")
    public ModelAndView newPost(@Valid final AdminRegionNewForm form, BindingResult bindingResult) {
        form.setParent(StringUtils.substringAfter(form.getParent(), "-").trim());
        return regionService.findOne(form.getParent())
                .filter(region -> region.getLevel().equals(RegionLevel.CITY))
                .map(region -> {
                    if (regionService.findOne(form.getCode()).isPresent()) {
                        bindingResult.rejectValue("code", "", "地区代码已经存在");
                    }
                    if (bindingResult.hasErrors()) {
                        return newGet(form.getParent(), form, bindingResult);
                    }
                    return createEntity(regionService, form);
                })
                .orElse(overviewPage(viewHelper.nextWarningOverview("地区不存在")));
    }

    @GetMapping("/{id}/new")
    public ModelAndView newGetRedirect(@PathVariable String id) {
        return redirect(ADMIN_REGION_URL + "/new?id=" + id);
    }

    @PostMapping("/{code}/disable")
    public ResponseEntity<RestfulResponse> regionDisable(@PathVariable String code) {
        return execute("禁用地区", context -> {
            try {
                regionService.findOne(code).ifPresent(region -> {
                    if (region.getLevel().equals(RegionLevel.PROVINCE)) {
                        regionService.findInferiors(region).forEach(pInferior -> {
                            regionService.update(pInferior.getCode(), r -> r.setDisable(true));
                            regionService.findInferiors(pInferior).forEach(cInferior -> {
                                regionService.update(cInferior.getCode(), c -> c.setDisable(true));
                            });
                        });
                    } else if (region.getLevel().equals(RegionLevel.CITY)) {
                        regionService.findInferiors(region).forEach(inferior -> regionService.update(inferior.getCode(), r -> r.setDisable(true)));
                    }
                    applicationContext.publishEvent(new RegionDisableEvent(currentUser().orElseThrow(RuntimeException::new), regionService.update(region.getCode(), r -> r.setDisable(true))));
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @PostMapping("/{code}/enable")
    public ResponseEntity<RestfulResponse> regionEnable(@PathVariable String code) {
        return execute("启用地区", context -> {
            try {
                regionService.findOne(code).ifPresent(region -> {
                    if (region.getLevel().equals(RegionLevel.PROVINCE)) {
                        regionService.findAllInferiors(region).forEach(pInferior -> {
                            regionService.update(pInferior.getCode(), r -> r.setDisable(false));
                            regionService.findAllInferiors(pInferior).forEach(cInferior -> {
                                regionService.update(cInferior.getCode(), c -> c.setDisable(false));
                            });
                        });
                    } else if (region.getLevel().equals(RegionLevel.CITY)) {
                        regionService.findAllInferiors(region).forEach(inferior -> regionService.update(inferior.getCode(), r -> r.setDisable(false)));
                    }
                    applicationContext.publishEvent(new RegionEnableEvent(currentUser().orElseThrow(RuntimeException::new), regionService.update(region.getCode(), r -> r.setDisable(false))));
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }
}
