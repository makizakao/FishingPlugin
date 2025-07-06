package org.hark7.fishingPlugin.type.item;

import org.bukkit.Material;

import java.util.List;

public class ToolFish extends ItemFish {
    public ToolFish(Material material, Rarity rarity, List<EnchantmentLevelPair> enchantments) {
        super(material, rarity, enchantments);
    }
    public ToolFish(Material material, Rarity rarity, int damage, List<EnchantmentLevelPair> enchantments) {
        super(material, rarity, damage, enchantments);
    }
}
