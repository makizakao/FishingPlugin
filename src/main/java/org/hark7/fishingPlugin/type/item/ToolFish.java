package org.hark7.fishingPlugin.type.item;

import lombok.experimental.SuperBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.type.group.DurabilityGroup;

import java.util.List;

@SuperBuilder
public class ToolFish extends ItemFish {
    private final int damage;
    private final List<DurabilityGroup> durabilityGroups;

    @Override
    public ItemStack createItemStack(int playerLevel) {
        var itemStack = super.createItemStack(playerLevel);
        if (itemStack.getItemMeta() instanceof Damageable meta) {
            meta.setDamage(getResultDamage(damage, playerLevel));
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    private int getResultDamage(final int damage, int level) {
        var result = damage;
        for (DurabilityGroup group : durabilityGroups) {
            var minRepair = group.getMinRepair();
            var maxRepair = group.getMaxRepair();
            if (group.getMinLevel() <= level) {
                result -= rand.nextInt(minRepair + maxRepair + 1);
            }
        }
        return Math.max(result, 0);
    }

    @Override
    protected void setEnchant(ItemMeta meta, int playerLevel) {
        enchantments.stream().filter(e -> e.getMinFishLevel() <= playerLevel)
                .filter(e -> rand.nextFloat() < e.getChance())
                .forEach(e -> {
                    var enchantment = e.getEnchantment();
                    var minLevel = e.getMinFishLevel();
                    var maxLevel = e.getMaxEnchantLevel();
                    var increment = e.getIncrement();
                    var level = 1 + rand.nextInt(Math.min(maxLevel, playerLevel / increment + minLevel) + 1);
                    meta.addEnchant(enchantment, level, true);
                });
    }

    public abstract static class ToolFishBuilder<C extends ToolFish,
            B extends ToolFishBuilder<C, B>> extends ItemFishBuilder<C, B> {
        protected List<DurabilityGroup> durabilityGroups;

        protected ToolFishBuilder() {
            super();
            durabilityGroups = List.of();
        }

        public B damage(int damage) {
            this.damage = damage;
            return self();
        }

        public B durabilityGroups(List<DurabilityGroup> durabilityGroups) {
            if (durabilityGroups == null || durabilityGroups.isEmpty()) {
                throw new IllegalArgumentException("Damage groups cannot be null or empty");
            }

            this.durabilityGroups = durabilityGroups;
            return self();
        }
    }
}