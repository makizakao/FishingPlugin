package org.hark7.fishingPlugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.manager.VillagerRecipeManager;
import org.hark7.fishingPlugin.manager.VillagerRecipeManager.VillagerLevel;
import org.hark7.fishingPlugin.type.recipe.IMerchantRecipe;

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
    private final VillagerRecipeManager manager;
    private final FishingPlugin plugin;

    /**
     * レシピテーブルを設定します。
     *
     * @param plugin  FishingPluginのインスタンス
     * @param manager 村人の取引レシピを管理するマネージャー
     */
    public VillagerAcquireTradeListener(FishingPlugin plugin, VillagerRecipeManager manager) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        // 村人が漁師の取引を獲得した場合の処理
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (!villager.getProfession().equals(Villager.Profession.FISHERMAN)) return;

        var rand = new Random();
        var level = VillagerLevel.fromLevel(villager.getVillagerLevel());
        setRecipes(manager.getRecipes(level), villager, rand, level.getAmount());
        event.setCancelled(true);
    }

    private void setRecipes(List<IMerchantRecipe> table, Villager villager, Random rand, int count) {
        var newRecipes = new ArrayList<>(villager.getRecipes());
        var candidates = new ArrayList<MerchantRecipe>();
        for (var recipe : table) {
            if (!VillagerRecipeManager.isDuplicate(recipe.recipe(), newRecipes)) candidates.add(recipe.recipe());
        }
        int addCount = Math.min(count, candidates.size());
        for (int i = 0; i < addCount; i++) {
            var recipe = candidates.remove(rand.nextInt(candidates.size()));
            if (VillagerRecipeManager.isDuplicate(recipe, newRecipes)) continue;
            newRecipes.add(recipe);
        }
        Bukkit.getScheduler().runTask(plugin, () -> villager.setRecipes(newRecipes));
    }
}