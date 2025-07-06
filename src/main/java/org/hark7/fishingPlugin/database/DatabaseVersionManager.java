package org.hark7.fishingPlugin.database;

import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.Settings;

/**
 * FishingPluginのデータベースバージョン管理クラス
 * <p>
 * このクラスは、データベースのバージョンを管理し、必要に応じてマイグレーションを行います。
 */
public class DatabaseVersionManager {
    private final FishingPlugin plugin;
    private final PlayerDataController PlayerDataController;
    public static final int CURRENT_PLAYERS_TABLE_VERSION = 1;
    public static final int CURRENT_COUNTS_TABLE_VERSION = 1;

    /**
     * コンストラクタ
     *
     * @param plugin               FishingPluginのインスタンス
     * @param PlayerDataController PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException pluginまたはplayerDataManagerがnullの場合
     */
    public DatabaseVersionManager(FishingPlugin plugin, PlayerDataController PlayerDataController) {
        if (plugin == null) throw new IllegalArgumentException("plugin cannot be null");
        if (PlayerDataController == null) throw new IllegalArgumentException("playerDataManager cannot be null");
        this.plugin = plugin;
        this.PlayerDataController = PlayerDataController;
    }

    /**
     * データベースのバージョンをチェックし、必要に応じてマイグレーションを行います。
     * <p>
     * 現在のバージョンと最新バージョンを比較し、必要な場合はマイグレーションを実行します。
     */
    public void checkAndMigrate() {
        int currentPlayersTableVersion = Settings.Database.playersTableVersion;
        int currentCountsTableVersion = Settings.Database.countsTableVersion;

        if (currentPlayersTableVersion < CURRENT_PLAYERS_TABLE_VERSION) {
            plugin.getLogger().info("Updating players table from version " + currentPlayersTableVersion + " to " + CURRENT_PLAYERS_TABLE_VERSION);
            PlayerDataController.updatePlayersTableVersion();
            Settings.Database.playersTableVersion = CURRENT_PLAYERS_TABLE_VERSION;
        }

        if (currentCountsTableVersion < CURRENT_COUNTS_TABLE_VERSION) {
            plugin.getLogger().info("Updating counts table from version " + currentCountsTableVersion + " to " + CURRENT_COUNTS_TABLE_VERSION);
            PlayerDataController.updateCountsTableVersion();
            Settings.Database.countsTableVersion = CURRENT_COUNTS_TABLE_VERSION;
        }

        plugin.getLogger().info("Current players table version: " + currentPlayersTableVersion);
        plugin.getLogger().info("Current counts table version: " + currentCountsTableVersion);
    }
}
