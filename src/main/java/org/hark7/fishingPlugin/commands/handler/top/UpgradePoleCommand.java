package org.hark7.fishingPlugin.commands.handler.top;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.commands.handler.ICommandHandler;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

public class UpgradePoleCommand implements ICommandHandler {
    private final PlayerDataManager manager;

    public UpgradePoleCommand(PlayerDataManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean useOnlyOp() {
        return false;
    }

    @Override
    public void execute(Player player, String[] args) {
        ItemStack fishingRod = player.getInventory().getItemInMainHand();
        var lang = player.locale().toLanguageTag();
        if (fishingRod.getType() != Material.FISHING_ROD) {
            player.sendMessage(CustomLang.ofComponent("Commands.upgradepole.InvalidItem", lang)
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{item}")
                            .replacement(Component.translatable(Material.FISHING_ROD.translationKey()))
                            .build()));
            return;
        }
        var playerUUID = player.getUniqueId();
        int playerLevel = manager.getPlayerData(playerUUID).level();
        int upgradeCost = calculateUpgradeCost(fishingRod);

        if (playerLevel < upgradeCost) {
            CustomLang.ofSimpleComponent("Commands.Invalid.NotEnoughLevel", lang)
                    .replace("{level}", playerLevel)
                    .send(player);
            return;
        }

        upgradeFishingRod(fishingRod);
        CustomLang.ofSimpleComponent("Commands.upgradepole.Success", lang).send(player);
    }

    private int calculateUpgradeCost(ItemStack fishingRod) {
        int luckOfTheSeaLevel = fishingRod.getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA);
        int lureLevel = fishingRod.getEnchantmentLevel(Enchantment.LURE);
        return (luckOfTheSeaLevel + lureLevel + 1) * 5;
    }

    private void upgradeFishingRod(ItemStack fishingRod) {
        ItemMeta meta = fishingRod.getItemMeta();
        if (meta == null) return;

        if (Math.random() < 0.5) {
            int currentLevel = meta.getEnchantLevel(Enchantment.LUCK_OF_THE_SEA);
            meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, currentLevel + 1, true);
        } else {
            int currentLevel = meta.getEnchantLevel(Enchantment.LURE);
            meta.addEnchant(Enchantment.LURE, currentLevel + 1, true);
        }

        fishingRod.setItemMeta(meta);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return List.of();
    }
}