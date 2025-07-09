package org.hark7.fishingPlugin.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.hark7.fishingPlugin.FishTable;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.type.item.Fishable;
import org.hark7.fishingPlugin.type.item.Fishable.*;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.*;

/**
 * プレイヤーが魚を釣り上げた際に呼び出され、ランダムな魚を生成してプレイヤーに通知します。
 * また、釣り竿のエンチャントによるボーナス経験値も計算します。
 */
public class FishListener implements Listener {
    private static final float BASE_LEGENDARY_CHANCE = 0.001F; // 基本のレジェンダリー魚の出現確率
    private static final float BASE_EPIC_CHANCE = 0.01F;       // 基本のエピック魚の出現確率
    private static final float BASE_RARE_CHANCE = 0.05F;       // 基本のレア魚の出現確率
    private static final float BASE_UNCOMMON_CHANCE = 0.2F;    // 基本のアンコモン魚の出現確率
    private static final float BASE_COMMON_CHANCE = 0.8F;      // 基本のコモン魚の出現確率
    private final FishingPlugin plugin;
    private final FishLevelManager levelManager;
    private final PlayerDataController saveManager;
    private final FishTable fishTable;

    public FishListener(FishingPlugin plugin, FishLevelManager levelManager, PlayerDataController saveManager,
                        FishTable fishTable) {
        this.plugin = plugin;
        this.levelManager = levelManager;
        this.saveManager = saveManager;
        this.fishTable = fishTable;
    }

    /**
     * プレイヤーが魚を釣り上げた際に呼び出されるイベントハンドラー。
     * 釣り上げた魚の種類をランダムに決定し、プレイヤーに通知します。
     * また、釣り竿のエンチャントによるボーナス経験値も計算します。
     *
     * @param event PlayerFishEvent イベント
     */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            UUID playerUUID = player.getUniqueId();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Fishable caughtFish = getRandomFish(playerUUID, player.getInventory().getItemInMainHand());
                if (caughtFish == null) return;
                event.setExpToDrop(caughtFish.rarity().playerExp());
                caughtFish.onFish(event, levelManager, plugin);

                int baseExp = caughtFish.rarity().exp();
                int bonusExp = calculateBonusExp(player.getInventory().getItemInMainHand());
                int totalExp = baseExp + bonusExp;
                player.sendMessage(CustomLang.ofComponent("Fishing.OnFish")
                        .replaceText(TextReplacementConfig.builder()
                                .matchLiteral("{fish}")
                                .replacement(caughtFish.name().color(caughtFish.rarity().color()))
                                .build()));
                CustomLang.ofSimpleComponent("Fishing.OmGetExp")
                        .replace("{exp}", totalExp)
                        .replace("{bonus}", bonusExp)
                        .send(player);
                levelManager.addExperience(player, totalExp);
                addCount(playerUUID, caughtFish.rarity());
            });
        }
    }

    public void addCount(UUID playerUUID, Fishable.Rarity rarity) {
        var playerData = saveManager.playerDataMap().get(playerUUID);
        int currentCount = playerData.count(rarity) + 1;

        saveManager.savePlayerCount(playerUUID, rarity, currentCount);
    }

    /**
     * 釣り竿のエンチャントからボーナス経験値を計算します。
     * 「宝釣り」と「入れ食い」のレベルに応じて経験値が増加します。
     *
     * @param fishingRod 釣り竿のアイテムスタック
     * @return ボーナス経験値
     */
    private int calculateBonusExp(ItemStack fishingRod) {
        if (fishingRod.getType() != Material.FISHING_ROD) return 0;

        int luckOfTheSeaLevel = fishingRod.getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA);
        int lureLevel = fishingRod.getEnchantmentLevel(Enchantment.LURE);

        return (luckOfTheSeaLevel * 5) + (lureLevel * 3);
    }

    /**
     * プレイヤーのレベルと釣り竿のエンチャントからレアリティボーナスを計算します。
     * プレイヤーのレベルが高いほど、レアリティの確率が上昇します。
     *
     * @param playerLevel プレイヤーのレベル
     * @param fishingRod  釣り竿のアイテムスタック
     * @return レアリティボーナス
     */
    private double calculateRarityBonus(int playerLevel, ItemStack fishingRod) {
        int luckOfTheSeaLevel = fishingRod.getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA);
        return playerLevel * 0.001 + luckOfTheSeaLevel * 0.005;
    }

    /**
     * ランダムな魚を取得します。
     * プレイヤーのレベルに応じてレアリティの確率が変動します。
     *
     * @param playerUUID プレイヤーのUUID
     * @return ランダムに選ばれた魚
     */
    private Fishable getRandomFish(UUID playerUUID, ItemStack fishingRod) {
        int playerLevel = saveManager.playerDataMap().get(playerUUID).level();
        double rarityBonus = calculateRarityBonus(playerLevel, fishingRod);
        double random = Math.random();
        Rarity selectedRarity = getRarity(random, rarityBonus);

        List<Fishable> fishOfSelectedRarity = fishTable.fishList().stream()
                .filter(fish -> fish.rarity() == selectedRarity)
                .toList();
        return fishOfSelectedRarity.get(new Random().nextInt(fishOfSelectedRarity.size()));
    }

    /**
     * ランダムな値に基づいてレアリティを決定します。
     * レアリティの確率はプレイヤーのレベルと釣り竿のエンチャントによって変動します。
     *
     * @param random      ランダムな値
     * @param rarityBonus レアリティボーナス
     * @return 選択されたレアリティ
     */
    private static Rarity getRarity(double random, double rarityBonus) {
        Rarity selectedRarity;
        if (random < (BASE_LEGENDARY_CHANCE + rarityBonus)) selectedRarity = Rarity.LEGENDARY;
        else if (random < (BASE_EPIC_CHANCE + rarityBonus)) selectedRarity = Rarity.EPIC;
        else if (random < (BASE_RARE_CHANCE + rarityBonus)) selectedRarity = Rarity.RARE;
        else if (random < (BASE_UNCOMMON_CHANCE + rarityBonus)) selectedRarity = Rarity.UNCOMMON;
        else if (random < (BASE_COMMON_CHANCE + rarityBonus)) selectedRarity = Rarity.COMMON;
        else selectedRarity = Rarity.SCRAP;
        return selectedRarity;
    }
}
