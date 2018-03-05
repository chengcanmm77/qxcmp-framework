package com.qxcmp.config;

import com.google.common.collect.Lists;
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
public class UserConfigServiceImpl implements UserConfigService {

    private final UserConfigRepository userConfigRepository;

    @Override
    public Optional<String> getString(String userId, String name) {
        return userConfigRepository.findByUserIdAndName(userId, name).filter(userConfig -> StringUtils.isNotBlank(userConfig.getValue())).map(UserConfig::getValue);
    }

    @Override
    public Optional<Integer> getInteger(String userId, String name) {
        return getString(userId, name).map(Integer::parseInt);
    }

    @Override
    public Optional<Short> getShort(String userId, String name) {
        return getString(userId, name).map(Short::parseShort);
    }

    @Override
    public Optional<Long> getLong(String userId, String name) {
        return getString(userId, name).map(Long::parseLong);
    }

    @Override
    public Optional<Float> getFloat(String userId, String name) {
        return getString(userId, name).map(Float::parseFloat);
    }

    @Override
    public Optional<Double> getDouble(String userId, String name) {
        return getString(userId, name).map(Double::parseDouble);
    }

    @Override
    public Optional<Boolean> getBoolean(String userId, String name) {
        return getString(userId, name).map(Boolean::parseBoolean);
    }

    @Override
    public List<String> getList(String userId, String name) {
        return Arrays.stream(getString(userId, name).orElse("").split(SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }

    @Override
    public Optional<UserConfig> create(String userId, String name, String value) {
        if (!userConfigRepository.findByUserIdAndName(userId, name).isPresent()) {
            UserConfig userConfig = new UserConfig();
            userConfig.setUserId(userId);
            userConfig.setName(name);
            userConfig.setValue(value);
            return Optional.of(userConfigRepository.save(userConfig));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserConfig> update(String userId, String name, String value) {
        checkNotNull(value, "UserConfig value can not be null");

        return userConfigRepository.findByUserIdAndName(userId, name).flatMap(userConfig -> {
            userConfig.setDateModified(new Date());
            userConfig.setValue(value);
            return Optional.of(userConfigRepository.save(userConfig));
        });
    }

    @Override
    public List<String> update(String userId, String name, List<String> value) {
        return update(userId, name, StringUtils.join(value.stream().map(String::trim).collect(Collectors.toList()), SEPARATOR)).map(userConfig -> getList(userConfig.getUserId(), userConfig.getName())).orElse(Lists.newArrayList());
    }

    @Override
    public Optional<UserConfig> save(String userId, String name, String value) {
        if (userConfigRepository.findByUserIdAndName(userId, name).isPresent()) {
            return update(userId, name, value);
        } else {
            return create(userId, name, value);
        }
    }
}
