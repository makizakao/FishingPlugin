package org.hark7.fishingPlugin.database;

import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.Lang;

public class FishExpManager {
    private final PlayerDataManager manager;

    public FishExpManager(PlayerDataManager manager) {
        this.manager = manager;
    }

    /**
     * プレイヤーの経験値を追加します。
     * レベルアップ時にはメッセージを送信します。
     *
     * @param player プレイヤー
     * @param exp    追加する経験値
     */
    public void addExperience(Player player, int exp) {
        var playerUUID = player.getUniqueId();
        var playerData = manager.playerDataMap().get(playerUUID);
        int currentExp = playerData.exp() + exp;
        int currentLevel = playerData.level();

        while (currentExp >= getRequiredExp(currentLevel)) {
            currentExp -= getRequiredExp(currentLevel);
            currentLevel++;
            var message = Lang.ofComponent("FishingLevel.LevelUp", "en")
                    .replace("{level}", String.valueOf(currentLevel));
            message.send(player);
        }
        manager.setPlayerExp(playerUUID, currentExp);
        manager.setPlayerLevel(playerUUID, currentLevel);
    }
    public int getRequiredExp(int level) {
        return 100 * level * level;
    }
}
