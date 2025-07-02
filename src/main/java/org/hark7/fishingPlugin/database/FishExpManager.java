package org.hark7.fishingPlugin.database;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

            Lang.ofComponent("FishingLevel.LevelUp", );
            player.sendMessage(Component
                    .text("釣りレベルが上がりました！ 現在のレベル: ")
                    .append(Component.text(currentLevel))
                    .color(NamedTextColor.GOLD));
        }
        manager.setPlayerExp(playerUUID, currentExp);
        manager.setPlayerLevel(playerUUID, currentLevel);
    }
    public int getRequiredExp(int level) {
        return 100 * level * level;
    }
}
