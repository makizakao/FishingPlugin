package org.hark7.fishingPlugin.type.recipe;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MerchantToolRecipe implements IMerchantRecipe {
    private final MerchantRecipe recipe;
    private final List<Enchantment> enchants;

    private MerchantToolRecipe(MerchantRecipe recipe, List<Enchantment> enchants) {
        this.recipe = recipe;
        this.enchants = enchants;
    }

    @Override
    public MerchantRecipe recipe() {
        applyRandomEnchantment();
        return recipe;
    }

    private void applyRandomEnchantment() {
        if (enchants.isEmpty()) {
            return;
        }
        var rand = new Random();
        var randomEnchantment = enchants.get(rand.nextInt(enchants.size()));
        var level = rand.nextInt(randomEnchantment.getMaxLevel()) + 1;
        var cost = level * rand.nextInt(level);
        var tool = recipe.getResult();
        tool.addEnchantment(randomEnchantment, level);
        Optional.of(recipe.getIngredients())
                .filter(l -> !l.isEmpty())
                .map(List::getFirst)
                .ifPresent(i -> {
                    i.setAmount(i.getAmount() + cost);
                    var ingredients = recipe.getIngredients();
                    ingredients.clear();
                    ingredients.add(tool);
                    recipe.setIngredients(ingredients);
                });
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RecipeBuilder {
        @NonNull
        private final ItemStack result;
        @NonNull
        private final int maxUses;
        private float priceMultiplier;
        private int villagerExperience;
        private List<ItemStack> ingredients;
        private List<Enchantment> enchants;

        public MerchantToolRecipe toolRecipe() {
            var recipe = new MerchantRecipe(result, maxUses);
            recipe.setPriceMultiplier(priceMultiplier);
            ingredients.forEach(recipe::addIngredient);
            recipe.setVillagerExperience(villagerExperience);
            return new MerchantToolRecipe(recipe, enchants);
        }
    }
}
