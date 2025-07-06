package org.hark7.fishingPlugin.commands.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

/**
 * コマンドハンドラー: upgradepole
 * <p>
 * プレイヤーの釣り竿をアップグレードするコマンドを処理します。
 */
public class UpgradePoleCommand implements ICommandHandler {
    private final FishingPlugin plugin;
    private final PlayerDataController manager;

    /**
     * コンストラクタ
     *
     * @param plugin  FishingPluginのインスタンス
     * @param manager PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public UpgradePoleCommand(FishingPlugin plugin, PlayerDataController manager) {
        if (plugin == null) throw new IllegalArgumentException("FishingPlugin cannot be null");
        if (manager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        this.plugin = plugin;
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
            player.sendMessage(CustomLang.ofComponent("Commands.Invalid.NoHandItem", lang)
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{item}")
                            .replacement(Component.translatable(Material.FISHING_ROD.translationKey()))
                            .build()));
            return;
        }
        var playerUUID = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int playerLevel = manager.getPlayerData(playerUUID).level();
            int upgradeCost = calculateUpgradeCost(fishingRod);

            if (playerLevel < upgradeCost) {
                CustomLang.ofSimpleComponent("Commands.Invalid.NotEnoughFishingLevel", lang)
                        .replace("{level}", playerLevel)
                        .send(player);
                return;
            }

            upgradeFishingRod(fishingRod);
            CustomLang.ofSimpleComponent("Commands.upgradepole.Success", lang).send(player);
        });
    }

    /**
     * アップグレードに必要なレベルを計算します。
     *
     * @param fishingRod
     * @return
     */
    private int calculateUpgradeCost(ItemStack fishingRod) {
        int luckOfTheSeaLevel = fishingRod.getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA);
        int lureLevel = fishingRod.getEnchantmentLevel(Enchantment.LURE);
        return (luckOfTheSeaLevel + lureLevel + 1) * 5;
    }

    /**
     * 釣り竿をアップグレードします。
     * <p>
     * ランダムに「幸運の釣り」または「ルアー」のレベルを1上げます。
     *
     * @param fishingRod アップグレードする釣り竿
     */
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