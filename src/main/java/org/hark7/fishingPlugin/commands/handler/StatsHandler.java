package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.type.Fishable.*;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

public class StatsHandler implements ICommandHandler {
    private final PlayerDataManager playerDataManager;
    private final FishExpManager fishExpManager;

    public StatsHandler(PlayerDataManager playerDataManager, FishExpManager fishExpManager) {
        this.fishExpManager = fishExpManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean useOnlyOp() {
        return false;
    }

    @Override
    public void execute(Player player, String[] args) {
        var playerUUID = player.getUniqueId();
        var playerData = playerDataManager.getPlayerData(playerUUID);
        int level = playerData.level();
        int exp = playerData.exp();
        int requiredExp = fishExpManager.getRequiredExp(level);
        int total = playerData.countAll();
        CustomLang.ofSimpleComponent("Commands.stats.Title").send(player);
        CustomLang.ofSimpleComponent("Commands.stats.Level").replace("{level}", level).send(player);
        CustomLang.ofSimpleComponent("Commands.stats.Experience")
                .replace("{exp}", exp)
                .replace("{requiredExp}", requiredExp)
                .send(player);
        CustomLang.ofSimpleComponent("Commands.stats.Total").replace("{total}", total).send(player);
        if (args.length == 2 && args[1].equals("all")) {
            CustomLang.ofSimpleComponent("Commands.stats.Scrap")
                    .replace("{total}", playerData.count(Rarity.SCRAP)).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Common")
                    .replace("{total}", playerData.count(Rarity.COMMON)).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Uncommon")
                    .replace("{total}", playerData.count(Rarity.UNCOMMON)).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Rare")
                    .replace("{total}", playerData.count(Rarity.RARE)).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Epic")
                    .replace("{total}", playerData.count(Rarity.EPIC)).send(player);
            CustomLang.ofSimpleComponent("Commands.stats.Legendary")
                    .replace("{total}", playerData.count(Rarity.LEGENDARY)).send(player);
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return List.of("all");
        }

        return List.of();
    }
}