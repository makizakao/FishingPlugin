package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.type.item.Fishable.*;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

/**
 * コマンドハンドラー: stats
 * <p>
 * プレイヤーの統計情報を表示するコマンドを処理します。
 */
public class StatsHandler implements ICommandHandler {
    private final FishingPlugin plugin;
    private final PlayerDataController saveManager;
    private final FishLevelManager fishLevelManager;

    /**
     * コンストラクタ
     *
     * @param plugin           FishingPluginのインスタンス
     * @param saveManager      PlayerDataManagerのインスタンス
     * @param fishLevelManager FishExpManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public StatsHandler(FishingPlugin plugin, PlayerDataController saveManager, FishLevelManager fishLevelManager) {
        if (plugin == null) throw new IllegalArgumentException("FishingPlugin cannot be null");
        if (saveManager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        if (fishLevelManager == null) throw new IllegalArgumentException("FishExpManager cannot be null");
        this.plugin = plugin;
        this.fishLevelManager = fishLevelManager;
        this.saveManager = saveManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        var playerUUID = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            var playerData = saveManager.getPlayerData(playerUUID);
            int level = playerData.level();
            int exp = playerData.exp();
            int requiredExp = fishLevelManager.getRequiredExp(level);
            int total = playerData.countAll();
            CustomLang.ofSimpleComponent("Commands.stats.Title").send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Level").replace("{level}", level).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Experience")
                    .replace("{exp}", exp)
                    .replace("{requiredExp}", requiredExp)
                    .send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Total").replace("{total}", total).send(player);
            if (args.length == 2 && (args[1].equals("all"))) {
                for (var rarity : Rarity.values()) {
                    CustomLang.ofSimpleComponent("Commands.stats." + rarity.key())
                            .replace("{total}", playerData.count(rarity))
                            .send(player);
                }
            }
        });
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return List.of("all");
        }
        return List.of();
    }

    @Override
    public List<String> permList() {
        return List.of();
    }
}