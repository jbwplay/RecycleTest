package com.recycletest.recycleview;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;

public final class ResourceUtils {

    public static int id(Context context, String resourceName, TYPE type) {
        Resources resources = context.getResources();
        // 获取系统R使用android作为包名即可
        return resources.getIdentifier(resourceName, type.getString(), context.getPackageName());
    }

    public enum TYPE {
        ATTR("attr"),
        ARRAY("array"),
        ANIM("anim"),
        BOOL("bool"),
        COLOR("color"),
        DIMEN("dimen"),
        DRAWABLE("drawable"),
        ID("id"),
        INTEGER("integer"),
        LAYOUT("layout"),
        MENU("menu"),
        MIPMAP("mipmap"),
        RAW("raw"),
        STRING("string"),
        STYLE("style");

        private String string;

        TYPE(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    // context.getResources().getIdentifier can not retrieve styleable Id
    public static int getStyleable(Context context, String name) {
        return ((Integer) getResourceIdNew(context, name, "styleable")).intValue();
    }

    public static int[] getStyleableArray(Context context, String name) {
        return (int[]) getResourceIdNew(context, name, "styleable");
    }

    private static Object getResourceIdNew(Context context, String name, String type) {
        String className = context.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName(className);
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return field.get(null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
