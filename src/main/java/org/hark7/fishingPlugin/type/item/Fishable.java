package org.hark7.fishingPlugin.type.item;

import com.google.common.base.CaseFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerFishEvent;

public interface Fishable {
    Component name();
    FishType fishType();
    Rarity rarity();
    void onFish(PlayerFishEvent event);

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
        private final TextColor textColor;

        Rarity(int expValue, int pExpValue, TextColor textColor) {
            this.expValue = expValue;
            this.pExpValue = pExpValue;
            this.textColor = textColor;
        }

        public int playerExp() {
            return pExpValue;
        }

        public int exp() {
            return expValue;
        }

        public TextColor textColor() {
            return textColor;
        }

        public String key() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
        }
    }

    record EnchantmentLevelPair(Enchantment enchantment, int level) {
    }
}
