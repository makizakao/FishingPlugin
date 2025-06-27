package org.hark7.fishingPlugin.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.hark7.fishingPlugin.CustomFish.*;
import org.hark7.fishingPlugin.FishingPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * VillagerAcquireTradeListener
 * <p>
 * このクラスは、村人が新しい取引を獲得した際に呼び出されるイベントリスナーです。
 * 取引が獲得されたときに特定の処理を実行するために使用されます。
 */
public class VillagerAcquireTradeListener implements Listener {
    private static final List<MerchantRecipe> NOVICE_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> APPRENTICE_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> JOURNEYMAN_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> EXPERT_RECIPE_TABLE = new ArrayList<>();
    private static final List<MerchantRecipe> MASTER_RECIPE_TABLE = new ArrayList<>();
    private static final Enchantment[] fishRodEnchantments = new Enchantment[] {
            Enchantment.LUCK_OF_THE_SEA,
            Enchantment.LURE,
            Enchantment.UNBREAKING,
            Enchantment.MENDING,
    };

    /**
     * レシピテーブルを設定します。
     * @param plugin FishingPluginのインスタンス
     */
    public VillagerAcquireTradeListener(FishingPlugin plugin) {
        // 新米漁師のレシピテーブルにレシピを追加
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.COD, 15));
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.SALMON, 15));
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 2,
                new ItemStack(Material.EMERALD), new ItemStack(Material.STRING, 20));
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 2,
                new ItemStack(Material.EMERALD), new ItemStack(Material.COAL, 10));
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 1,
                new ItemStack(Material.COD_BUCKET), new ItemStack(Material.EMERALD, 3));
        addRecipeToTable(NOVICE_RECIPE_TABLE, 16, 0.05f, 1, new ItemStack(Material.COOKED_COD),
                new ItemStack(Material.COD, 6), new ItemStack(Material.EMERALD));
        // 見習い漁師のレシピテーブルにレシピを追加
        addRecipeToTable(APPRENTICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.COD, 15));
        addRecipeToTable(APPRENTICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.SALMON, 15));
        addRecipeToTable(APPRENTICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.CAMPFIRE), new ItemStack(Material.EMERALD, 2));
        addRecipeToTable(APPRENTICE_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.COOKED_SALMON),
                new ItemStack(Material.SALMON, 20), new ItemStack(Material.EMERALD));
        plugin.fishList().stream().filter(i -> i.rarity == Rarity.RARE)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack();
                    itemStack.setAmount(5);
                    addRecipeToTable(APPRENTICE_RECIPE_TABLE, 16, 0.05f, 10,
                            new ItemStack(Material.EMERALD), itemStack);
                    addRecipeToTable(JOURNEYMAN_RECIPE_TABLE, 16, 0.05f, 10,
                            new ItemStack(Material.EMERALD), itemStack);
                });
        // 一人前漁師のレシピテーブルにレシピを追加
        addRecipeToTable(JOURNEYMAN_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.COD, 15));
        addRecipeToTable(JOURNEYMAN_RECIPE_TABLE, 16, 0.05f, 5,
                new ItemStack(Material.EMERALD), new ItemStack(Material.SALMON, 15));
        Arrays.stream(fishRodEnchantments).forEach(enchant -> {
            var itemStack = new ItemStack(Material.FISHING_ROD);
            var rand = new Random();
            var level = rand.nextInt(enchant.getMaxLevel()) + 1;
            itemStack.addEnchantment(enchant, level);
            addRecipeToTable(JOURNEYMAN_RECIPE_TABLE, 3, 0.02f, 5,
                    itemStack, new ItemStack(Material.EMERALD, 8 + (level - 1) * 7));
        });
        plugin.fishList().stream().filter(i -> i.rarity == Rarity.EPIC)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack();
                    addRecipeToTable(JOURNEYMAN_RECIPE_TABLE, 8, 0.05f, 20,
                            new ItemStack(Material.EMERALD), itemStack);
                    addRecipeToTable(EXPERT_RECIPE_TABLE, 8, 0.05f, 20,
                            new ItemStack(Material.EMERALD), itemStack);
                });
        // 熟練漁師のレシピテーブルにレシピを追加
        addRecipeToTable(EXPERT_RECIPE_TABLE, 12, 0.05f, 30,
                new ItemStack(Material.EMERALD), new ItemStack(Material.TROPICAL_FISH, 6));
        // マスター漁師のレシピテーブルにレシピを追加
        addRecipeToTable(MASTER_RECIPE_TABLE, 12, 0.05f, 50,
                new ItemStack(Material.EMERALD), new ItemStack(Material.PUFFERFISH, 4));
        plugin.fishList().stream().filter(i -> i.rarity == Rarity.LEGENDARY)
                .filter(i -> i.material == Material.COD)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack();
                    addRecipeToTable(MASTER_RECIPE_TABLE, 3, 0.05f, 30,
                            new ItemStack(Material.EMERALD, 5), itemStack);
                });
    }

    // レシピをテーブルに追加するメソッド
    private void addRecipeToTable(List<MerchantRecipe> table, int maxUses, float priceMul, int exp,
                                  ItemStack output, ItemStack... inputs) {
        MerchantRecipe recipe = new MerchantRecipe(output, maxUses);
        for (ItemStack input : inputs) {
            recipe.addIngredient(input);
        }
        recipe.setPriceMultiplier(priceMul);
        recipe.setVillagerExperience(exp);
        table.add(recipe);
    }


    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        // 村人が漁師の取引を獲得した場合の処理
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (!villager.getProfession().equals(Villager.Profession.FISHERMAN)) return;
        var rand = new Random();
        var recipes = new ArrayList<>(villager.getRecipes());
        switch (villager.getVillagerLevel()) {
            case 1:
                // 新米漁師のレシピをランダムに選択
                for (int i = 0; i < 2; i++) {
                    while (true) {
                        var noviceRecipe = NOVICE_RECIPE_TABLE.get(rand.nextInt(NOVICE_RECIPE_TABLE.size()));
                        var exp = Math.max(2, noviceRecipe.getVillagerExperience());
                        noviceRecipe.setVillagerExperience(exp);
                        if (checkExistRecipe(noviceRecipe, recipes)) continue;
                        recipes.add(noviceRecipe);
                        break;
                    }
                }
                villager.setRecipes(recipes);
                break;
            case 2:
                // 見習い漁師のレシピをランダムに選択
                for (int i = 0; i < 2; i++) {
                    while (true) {
                        var apprenticeRecipe = APPRENTICE_RECIPE_TABLE.get(rand.nextInt(APPRENTICE_RECIPE_TABLE.size()));
                        if (checkExistRecipe(apprenticeRecipe, recipes)) continue;
                        recipes.add(apprenticeRecipe);
                        break;
                    }
                }
                villager.setRecipes(recipes);
                break;
            case 3:
                // 一人前漁師のレシピをランダムに選択
                for (int i = 0; i < 2; i++) {
                    while (true) {
                        var journeymanRecipe = JOURNEYMAN_RECIPE_TABLE.get(rand.nextInt(JOURNEYMAN_RECIPE_TABLE.size()));
                        if (checkExistRecipe(journeymanRecipe, recipes)) continue;
                        recipes.add(journeymanRecipe);
                        break;
                    }
                }
                villager.setRecipes(recipes);
                break;
            case 4:
                // 熟練漁師のレシピをランダムに選択
                while (true) {
                    var expertRecipe = EXPERT_RECIPE_TABLE.get(rand.nextInt(EXPERT_RECIPE_TABLE.size()));
                    if (checkExistRecipe(expertRecipe, recipes)) continue;
                    recipes.add(expertRecipe);
                    break;
                }
                villager.setRecipes(recipes);
                break;
            case 5:
                // マスター漁師のレシピをランダムに選択
                for (int i = 0; i < 2; i++) {
                    while (true) {
                        var masterRecipe = MASTER_RECIPE_TABLE.get(rand.nextInt(MASTER_RECIPE_TABLE.size()));
                        if (checkExistRecipe(masterRecipe, recipes)) continue;
                        recipes.add(masterRecipe);
                        break;
                    }
                }
                villager.setRecipes(recipes);
                break;
        }
        event.setCancelled(true);
    }

    // 既存のレシピと同じかどうかをチェックするメソッド
    private boolean checkExistRecipe(MerchantRecipe recipe, List<MerchantRecipe> recipes) {
        for (MerchantRecipe r : recipes) {
            if (r.getResult().isSimilar(recipe.getResult()) && r.getIngredients().equals(recipe.getIngredients())) {
                return true;
            }
        }
        return false;
    }
}