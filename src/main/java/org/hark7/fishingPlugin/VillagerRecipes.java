package org.hark7.fishingPlugin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.hark7.fishingPlugin.manager.VillagerRecipeManager;
import org.hark7.fishingPlugin.type.item.Fishable;
import org.hark7.fishingPlugin.type.item.ItemFish;
import org.hark7.fishingPlugin.type.recipe.MerchantItemRecipe;
import org.hark7.fishingPlugin.type.recipe.MerchantToolRecipe;

import java.util.List;

public class VillagerRecipes {
    public static VillagerRecipeManager createRecipeManager(List<Fishable> fishList) {
        var manager = new VillagerRecipeManager();
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(5)
                .ingredients(List.of(new ItemStack(Material.COD, 15)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(5)
                .ingredients(List.of(new ItemStack(Material.SALMON, 15)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(2)
                .ingredients(List.of(new ItemStack(Material.STRING, 20)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(2)
                .ingredients(List.of(new ItemStack(Material.COAL, 2)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.COD_BUCKET))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(1)
                .ingredients(List.of(new ItemStack(Material.EMERALD, 3)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.NOVICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.COOKED_COD, 6))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(1)
                .ingredients(List.of(new ItemStack(Material.COD, 6), new ItemStack(Material.EMERALD)))
                .build().itemRecipe());
        // 見習い漁師のレシピテーブルにレシピを追加
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.APPRENTICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(5)
                .ingredients(List.of(new ItemStack(Material.COD, 15)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.APPRENTICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(5)
                .ingredients(List.of(new ItemStack(Material.SALMON, 15)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.APPRENTICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.CAMPFIRE))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(5)
                .ingredients(List.of(new ItemStack(Material.EMERALD, 2)))
                .build().itemRecipe());
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.APPRENTICE, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.COOKED_SALMON, 6))
                .maxUses(16)
                .priceMultiplier(0.05f)
                .villagerExperience(1)
                .ingredients(List.of(new ItemStack(Material.SALMON, 6), new ItemStack(Material.EMERALD)))
                .build().itemRecipe());
        fishList.stream()
                .filter(i -> i.rarity() == Fishable.Rarity.RARE)
                .filter(i -> i.fishType() == Fishable.FishType.Fish)
                .filter(i -> i instanceof ItemFish)
                .map(i -> (ItemFish) i)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack(5);
                    manager.addRecipe(VillagerRecipeManager.VillagerLevel.APPRENTICE, MerchantItemRecipe.RecipeBuilder.builder()
                            .result(new ItemStack(Material.EMERALD))
                            .maxUses(16)
                            .priceMultiplier(0.05f)
                            .villagerExperience(10)
                            .ingredients(List.of(itemStack))
                            .build().itemRecipe());
                    manager.addRecipe(VillagerRecipeManager.VillagerLevel.JOURNEYMAN, MerchantItemRecipe.RecipeBuilder.builder()
                            .result(new ItemStack(Material.EMERALD))
                            .maxUses(16)
                            .priceMultiplier(0.05f)
                            .villagerExperience(10)
                            .ingredients(List.of(itemStack))
                            .build().itemRecipe());
                });
        // 一人前漁師のレシピテーブルにレシピを追加
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.JOURNEYMAN, MerchantToolRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.FISHING_ROD, 1))
                .maxUses(3)
                .priceMultiplier(0.02f)
                .villagerExperience(10)
                .ingredients(List.of(new ItemStack(Material.EMERALD, 8)))
                .enchants(List.of(
                        Enchantment.LUCK_OF_THE_SEA,
                        Enchantment.LURE,
                        Enchantment.UNBREAKING,
                        Enchantment.MENDING
                ))
                .build().toolRecipe());
        fishList.stream()
                .filter(i -> i.rarity() == Fishable.Rarity.EPIC)
                .filter(i -> i.fishType() == Fishable.FishType.Fish)
                .filter(i -> i instanceof ItemFish)
                .map(i -> (ItemFish) i)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack(1);
                    manager.addRecipe(VillagerRecipeManager.VillagerLevel.JOURNEYMAN, MerchantItemRecipe.RecipeBuilder.builder()
                            .result(new ItemStack(Material.EMERALD))
                            .maxUses(8)
                            .priceMultiplier(0.05f)
                            .villagerExperience(20)
                            .ingredients(List.of(itemStack))
                            .build().itemRecipe());
                    manager.addRecipe(VillagerRecipeManager.VillagerLevel.EXPERT, MerchantItemRecipe.RecipeBuilder.builder()
                            .result(new ItemStack(Material.EMERALD))
                            .maxUses(8)
                            .priceMultiplier(0.05f)
                            .villagerExperience(20)
                            .ingredients(List.of(itemStack))
                            .build().itemRecipe());
                });
        // 熟練漁師のレシピテーブルにレシピを追加
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.EXPERT, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(12)
                .priceMultiplier(0.05f)
                .villagerExperience(30)
                .ingredients(List.of(new ItemStack(Material.TROPICAL_FISH, 6)))
                .build().itemRecipe());
        // マスター漁師のレシピテーブルにレシピを追加
        manager.addRecipe(VillagerRecipeManager.VillagerLevel.MASTER, MerchantItemRecipe.RecipeBuilder.builder()
                .result(new ItemStack(Material.EMERALD))
                .maxUses(12)
                .priceMultiplier(0.05f)
                .villagerExperience(30)
                .ingredients(List.of(new ItemStack(Material.PUFFERFISH, 4)))
                .build().itemRecipe());
        fishList.stream().filter(i -> i.rarity() == Fishable.Rarity.LEGENDARY)
                .filter(i -> i.fishType() == Fishable.FishType.Fish)
                .filter(i -> i instanceof ItemFish)
                .map(i -> (ItemFish) i)
                .forEach(fish -> {
                    var itemStack = fish.createItemStack(1);
                    manager.addRecipe(VillagerRecipeManager.VillagerLevel.MASTER, MerchantItemRecipe.RecipeBuilder.builder()
                            .result(new ItemStack(Material.EMERALD, 5))
                            .maxUses(3)
                            .priceMultiplier(0.05f)
                            .villagerExperience(50)
                            .ingredients(List.of(itemStack))
                            .build().itemRecipe());
                });
        return manager;
    }
}
