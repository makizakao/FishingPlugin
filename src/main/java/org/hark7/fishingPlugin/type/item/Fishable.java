package org.hark7.fishingPlugin.type.item;

import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerFishEvent;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.manager.FishLevelManager;

import java.util.List;

public interface Fishable {
    Component name();

    FishType fishType();

    Rarity rarity();

    void onFish(PlayerFishEvent event, FishLevelManager manager, FishingPlugin plugin);

    enum FishType {
        Fish,
        Item,
        Entity
    }

    enum Rarity {
        SCRAP(1, 1, NamedTextColor.GRAY),
        COMMON(10, 2, NamedTextColor.WHITE),
        UNCOMMON(20, 4, NamedTextColor.GREEN),
        RARE(40, 6, NamedTextColor.BLUE),
        EPIC(100, 10, NamedTextColor.DARK_PURPLE),
        LEGENDARY(250, 15, NamedTextColor.GOLD);

        private final int expValue;
        private final int pExpValue;
        private final TextColor color;

        Rarity(int expValue, int pExpValue, TextColor color) {
            this.expValue = expValue;
            this.pExpValue = pExpValue;
            this.color = color;
        }

        public int playerExp() {
            return pExpValue;
        }

        public int exp() {
            return expValue;
        }

        public TextColor color() {
            return color;
        }

        public String key() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
        }
    }

    @SuperBuilder
    @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    class EnchantmentGroup {
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

    @SuperBuilder
    @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    class DurabilityGroup {
        @Getter
        private int minLevel;
        @Getter
        private int minRepair;
        @Getter
        private int maxRepair;

        public abstract static class DurabilityGroupBuilder<C extends DurabilityGroup, B extends DurabilityGroupBuilder<C, B>> {
            protected int minLevel;
            protected int minRepair;
            protected int maxRepair;

            public B minLevel(int minLevel) {
                if (minLevel < 1) throw new IllegalArgumentException("Minimum level must be at least 1.");
                this.minLevel = minLevel;
                return self();
            }

            public B minRepair(int minRepair) {
                if (minRepair < 0) throw new IllegalArgumentException("Minimum repair must be at least 0.");
                this.minRepair = minRepair;
                return self();
            }

            public B maxRepair(int maxRepair) {
                if (maxRepair < 0) throw new IllegalArgumentException("Maximum repair must be at least 0.");
                this.maxRepair = maxRepair;
                return self();
            }
        }
    }
}
