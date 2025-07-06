package org.hark7.fishingPlugin;

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

    public static final class Database {
        private static final String PREFIX = "Database";
        public static Integer playersTableVersion;
        public static Integer countsTableVersion;
        public static String databasePath;

        private static void init() {
            setPathPrefix(PREFIX);
            databasePath = getString("Path");
            playersTableVersion = getInteger("PlayersTableVersion");
            countsTableVersion = getInteger("CountsTableVersion");
        }
    }
}
