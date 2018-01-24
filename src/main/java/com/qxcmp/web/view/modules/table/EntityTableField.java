package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@Setter
public class EntityTableField {

    private Field field;

    private Method render;

    private String title;

    private String description;

    private int order;

    private String fieldSuffix;

    private int maxCollectionCount;

    private String collectionEntityIndex;

    private boolean image;

    private boolean enableUrl;

    private String urlPrefix;

    private String urlEntityIndex;

    private String urlSuffix;

    private AnchorTarget urlTarget;

    private Alignment alignment;
}
