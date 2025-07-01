package org.hark7.fishingPlugin.database;

import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.Settings;

public class DatabaseVersionManager {
    private final FishingPlugin plugin;
    private final PlayerDataManager playerDataManager;
    public static final int CURRENT_PLAYERS_TABLE_VERSION = 1;
    public static final int CURRENT_COUNTS_TABLE_VERSION = 1;

    public DatabaseVersionManager(FishingPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void checkAndMigrate() {
        int currentPlayersTableVersion = Settings.DatabaseVersion.playersTableVersion;
        int currentCountsTableVersion = Settings.DatabaseVersion.countsTableVersion;

        if (currentPlayersTableVersion < CURRENT_PLAYERS_TABLE_VERSION) {
            plugin.getLogger().info("Updating players table from version " + currentPlayersTableVersion + " to " + CURRENT_PLAYERS_TABLE_VERSION);
            playerDataManager.updatePlayersTableVersion();
            Settings.DatabaseVersion.playersTableVersion = CURRENT_PLAYERS_TABLE_VERSION;
        }

        if (currentCountsTableVersion < CURRENT_COUNTS_TABLE_VERSION) {
            plugin.getLogger().info("Updating counts table from version " + currentCountsTableVersion + " to " + CURRENT_COUNTS_TABLE_VERSION);
            playerDataManager.updateCountsTableVersion();
            Settings.DatabaseVersion.countsTableVersion = CURRENT_COUNTS_TABLE_VERSION;
        }

        plugin.getLogger().info("Current players table version: " + currentPlayersTableVersion);
        plugin.getLogger().info("Current counts table version: " + currentCountsTableVersion);
    }
}
