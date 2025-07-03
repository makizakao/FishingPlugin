package org.hark7.fishingPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {
    public static final class Language {
        private static final String PREFIX = "Language";
        public static String defaultLanguage;
        public static String[] languages;

        private static void init() {
            setPathPrefix(PREFIX);
            defaultLanguage = getString("Default");
            languages = getStringList("Supported").toArray(new String[0]);
        }
    }

    public static final class DatabaseVersion {
        private static final String PREFIX = "DatabaseVersion";
        public static Integer playersTableVersion;
        public static Integer countsTableVersion;

        private static void init() {
            setPathPrefix(PREFIX);
            playersTableVersion = getInteger("PlayersTableVersion");
            countsTableVersion = getInteger("CountsTableVersion");
        }
    }
}
