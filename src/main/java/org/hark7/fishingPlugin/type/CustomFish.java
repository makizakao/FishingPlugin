package org.hark7.fishingPlugin.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * カスタム魚クラス
 * 各魚は名前、素材、説明、レアリティを持ちます。
 */
public class CustomFish implements MaterialFish {
    public final Component name;
    private final Rarity rarity;
    private final Material material;
    private final Component description;

    /**
     * 魚のコンストラクタ
     *
     * @param name        魚の名前
     * @param material    魚の素材
     * @param description 魚の説明
     * @param rarity      魚のレアリティ
     */
    public CustomFish(String name, Material material, Component description, Rarity rarity) {
        if (name == null) throw new NullPointerException();      // 名前がnullの場合は例外を投げる
        if (material == null) throw new NullPointerException();  // アイテムがnullの場合は例外を投げる

        if (description == null) description = Component.empty(); // 説明がnullの場合は空のコンポーネントを使用
        this.name = Component.text(name);
        this.material = material;
        this.description = description;
        this.rarity = rarity;
    }

    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(name);
        var lore = Optional.ofNullable(meta.lore()).orElse(new ArrayList<>());
        lore.add(description);
        lore.add(CustomLang.ofComponent("Items.Rarity").replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{rarity}")
                        .replacement(Component.text(rarity.name(), rarity.textColor()))
                        .build())
                .color(NamedTextColor.WHITE));
        meta.lore(lore);
        //meta.setCustomModelData(1001);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public Component name() {
        return name;
    }

    @Override
    public FishType fishType() {
        return FishType.Fish;
    }

    @Override
    public Rarity rarity() {
        return rarity;
    }

    @Override
    public Material material() {
        return material;
    }
}
