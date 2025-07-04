package org.hark7.fishingPlugin.type;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemFish implements MaterialFish {
    private final Component name;
    private final Rarity rarity;
    public final Material material;
    private final int damage;
    private final EnchantmentLevelPair[] enchantments;

    /**
     * 魚のコンストラクタ（名前と素材のみ）
     *
     * @param material     アイテム
     * @param rarity       レアリティ
     * @param enchantments 付与されるエンチャント
     */
    public ItemFish(Material material, Rarity rarity, EnchantmentLevelPair... enchantments) {
        this.name = Optional.ofNullable(material.getItemTranslationKey())
                .map(Component::translatable)
                .map(c -> (Component) c)
                .orElse(Component.text(""));
        this.material = material;
        this.rarity = rarity;
        this.enchantments = enchantments;
        this.damage = 0; // デフォルトのダメージ値
    }

    /**
     * 魚のコンストラクタ（名前、素材、ダメージ値）
     *
     * @param material     アイテム
     * @param rarity       レアリティ
     * @param damage       ダメージ値
     * @param enchantments 付与されるエンチャント
     */
    public ItemFish(Material material, Rarity rarity, int damage, EnchantmentLevelPair... enchantments) {
        this.name = Component.translatable(material.translationKey());
        this.material = material;
        this.rarity = rarity;
        this.damage = damage;
        this.enchantments = enchantments;
    }

    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        var lore = Optional.ofNullable(meta.lore()).orElse(new ArrayList<>());
        lore.add(CustomLang.ofComponent("Items.Rarity").replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{rarity}")
                        .replacement(Component.text(rarity.name(), rarity.textColor()))
                        .build())
                .color(NamedTextColor.WHITE));
        meta.lore(lore);
        for (EnchantmentLevelPair enchantment : enchantments) {
            meta.addEnchant(enchantment.enchantment(), enchantment.level(), true);
        }
        //meta.setCustomModelData(1001);
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(damage); // 初期ダメージを0に設定
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public FishType fishType() {
        return FishType.Item;
    }

    @Override
    public Rarity rarity() {
        return rarity;
    }

    @Override
    public Material material() {
        return material;
    }

    @Override
    public Component name() {
        return name;
    }
}
