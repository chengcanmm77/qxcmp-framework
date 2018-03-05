package com.qxcmp.config;

import com.google.common.collect.Lists;
import com.qxcmp.core.QxcmpConfigurator;
import com.qxcmp.core.QxcmpSystemConfigConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * 系统字典加载器
 * <p>
 * 负责加载系统默认字典配置
 *
 * @author aaric
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemDictionaryLoader implements QxcmpConfigurator {

    private final SystemConfigService systemConfigService;

    private final SystemDictionaryService systemDictionaryService;

    private final SystemDictionaryItemService systemDictionaryItemService;

    public void reload() throws IOException {
        Resource resource = new ClassPathResource("/dictionary/Dictionary.csv");
        CSVFormat.EXCEL.parse(new InputStreamReader(resource.getInputStream())).forEach(record -> {
            if (record.getRecordNumber() == 1) {
                return;
            }

            String name = "";

            List<String> values = Lists.newArrayList();

            for (String value : record) {
                if (StringUtils.isBlank(name)) {
                    name = value;
                } else {
                    values.add(value);
                }
            }

            SystemDictionary systemDictionary;

            Optional<SystemDictionary> systemDictionaryOptional = systemDictionaryService.findOne(name);

            if (systemDictionaryOptional.isPresent()) {
                systemDictionary = systemDictionaryOptional.get();
            } else {
                systemDictionary = new SystemDictionary();
                systemDictionary.setName(name);
                systemDictionary = systemDictionaryService.save(systemDictionary);
            }

            for (int i = 0; i < values.size(); i++) {

                String value = values.get(i);

                if (StringUtils.isBlank(value)) {
                    continue;
                }

                if (systemDictionary.getItems().stream().noneMatch(systemDictionaryItem -> systemDictionaryItem.getName().equals(value))) {
                    SystemDictionaryItem systemDictionaryItem = new SystemDictionaryItem();
                    systemDictionaryItem.setParent(systemDictionary);
                    systemDictionaryItem.setName(value);
                    systemDictionaryItem.setPriority(i);
                    systemDictionaryItemService.save(systemDictionaryItem);
                }
            }

            systemDictionaryService.save(systemDictionary);
        });
    }

    @Override
    public void config() throws IOException {
        if (!systemConfigService.getBoolean(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_DICTIONARY_INITIAL_FLAG).orElse(false)) {
            reload();
            systemConfigService.update(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_DICTIONARY_INITIAL_FLAG, "true");
        }

        systemDictionaryService.refresh();
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 2;
    }
}
