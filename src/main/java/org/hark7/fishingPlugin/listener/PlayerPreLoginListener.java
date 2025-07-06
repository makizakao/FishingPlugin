package org.hark7.fishingPlugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataController;

import java.util.UUID;

/**
 * プレイヤーのログイン前にデータを初期化するリスナー
 * <p>
 * プレイヤーが初めてログインする際に、データベースにプレイヤーデータを作成します。
 */
public class PlayerPreLoginListener implements Listener {
    FishingPlugin plugin;
    PlayerDataController manager;

    /**
     * コンストラクタ
     *
     * @param plugin  FishingPluginのインスタンス
     * @param manager PlayerDataManagerのインスタンス
     */
    public PlayerPreLoginListener(FishingPlugin plugin, PlayerDataController manager) {
        if (plugin == null) throw new IllegalArgumentException("plugin must not be null");
        if (manager == null) throw new IllegalArgumentException("manager must not be null");
        this.plugin = plugin;
        this.manager = manager;
    }

    /**
     * プレイヤーがログインする前に呼び出されるイベントハンドラー
     * <p>
     * プレイヤーのUUIDと名前を取得し、データベースにプレイヤーデータが存在しない場合は新しいデータを作成します。
     *
     * @param event AsyncPlayerPreLoginEvent イベント
     */
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        var playerUUID = event.getUniqueId();
        String name = event.getName();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!existPlayerData(playerUUID)) savePlayerData(name, playerUUID);
        });
    }

    /**
     * プレイヤーデータをデータベースに保存します。
     *
     * @param name       プレイヤーの名前
     * @param playerUUID プレイヤーのUUID
     */
    private void savePlayerData(String name, UUID playerUUID) {
        var playerData = new PlayerData(name, 1, 0);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> manager.insertPlayerData(playerUUID, playerData));
    }

    /**
     * プレイヤーデータがデータベースに存在するかどうかを確認します。
     *
     * @param playerUUID プレイヤーのUUID
     * @return プレイヤーデータが存在する: true 存在しない: false
     */
    private boolean existPlayerData(java.util.UUID playerUUID) {
        return manager.playerDataMap().containsKey(playerUUID);
    }
}
