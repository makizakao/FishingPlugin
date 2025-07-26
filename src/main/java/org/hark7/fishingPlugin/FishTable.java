package org.hark7.fishingPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.hark7.fishingPlugin.type.group.DurabilityGroup;
import org.hark7.fishingPlugin.type.group.EnchantmentGroup;
import org.hark7.fishingPlugin.type.item.*;
import org.hark7.fishingPlugin.type.item.Fishable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FishTable {
    private final List<Fishable> fishList = new ArrayList<>();       // 釣り可能な魚のリスト

    public void initializeFishList() {
        // Scrap fish
        addItem(Material.LILY_PAD, Rarity.SCRAP);
        addItem(Material.BOWL, Rarity.SCRAP);
        addItem(Material.STICK, Rarity.SCRAP);
        addItem(Material.STRING, Rarity.SCRAP);
        addItem(Material.ROTTEN_FLESH, Rarity.SCRAP);
        addItem(Material.BONE, Rarity.SCRAP);
        addItem(Material.LEATHER, Rarity.SCRAP);
        addItem(Material.INK_SAC, Rarity.SCRAP);
        addEntity(EntityType.ZOMBIE, Rarity.SCRAP);
        addEntity(EntityType.SKELETON, Rarity.SCRAP);
        addEntity(EntityType.DROWNED, Rarity.SCRAP);
        addEntity(EntityType.TNT, Rarity.UNCOMMON);

        // Common fish (40 types)
        addFish("コイ", "淡水の定番魚", Material.COD, Rarity.COMMON);
        addFish("フナ", "日本の川でよく見られる魚", Material.COD, Rarity.COMMON);
        addFish("ワカサギ", "小さくて美味しい魚", Material.COD, Rarity.COMMON);
        addFish("アユ", "香り豊かな清流の女王", Material.SALMON, Rarity.COMMON);
        addFish("ヤマメ", "渓流釣りの人気者", Material.SALMON, Rarity.COMMON);
        addFish("ニジマス", "虹色に輝く美しい魚", Material.SALMON, Rarity.COMMON);
        addFish("イワナ", "渓流の王者", Material.SALMON, Rarity.COMMON);
        addFish("ウグイ", "清流に住む魚", Material.COD, Rarity.COMMON);
        addFish("オイカワ", "小川によく見られる魚", Material.COD, Rarity.COMMON);
        addFish("カワムツ", "川の中流によく生息する魚", Material.COD, Rarity.COMMON);
        addFish("タナゴ", "二枚貝に卵を産む珍しい魚", Material.COD, Rarity.COMMON);
        addFish("ドジョウ", "田んぼにも生息する魚", Material.COD, Rarity.COMMON);
        addFish("ナマズ", "ひげが特徴的な大型淡水魚", Material.COD, Rarity.COMMON);
        addFish("ハヤ", "清流を好む小型の魚", Material.COD, Rarity.COMMON);
        addFish("フナ", "コイの仲間で pond fish とも呼ばれる", Material.COD, Rarity.COMMON);
        addFish("メダカ", "日本の原風景を思わせる小魚", Material.COD, Rarity.COMMON);
        addFish("モツゴ", "小川や池に生息する小魚", Material.COD, Rarity.COMMON);
        addFish("ライギョ", "外来種で強い生命力を持つ魚", Material.COD, Rarity.COMMON);
        addFish("ワカサギ", "湖に生息する小魚で佃煮にも", Material.COD, Rarity.COMMON);
        addFish("アジ", "釣り人に人気の海水魚", Material.COD, Rarity.COMMON);
        addEntity(EntityType.SQUID, Rarity.COMMON);
        addEntity(EntityType.COD, Rarity.COMMON);
        addEntity(EntityType.SALMON, Rarity.COMMON);

        // Uncommon fish (30 types)
        addFish("サケ", "遡上する魚の代表", Material.SALMON, Rarity.UNCOMMON);
        addFish("マス", "冷たい川に住む魚", Material.SALMON, Rarity.UNCOMMON);
        addFish("ブラックバス", "外来種だが人気の高い魚", Material.COD, Rarity.UNCOMMON);
        addFish("ナマズ", "大きくなる底生魚", Material.COD, Rarity.UNCOMMON);
        addFish("ウナギ", "夏の風物詩", Material.COD, Rarity.UNCOMMON);
        addFish("ハゼ", "河口域でよく見られる", Material.COD, Rarity.UNCOMMON);
        addFish("キンギョ", "観賞用として人気の魚", Material.COD, Rarity.UNCOMMON);
        addFish("グッピー", "カラフルな熱帯魚", Material.TROPICAL_FISH, Rarity.UNCOMMON);
        addFish("ティラピア", "養殖が盛んな食用魚", Material.COD, Rarity.UNCOMMON);
        addFish("コチ", "毒のある棘を持つ魚", Material.COD, Rarity.UNCOMMON);
        addFish("カレイ", "片側に目がある平たい魚", Material.COD, Rarity.UNCOMMON);
        addFish("ヒラメ", "高級食材として知られる魚", Material.COD, Rarity.UNCOMMON);
        addFish("スズキ", "沿岸でよく釣れる魚", Material.COD, Rarity.UNCOMMON);
        addFish("タイ", "祝い事に使われる縁起の良い魚", Material.COD, Rarity.UNCOMMON);
        addFish("ブリ", "出世魚として知られる", Material.COD, Rarity.UNCOMMON);
        addEntity(EntityType.TROPICAL_FISH, Rarity.UNCOMMON);


        // Rare fish (20 types)
        addFish("マグロ", "寿司の王様", Material.SALMON, Rarity.RARE);
        addFish("カツオ", "初がつおが有名", Material.SALMON, Rarity.RARE);
        addFish("サンマ", "秋の味覚", Material.COD, Rarity.RARE);
        addFish("イカ", "触手を持つ魚ではない生き物", Material.COD, Rarity.RARE);
        addFish("タコ", "知能の高い八本足", Material.COD, Rarity.RARE);
        addFish("エビ", "様々な料理に使われる", Material.COD, Rarity.RARE);
        addFish("カニ", "甲殻類の代表", Material.COD, Rarity.RARE);
        addFish("ホタテ", "貝類の中でも人気", Material.COD, Rarity.RARE);
        addFish("アワビ", "高級食材として知られる", Material.COD, Rarity.RARE);
        addFish("ウニ", "海のフォアグラと呼ばれる", Material.COD, Rarity.RARE);
        addFish("フグ", "毒を持つ高級魚", Material.PUFFERFISH, Rarity.RARE);
        addFish("アンコウ", "深海に住む奇妙な魚", Material.COD, Rarity.RARE);
        addFish("ノコギリザメ", "特徴的な吻を持つサメ", Material.COD, Rarity.RARE);
        addFish("イセエビ", "高級食材として知られる甲殻類", Material.COD, Rarity.RARE);
        addFish("タツノオトシゴ", "ユニークな形状の魚", Material.TROPICAL_FISH, Rarity.RARE);
        addEntity(EntityType.PUFFERFISH, Rarity.RARE);

        // Epic fish (8 types)
        addFish("マンボウ", "巨大な体の奇妙な魚", Material.COD, Rarity.EPIC);
        addFish("チョウザメ", "キャビアの源となる魚", Material.COD, Rarity.EPIC);
        addFish("ガー", "古代から姿を変えない魚", Material.COD, Rarity.EPIC);
        addFish("ピラルク", "アマゾンの巨大魚", Material.COD, Rarity.EPIC);
        addFish("オオメジロザメ", "大型の猛烈な捕食者", Material.COD, Rarity.EPIC);
        addFish("シーラカンス", "生きた化石", Material.COD, Rarity.EPIC);
        addFish("リュウグウノツカイ", "深海の不思議な魚", Material.COD, Rarity.EPIC);
        addFish("ラブカ", "原始的な姿の深海ザメ", Material.COD, Rarity.EPIC);

        // Legendary fish (8 types)
        addFish("幻の金色コイ", "伝説の魚", Material.COD, Rarity.LEGENDARY);
        addFish("深海の巨大イカ", "伝説の海獣", Material.COD, Rarity.LEGENDARY);
        addItem(Material.NAUTILUS_SHELL, Rarity.LEGENDARY);
        addItem(Material.SADDLE, Rarity.LEGENDARY);
        addItem(Material.NAME_TAG, Rarity.LEGENDARY);
        addTool(Material.FISHING_ROD, Rarity.LEGENDARY, 60,
                List.of(
                        EnchantmentGroup.builder().enchantment(Enchantment.UNBREAKING)
                                .minFishLevel(3).maxEnchantLevel(3).increment(3).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.LURE)
                                .minFishLevel(7).maxEnchantLevel(3).increment(4).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.LUCK_OF_THE_SEA)
                                .minFishLevel(10).maxEnchantLevel(3).increment(3).chance(0.25f).build()
                ), List.of(
                        DurabilityGroup.builder().minLevel(3).minRepair(1).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(5).minRepair(2).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(7).minRepair(2).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(10).minRepair(3).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(12).minRepair(3).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(15).minRepair(1).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(18).minRepair(1).maxRepair(10).build(),
                        DurabilityGroup.builder().minLevel(20).minRepair(1).maxRepair(10).build()
                ));
        addTool(Material.BOW, Rarity.LEGENDARY, 350,
                List.of(
                        EnchantmentGroup.builder().enchantment(Enchantment.UNBREAKING)
                                .minFishLevel(3).maxEnchantLevel(3).increment(3).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.PUNCH)
                                .minFishLevel(5).maxEnchantLevel(2).increment(5).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.POWER)
                                .minFishLevel(5).maxEnchantLevel(5).increment(3).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.FLAME)
                                .minFishLevel(15).maxEnchantLevel(1).chance(0.25f).build(),
                        EnchantmentGroup.builder().enchantment(Enchantment.INFINITY)
                                .minFishLevel(20).maxEnchantLevel(1).chance(0.25f).build()
                ), List.of(
                        DurabilityGroup.builder().minLevel(3).minRepair(5).maxRepair(30).build(),
                        DurabilityGroup.builder().minLevel(5).minRepair(5).maxRepair(30).build(),
                        DurabilityGroup.builder().minLevel(7).minRepair(5).maxRepair(30).build(),
                        DurabilityGroup.builder().minLevel(10).minRepair(10).maxRepair(30).build(),
                        DurabilityGroup.builder().minLevel(12).minRepair(10).maxRepair(30).build(),
                        DurabilityGroup.builder().minLevel(15).minRepair(5).maxRepair(40).build(),
                        DurabilityGroup.builder().minLevel(18).minRepair(5).maxRepair(40).build(),
                        DurabilityGroup.builder().minLevel(20).minRepair(5).maxRepair(50).build()
                ));
        List<EnchantmentGroup> enchantments = Stream.of(
                Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE, Enchantment.FROST_WALKER, Enchantment.MENDING,
                Enchantment.SOUL_SPEED, Enchantment.SWIFT_SNEAK, Enchantment.WIND_BURST, Enchantment.AQUA_AFFINITY,
                Enchantment.BANE_OF_ARTHROPODS, Enchantment.BLAST_PROTECTION, Enchantment.BREACH,
                Enchantment.CHANNELING, Enchantment.DEPTH_STRIDER, Enchantment.DENSITY, Enchantment.EFFICIENCY,
                Enchantment.FEATHER_FALLING, Enchantment.FIRE_ASPECT, Enchantment.FIRE_PROTECTION, Enchantment.FLAME,
                Enchantment.FORTUNE, Enchantment.IMPALING, Enchantment.INFINITY, Enchantment.KNOCKBACK,
                Enchantment.LOOTING, Enchantment.LOYALTY, Enchantment.LUCK_OF_THE_SEA, Enchantment.LURE,
                Enchantment.MULTISHOT, Enchantment.PIERCING, Enchantment.POWER, Enchantment.PROJECTILE_PROTECTION,
                Enchantment.PROTECTION, Enchantment.PUNCH, Enchantment.QUICK_CHARGE, Enchantment.RESPIRATION,
                Enchantment.RIPTIDE, Enchantment.SHARPNESS, Enchantment.SILK_TOUCH, Enchantment.SMITE,
                Enchantment.SWEEPING_EDGE, Enchantment.THORNS, Enchantment.UNBREAKING
        ).map(e -> {
            var maxLevel = e.getMaxLevel();
            var increment = 2 < maxLevel ? 10 : 5;
            return EnchantmentGroup.builder().enchantment(e)
                    .minFishLevel(1).maxEnchantLevel(maxLevel).increment(increment).chance(0.5f).build();
        }).collect(Collectors.toUnmodifiableList());
        addItem(Material.ENCHANTED_BOOK, Rarity.LEGENDARY, enchantments);
    }

    public List<Fishable> fishList() {
        return List.copyOf(fishList);
    }

    private void addFish(String name, String description, Material material, Rarity rarity) {
        fishList.add(ItemFish.builder()
                .name(Component.text(name))
                .material(material)
                .description(Component.text(description).color(NamedTextColor.GRAY))
                .fishType(FishType.Fish)
                .rarity(rarity).build());
    }

    private void addItem(Material material, Rarity rarity) {
        fishList.add(ItemFish.builder()
                .material(material)
                .nameFromMaterial()
                .fishType(FishType.Item)
                .rarity(rarity).build());
    }

    private void addItem(Material material, Rarity rarity, List<EnchantmentGroup> enchantments) {
        fishList.add(ItemFish.builder()
                .material(material)
                .nameFromMaterial()
                .rarity(rarity)
                .enchantments(enchantments)
                .fishType(FishType.Item)
                .build());
    }

    private void addTool(Material material, Rarity rarity, int damage,
                         List<EnchantmentGroup> enchantments, List<DurabilityGroup> durabilityGroups) {
        fishList.add(ToolFish.builder()
                .material(material)
                .nameFromMaterial()
                .rarity(rarity)
                .damage(damage)
                .enchantments(enchantments)
                .durabilityGroups(durabilityGroups)
                .fishType(FishType.Item)
                .build());
    }

    private void addEntity(EntityType entity, Rarity rarity) {
        fishList.add(EntityFish.builder()
                .entity(entity)
                .nameFromEntity()
                .rarity(rarity).build());
    }
}
