package org.hark7.fishingPlugin.util;

import org.mineacademy.fo.settings.Lang;

public class CustomLang {
    public static String[] ofArray(String path, String lang) {
        return new String[0];
    }

    public static ComponentBuilder ofComponent(String path, String lang) {
        return new ComponentBuilder(Lang.of(path));
    }
}

