package org.hark7.fishingPlugin.manager;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.hark7.fishingPlugin.type.recipe.IMerchantRecipe;

import java.util.*;

public class VillagerRecipeManager {
    private final Map<VillagerLevel, List<IMerchantRecipe>> recipeTables = new HashMap<>();

    public VillagerRecipeManager() {
        for (var level : VillagerLevel.values()) {
            recipeTables.put(level, new ArrayList<>());
        }
    }

    public void addRecipe(VillagerLevel level, IMerchantRecipe recipe) {
        recipeTables.get(level).add(recipe);
    }

    public List<IMerchantRecipe> getRecipes(VillagerLevel level) {
        return Collections.unmodifiableList(recipeTables.get(level));
    }

    public static boolean isDuplicate(MerchantRecipe recipe, List<MerchantRecipe> recipes) {
        for (var r : recipes) {
            if (r.getResult().isSimilar(recipe.getResult()) &&
                    ingredientsSimilar(r.getIngredients(), recipe.getIngredients())) {
                return true;
            }
        }
        return false;
    }

    private static boolean ingredientsSimilar(List<ItemStack> a, List<ItemStack> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).isSimilar(b.get(i))) return false;
        }
        return true;
    }

    public enum VillagerLevel {
        NOVICE(1, 2),
        APPRENTICE(2, 2),
        JOURNEYMAN(3, 2),
        EXPERT(4, 1),
        MASTER(5, 2);

        @Getter
        private final int level;
        @Getter
        private final int amount;

        VillagerLevel(int level, int amount) {
            this.level = level;
            this.amount = amount;
        }

        public static VillagerLevel fromLevel(int level) {
            for (var v : values()) {
                if (v.level == level) return v;
            }
            throw new IllegalArgumentException("Invalid villager level: " + level);
        }
    }


}
