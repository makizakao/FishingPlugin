package org.hark7.fishingPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

    public static final class DatabaseVersion {
        private static final String PREFIX = "DatabaseVersion";
        private static final String PLAYERS_TABLE_VERSION_KEY = "PlayersTableVersion";
        private static final String COUNTS_TABLE_VERSION_KEY = "CountsTableVersion";
        public static Integer playersTableVersion;
        public static Integer countsTableVersion;

        private static void init() {
            setPathPrefix(PREFIX);
            playersTableVersion = getInteger(PLAYERS_TABLE_VERSION_KEY);
            countsTableVersion = getInteger(COUNTS_TABLE_VERSION_KEY);
        }
    }
}
