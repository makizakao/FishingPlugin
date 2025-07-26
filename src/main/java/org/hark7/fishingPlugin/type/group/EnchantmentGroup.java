package org.hark7.fishingPlugin.type.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.bukkit.enchantments.Enchantment;
import org.hark7.fishingPlugin.type.item.Fishable;

import java.util.List;

@SuperBuilder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EnchantmentGroup {
    @Getter
    @NonNull
    private Enchantment enchantment;
    @Getter
    private int minFishLevel;
    @Getter
    private int maxEnchantLevel;
    @Getter
    private int increment;
    @Getter
    private float chance;

    public abstract static class EnchantmentGroupBuilder<C extends EnchantmentGroup, B extends EnchantmentGroupBuilder<C, B>> {
        protected int increment;
        protected List<EnchantmentGroup> enchantments;

        protected EnchantmentGroupBuilder() {
            increment = 1;
            this.enchantments = List.of();
        }

        public B minFishLevel(int minFishLevel) {
            if (minFishLevel < 1) throw new IllegalArgumentException("Minimum fish level must be at least 1.");
            this.minFishLevel = minFishLevel;
            return self();
        }

        public B maxEnchantLevel(int maxEnchantLevel) {
            if (maxEnchantLevel < 1)
                throw new IllegalArgumentException("Maximum enchantment level must be at least 1.");
            this.maxEnchantLevel = maxEnchantLevel;
            return self();
        }

        public B increment(int increment) {
            if (increment < 1) throw new IllegalArgumentException("Increment must be at least 1.");
            this.increment = increment;
            return self();
        }

        public B chance(float chance) {
            if (chance < 0 || 1 < chance) throw new IllegalArgumentException("Chance must be between 0 and 1.");
            this.chance = chance;
            return self();
        }
    }
}
