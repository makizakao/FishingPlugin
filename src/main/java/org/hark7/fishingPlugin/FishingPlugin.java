package org.hark7.fishingPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.*;
import org.hark7.fishingPlugin.database.DatabaseVersionManager;
import org.hark7.fishingPlugin.listener.FishListener;
import org.hark7.fishingPlugin.listener.PlayerPreLoginListener;
import org.hark7.fishingPlugin.listener.VillagerAcquireTradeListener;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.type.Fishable;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FishingPlugin extends SimplePlugin {
    private final FishTable fishTable = new FishTable();
    private PlayerDataManager saveManager;

    /**
     * プラグインの有効化時に呼び出されるメソッド
     * イベントリスナーの登録、設定ファイルの読み込み、レシピの登録を行います。
     */
    @Override
    public void onPluginStart() {
        saveManager = new PlayerDataManager(this);
        var dbVersionManager = new DatabaseVersionManager(this, saveManager);
        dbVersionManager.checkAndMigrate();
        fishTable.initializeFishList();
        Bukkit.getPluginManager().registerEvents(new FishListener(this), this);
        Bukkit.getPluginManager().registerEvents(new VillagerAcquireTradeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPreLoginListener(this), this);
        Recipes.register(this);
        // コマンドの追加
        Optional.ofNullable(getCommand("fishstats"))
                .ifPresent(c -> c.setExecutor(new FishStatsCommand(this)));
        Optional.ofNullable(getCommand("fishtop"))
                .ifPresent(c -> c.setExecutor(new FishTopCommand(this)));
        Optional.ofNullable(getCommand("upgradepole"))
                .ifPresent(c -> c.setExecutor(new UpgradePoleCommand(this)));
        Optional.ofNullable(getCommand("addexp"))
                .ifPresent(c -> c.setExecutor(new AddExpCommand(this)));
        Optional.ofNullable(getCommand("resetlevel"))
                .ifPresent(c -> c.setExecutor(new ResetLevelCommand(this, saveManager)));
        getLogger().info("FishingPlugin has been enabled!");
    }

    /**
     * プラグインの無効化時に呼び出されるメソッド
     * 設定ファイルの保存を行います。
     */
    @Override
    public void onPluginStop() {
        Optional.ofNullable(saveManager).ifPresent(PlayerDataManager::close);
        getLogger().info("FishingPlugin has been disabled!");
    }

    public void addCount(UUID playerUUID, Fishable.Rarity rarity) {
        var playerData = playerDataMap().get(playerUUID);
        int currentCount = playerData.count(rarity) + 1;
        saveManager.setPlayerCount(playerUUID, rarity, currentCount);
    }

    public int getRequiredExp(int level) {
        return 100 * level * level;
    }

    /**
     * プレイヤーのデータを取得します。
     * データが存在しない場合は新規作成します。
     *
     * @param playerUUID プレイヤーのUUID
     * @return PlayerData プレイヤーのデータ
     */
    public PlayerData getPlayerData(UUID playerUUID) {
        // プレイヤーのデータが存在しない場合は新規作成
        if (!playerDataMap().containsKey(playerUUID)) {
            String displayName = Optional.ofNullable(getServer().getPlayer(playerUUID))
                    .map(Player::getName).orElse("");
            PlayerData newData = new PlayerData(displayName, 1, 0);
            saveManager.setPlayerData(playerUUID, newData);
            getLogger().warning("Player " + playerUUID + " does not exists. Created new Data!");
            return newData;
        }
        return playerDataMap().get(playerUUID);
    }

    public List<Fishable> fishList() {
        return fishTable.fishList();
    }

    public Map<UUID, PlayerData> playerDataMap() {
        return saveManager.playerDataMap();
    }

    /**
     * プレイヤーデータを新規作成します。
     * 既にデータが存在する場合は警告を出力します。
     *
     * @param name       プレイヤーの名前
     * @param playerUUID プレイヤーのUUID
     */
    public void createPlayerData(String name, UUID playerUUID) {
        if (!playerDataMap().containsKey(playerUUID)) {
            var newData = new PlayerData(name, 1, 0);
            saveManager.setPlayerData(playerUUID, newData);
        } else {
            getLogger().warning("Player data for " + playerUUID + " already exists.");
        }
    }
}