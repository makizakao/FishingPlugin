package org.hark7.fishingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hark7.fishingPlugin.command.AddExpCommand;
import org.hark7.fishingPlugin.command.FishStatsCommand;
import org.hark7.fishingPlugin.command.FishTopCommand;
import org.hark7.fishingPlugin.command.UpgradePoleCommand;
import org.hark7.fishingPlugin.listener.FishListener;
import org.hark7.fishingPlugin.listener.PlayerPreLoginListener;
import org.hark7.fishingPlugin.listener.VillagerAcquireTradeListener;
import java.util.*;

public class FishingPlugin extends JavaPlugin {
    private final FishItems fishItems = new FishItems();
    private PlayerDataController controller;


    /**
     * プラグインの有効化時に呼び出されるメソッド
     * イベントリスナーの登録、設定ファイルの読み込み、レシピの登録を行います。
     */
    @Override
    public void onEnable() {
        controller = new PlayerDataController(this);
        getServer().getPluginManager().registerEvents(new FishListener(this), this);
        getServer().getPluginManager().registerEvents(new VillagerAcquireTradeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(this), this);
        fishItems.initializeFishList();
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
        getLogger().info("FishingPlugin has been enabled!");
    }

    /**
     * プラグインの無効化時に呼び出されるメソッド
     * 設定ファイルの保存を行います。
     */
    @Override
    public void onDisable() {
        Optional.ofNullable(controller).ifPresent(PlayerDataController::close);
        getLogger().info("FishingPlugin has been disabled!");
    }


    /**
     * プレイヤーの経験値を追加します。
     * レベルアップ時にはメッセージを送信します。
     *
     * @param playerUUID プレイヤーのUUID
     * @param exp 追加する経験値
     */
    public void addExperience(UUID playerUUID, int exp) {
        var playerData = playerDataMap().get(playerUUID);
        int currentExp = playerData.getExp() + exp;
        int currentLevel = playerData.getLevel();

        while (currentExp >= getRequiredExp(currentLevel)) {
            currentExp -= getRequiredExp(currentLevel);
            currentLevel++;
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.GOLD + "釣りレベルが上がりました！ 現在のレベル: " + currentLevel);
            }
        }
        controller.setPlayerExp(playerUUID, currentExp);
        controller.setPlayerLevel(playerUUID, currentLevel);
    }

    public void addCount(UUID playerUUID, CustomFish.Rarity rarity) {
        var playerData = playerDataMap().get(playerUUID);
        int currentCount = playerData.getCount(rarity) + 1;
        controller.setPlayerCount(playerUUID, rarity, currentCount);
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
            controller.setPlayerData(playerUUID, newData);
            getLogger().warning("Player " + playerUUID + " does not exists. Created new Data!");
            return newData;
        }
        return playerDataMap().get(playerUUID);
    }

    public List<CustomFish> fishList() {
        return fishItems.fishList();
    }

    public Map<UUID, PlayerData> playerDataMap() {
        return controller.getPlayerDataMap();
    }

    public void createPlayerData(String name, UUID playerUUID) {
        if (!playerDataMap().containsKey(playerUUID)) {
            var newData = new PlayerData(name,1, 0);
            controller.setPlayerData(playerUUID, newData);
        } else {
            getLogger().warning("Player data for " + playerUUID + " already exists.");
        }
    }
}