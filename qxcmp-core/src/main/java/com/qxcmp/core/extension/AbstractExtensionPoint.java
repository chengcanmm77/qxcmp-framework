package com.qxcmp.core.extension;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平台抽象扩展点
 *
 * @param <T>
 *
 * @author Aaric
 */
public abstract class AbstractExtensionPoint<T extends Extension> implements ExtensionPoint<T> {

    private final List<T> extensions = Lists.newArrayList();

    private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
    };

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getExtension() {
        return (Class<T>) typeToken.getRawType();
    }

    @Override
    public List<T> getExtensions() {
        return extensions.stream().filter(Extension::isEnabled).collect(Collectors.toList());
    }

    @Override
    public ExtensionPoint<T> addExtension(T extension) {
        extensions.add(extension);
        extensions.sort(Comparator.comparingInt(Extension::order));
        return this;
    }
}
