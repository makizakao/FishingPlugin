package org.hark7.fishingPlugin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.CustomFish.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FishStatsCommand implements CommandExecutor {
    private final FishingPlugin plugin;

    public FishStatsCommand(FishingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        var playerData = plugin.playerDataMap().get(playerUUID);
        int level = playerData.getLevel();
        int exp = playerData.getExp();
        int requiredExp = plugin.getRequiredExp(level);

        player.sendMessage(ChatColor.YELLOW + "===== 釣り統計 =====");
        player.sendMessage(ChatColor.GREEN + "レベル: " + level);
        player.sendMessage(ChatColor.GREEN + "経験値: " + exp + "/" + requiredExp);
        player.sendMessage(ChatColor.GREEN + "SCRAP: " + playerData.getCount(Rarity.SCRAP));
        player.sendMessage(ChatColor.GREEN + "COMMON: " + playerData.getCount(Rarity.COMMON));
        player.sendMessage(ChatColor.GREEN + "UNCOMMON: " + playerData.getCount(Rarity.UNCOMMON));
        player.sendMessage(ChatColor.GREEN + "RARE: " + playerData.getCount(Rarity.RARE));
        player.sendMessage(ChatColor.GREEN + "EPIC: " + playerData.getCount(Rarity.EPIC));
        player.sendMessage(ChatColor.GREEN + "LEGENDARY: " + playerData.getCount(Rarity.LEGENDARY));
        // Add more statistics here if needed

        return true;
    }
}