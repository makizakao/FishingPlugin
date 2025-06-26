package org.hark7.fishingPlugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.hark7.fishingPlugin.CustomFish;
import org.hark7.fishingPlugin.CustomFish.*;
import org.hark7.fishingPlugin.FishingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * VillagerAcquireTradeListener
 * <p>
 * このクラスは、村人が新しい取引を獲得した際に呼び出されるイベントリスナーです。
 * 取引が獲得されたときに特定の処理を実行するために使用されます。
 */
public class VillagerAcquireTradeListener implements Listener {
    private FishingPlugin plugin;
    private static final List<MerchantRecipe> NOVICE_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> APPRENTICE_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> JOURNEYMAN_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> EXPERT_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> MASTER_RECIPE_TABLE = new ArrayList<>();

    /**
     * レシピテーブルを設定します。
     * @param plugin FishingPluginのインスタンス
     */
    public VillagerAcquireTradeListener(FishingPlugin plugin) {
        this.plugin = plugin;
        // 新米漁師のレシピテーブルにレシピを追加
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.COD, 15));
    }

    private void addRecipeToTable(List<MerchantRecipe> table, int maxUses, float priceMul, int exp,
                                  ItemStack output, ItemStack... inputs) {
        // レシピをテーブルに追加するヘルパーメソッド
        MerchantRecipe recipe = new MerchantRecipe(output, maxUses);
        for (ItemStack input : inputs) {
            recipe.addIngredient(input);
        }
        recipe.setPriceMultiplier(priceMul);
        recipe.setVillagerExperience();
        table.add(recipe);
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        // 村人が漁師の取引を獲得した場合の処理
        /*
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (!villager.getProfession().equals(Villager.Profession.FISHERMAN)) return;
        var rand = new Random();
        var emerald = new ItemStack(Material.EMERALD);
        CustomFish[] fishArr = plugin.fishItems.fishList.stream()
                .filter(f -> f.rarity == Rarity.RARE)
                .toArray(CustomFish[]::new);
        if (fishArr.length == 0) return;

        var fish = fishArr[rand.nextInt(fishArr.length)].createItemStack();
        fish.setAmount(5);
        var recipe = new MerchantRecipe(emerald, 10);
        recipe.addIngredient(fish);
        recipe.setExperienceReward(true);
        recipe.setPriceMultiplier(0.05F);
        villager.setRecipe(villager.getRecipeCount() -1 , recipe); */
    }
}