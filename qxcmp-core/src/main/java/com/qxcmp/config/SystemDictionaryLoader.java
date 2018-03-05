package com.qxcmp.config;

import com.google.common.collect.Lists;
import com.qxcmp.core.QxcmpSystemConfigConfiguration;
import com.qxcmp.core.init.QxcmpInitailizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemDictionaryLoader implements QxcmpInitailizer {

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
}
