package org.hark7.fishingPlugin.type.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.type.group.EnchantmentGroup;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemFish implements Fishable {
    protected final Random rand = new Random();
    protected final Component name;
    protected final Component description;
    protected final Rarity rarity;
    protected final Material material;
    protected final List<EnchantmentGroup> enchantments;
    protected final FishType fishType;

    @Override
    public void onFish(PlayerFishEvent event, FishLevelManager manager, FishingPlugin plugin) {
        if (event.getCaught() instanceof Item caughtItem) {
            var level = manager.getPlayerLevel(event.getPlayer());
            caughtItem.setItemStack(createItemStack(level));
        }
    }

    public ItemStack createItemStack(int playerLevel) {
        var itemStack = new ItemStack(material);
        var meta = itemStack.getItemMeta();
        var lore = Optional.ofNullable(meta.lore()).orElse(new ArrayList<>());
        meta.displayName(name.color(rarity.color()));
        Optional.ofNullable(description).filter(d -> d != Component.empty()).ifPresent(lore::add);
        lore.add(CustomLang.ofComponent("Items.Rarity").replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{rarity}")
                        .replacement(Component.text(rarity.name(), rarity.color()))
                        .build())
                .color(NamedTextColor.WHITE));
        meta.lore(lore);
        setEnchant(meta, playerLevel);
        meta.displayName(name.color(rarity.color()));
        //meta.setCustomModelData(1001);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    protected void setEnchant(ItemMeta meta, int playerLevel) {
        var availableEnchantments = enchantments.stream()
                .filter(e -> e.getMinFishLevel() <= playerLevel)
                .filter(e -> rand.nextFloat() < e.getChance())
                .toList();
        if (availableEnchantments.isEmpty()) return;
        EnchantmentGroup enchantment = availableEnchantments.get(rand.nextInt(availableEnchantments.size()));
        int maxLevel = enchantment.getEnchantment().getMaxLevel();
        int minLevel = enchantment.getMinFishLevel();
        int increment = enchantment.getIncrement();
        var level = 1 + rand.nextInt(Math.min(maxLevel, playerLevel / increment + minLevel) + 1);
        meta.addEnchant(enchantment.getEnchantment(), level, true);
    }

    public abstract static class ItemFishBuilder<C extends ItemFish, B extends ItemFishBuilder<C, B>> {
        protected Component description;
        protected FishType fishType;
        protected List<EnchantmentGroup> enchantments;

        protected ItemFishBuilder() {
            this.description = Component.empty();
            this.fishType = FishType.Fish;
            this.enchantments = List.of();
        }

        public B description(Component description) {
            if (description == null) throw new NullPointerException("Description cannot be null");
            this.description = description;
            return self();
        }

        public B enchantments(List<EnchantmentGroup> enchantments) {
            if (enchantments == null) throw new NullPointerException("Enchantments cannot be null");
            this.enchantments = enchantments;
            return self();
        }

        public B nameFromMaterial() {
            if (material == null) throw new NullPointerException("Material must be set before.");
            if (this.name != null)
                throw new IllegalStateException("'name' is already set. Cannot set it again from material.");

            this.name = Optional.ofNullable(material.getItemTranslationKey())
                    .map(Component::translatable)
                    .map(c -> (Component) c)
                    .orElse(Component.text(material.name()));
            return self();
        }
    }

    @Override
    public FishType fishType() {
        return fishType;
    }

    @Override
    public Rarity rarity() {
        return rarity;
    }

    @Override
    public Component name() {
        return name;
    }
}
