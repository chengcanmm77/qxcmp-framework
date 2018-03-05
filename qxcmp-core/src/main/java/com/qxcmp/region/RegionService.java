package com.qxcmp.region;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfigConfiguration;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.init.QxcmpInitializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService extends AbstractEntityService<Region, String, RegionRepository> implements QxcmpInitializer {

    private final SystemConfigService systemConfigService;

    public List<Region> findByLevel(RegionLevel level) {
        return repository.findByLevel(level);
    }

    public List<Region> findInferiors(Region region) {
        return repository.findInferiors(region.getCode());
    }

    public List<Region> findInferiors(String parent) {
        return repository.findInferiors(parent);
    }

    public List<Region> findAllInferiors(Region region) {
        return repository.findAllInferiors(region.getCode());
    }

    public List<Region> findAllInferiors(String parent) {
        return repository.findAllInferiors(parent);
    }

    @Override
    public void init() {
        if (!systemConfigService.getBoolean(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_REGION_INITIAL_FLAG).orElse(false)) {
            reload();
            systemConfigService.update(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_REGION_INITIAL_FLAG, "true");
        }
    }

    public void reload() {
        try {
            Resource areaFile = new ClassPathResource("/district/District.csv");

            CSVFormat.EXCEL.parse(new InputStreamReader(areaFile.getInputStream())).forEach(record -> {

                if (record.getRecordNumber() == 1) {
                    return;
                }

                try {
                    String id = record.get(0);
                    String name = record.get(1);

                    if (!findOne("86").isPresent()) {
                        create(() -> {
                            Region region = next();
                            region.setCode("86");
                            region.setLevel(RegionLevel.STATE);
                            region.setName("中国");
                            return region;
                        });
                    }

                    if (!findOne(id).isPresent()) {
                        Region region = next();

                        region.setCode(id);
                        region.setName(name);

                        if (StringUtils.endsWith(id, "0000")) {
                            region.setLevel(RegionLevel.PROVINCE);
                            region.setParent("86");
                        } else if (StringUtils.endsWith(id, "00")) {
                            region.setLevel(RegionLevel.CITY);
                            region.setParent(StringUtils.substring(id, 0, 2) + "0000");
                        } else {
                            region.setLevel(RegionLevel.COUNTY);
                            region.setParent(StringUtils.substring(id, 0, 4) + "00");
                        }

                        create(() -> region);
                    }

                } catch (NumberFormatException ignored) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
