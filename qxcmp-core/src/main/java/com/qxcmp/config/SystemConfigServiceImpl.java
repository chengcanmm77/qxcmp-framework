package com.qxcmp.config;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    private final UserService userService;

    @Override
    public Optional<String> getString(String name) {
        return systemConfigRepository.findById(name).filter(systemConfig -> StringUtils.isNotBlank(systemConfig.getValue())).map(SystemConfig::getValue);
    }

    @Override
    public Optional<Integer> getInteger(String name) {
        return getString(name).map(Integer::parseInt);
    }

    @Override
    public Optional<Short> getShort(String name) {
        return getString(name).map(Short::parseShort);
    }

    @Override
    public Optional<Long> getLong(String name) {
        return getString(name).map(Long::parseLong);
    }

    @Override
    public Optional<Float> getFloat(String name) {
        return getString(name).map(Float::parseFloat);
    }

    @Override
    public Optional<Double> getDouble(String name) {
        return getString(name).map(Double::parseDouble);
    }

    @Override
    public Optional<Boolean> getBoolean(String name) {
        return getString(name).map(Boolean::parseBoolean);
    }

    @Override
    public List<String> getList(String name) {
        return Arrays.stream(getString(name).orElse("").split(SEPARATOR)).filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());
    }

    @Override
    public Optional<SystemConfig> create(String name, String value) {
        if (!systemConfigRepository.findById(name).isPresent()) {
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setName(name);
            systemConfig.setDescription(name + ".description");
            systemConfig.setValue(value);
            return Optional.of(systemConfigRepository.save(systemConfig));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SystemConfig> update(String name, String value) {
        checkNotNull(value, "SystemConfig value can not be null");

        return systemConfigRepository.findById(name).flatMap(systemConfig -> {
            systemConfig.setDateModified(new Date());
            systemConfig.setUserModified(userService.currentUser());
            systemConfig.setValue(value);
            return Optional.of(systemConfigRepository.save(systemConfig));
        });
    }

    @Override
    public List<String> update(String name, List<String> value) {
        checkNotNull(value, "SystemConfig value can not be null");
        return update(name, StringUtils.join(value.stream().map(String::trim).collect(Collectors.toList()), SEPARATOR)).map(systemConfig -> getList(systemConfig.getName())).orElse(Lists.newArrayList());
    }

    @Override
    public String getPrefix(Class<?> cls) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.substringBefore(cls.getSimpleName().replaceAll("SystemConfig", "").replaceAll("Module", ""), "$$")).replaceAll("_", ".") + ".";
    }
}
