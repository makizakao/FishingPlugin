package org.hark7.fishingPlugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;

/**
 * プレイヤーの釣りレベルと経験値を管理するクラス。
 * <p>
 * このクラスは、プレイヤーの釣りレベルと経験値を管理し、レベルアップ時にメッセージを送信します。
 */
public class FishLevelManager {
    private final FishingPlugin plugin;
    private final PlayerDataController manager;

    /**
     * コンストラクタ
     *
     * @param plugin  FishingPluginのインスタンス
     * @param manager PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException managerがnullの場合
     */
    public FishLevelManager(FishingPlugin plugin, PlayerDataController manager) {
        if (plugin == null) throw new IllegalArgumentException("FishingPlugin cannot be null");
        if (manager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        this.manager = manager;
        this.plugin = plugin;
    }

    /**
     * プレイヤーの経験値を追加します。
     * レベルアップ時にはメッセージを送信します。
     *
     * @param player プレイヤー
     * @param exp    追加する経験値
     */
    public void addExperience(Player player, int exp) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            var playerUUID = player.getUniqueId();
            var playerData = manager.playerDataMap().get(playerUUID);
            var lang = player.locale().toLanguageTag();
            int currentExp = playerData.exp() + exp;
            int currentLevel = playerData.level();

            while (currentExp >= getRequiredExp(currentLevel)) {
                currentExp -= getRequiredExp(currentLevel);
                currentLevel++;

                CustomLang.ofSimpleComponent("FishingLevel.LevelUp", lang)
                        .replace("{level}", String.valueOf(currentLevel))
                        .send(player);
            }
            final int finalCurrentExp = currentExp;
            final int finalCurrentLevel = currentLevel;
            manager.savePlayerExp(playerUUID, finalCurrentExp);
            manager.savePlayerLevel(playerUUID, finalCurrentLevel);
        });
    }

    public int getPlayerLevel(Player player) {
        return manager.getPlayerData(player.getUniqueId()).level();
    }

    public int getPlayerExp(Player player) {
        return manager.getPlayerData(player.getUniqueId()).exp();
    }

    /**
     * 指定されたレベルに必要な経験値を計算します。
     *
     * @param level レベル
     * @return 必要な経験値
     */
    public int getRequiredExp(int level) {
        return 100 * level * level;
    }
}
