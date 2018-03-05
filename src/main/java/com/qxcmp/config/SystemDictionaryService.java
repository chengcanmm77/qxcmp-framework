package com.qxcmp.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 系统字典服务
 *
 * @author aaric
 */
@Service
public class SystemDictionaryService extends AbstractEntityService<SystemDictionary, String, SystemDictionaryRepository> {

    private Multimap<String, String> cachedDictionary = ArrayListMultimap.create();

    /**
     * 获取一个系统字典的字典项
     *
     * @param name 字典名称
     *
     * @return 字典项
     */
    public List<String> get(String name) {
        return Lists.newArrayList(cachedDictionary.get(name));
    }

    /**
     * 刷新所有字典项
     */
    public void refresh() {
        Multimap<String, String> tmpMap = ArrayListMultimap.create();
        findAll().forEach(dictionary -> dictionary.getItems().stream().sorted(Comparator.comparingInt(SystemDictionaryItem::getPriority)).forEach(systemDictionaryItem -> tmpMap.put(dictionary.getName(), systemDictionaryItem.getName())));
        cachedDictionary = Multimaps.unmodifiableMultimap(tmpMap);
    }
}
