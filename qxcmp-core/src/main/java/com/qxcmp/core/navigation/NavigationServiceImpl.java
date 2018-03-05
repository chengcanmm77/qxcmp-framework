package com.qxcmp.core.navigation;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

@Service
public class NavigationServiceImpl implements NavigationService {

    private Map<String, AbstractNavigation> navigationMap = Maps.newConcurrentMap();

    @Override
    public AbstractNavigation get(String id) {
        checkState(navigationMap.containsKey(id), "Navigation not exist: " + id);
        return navigationMap.get(id);
    }

    @Override
    public void add(AbstractNavigation navigation) {
        checkState(!navigationMap.containsKey(navigation.getId()), "Navigation already exist: " + navigation.getId());
        navigationMap.put(navigation.getId(), navigation);
    }
}
