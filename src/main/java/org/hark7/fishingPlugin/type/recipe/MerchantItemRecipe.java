package org.hark7.fishingPlugin.type.recipe;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

public class MerchantItemRecipe implements IMerchantRecipe {
    private final MerchantRecipe recipe;

    public MerchantItemRecipe(MerchantRecipe recipe) {
        this.recipe = recipe;
    }


    @Override
    public MerchantRecipe recipe() {
        return recipe;
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

        public MerchantItemRecipe itemRecipe() {
            var recipe = new MerchantRecipe(result, maxUses);
            recipe.setPriceMultiplier(priceMultiplier);
            ingredients.forEach(recipe::addIngredient);
            recipe.setVillagerExperience(villagerExperience);
            return new MerchantItemRecipe(recipe);
        }
    }
}
