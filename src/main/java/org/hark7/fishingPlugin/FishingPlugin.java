package org.hark7.fishingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class FishingPlugin extends JavaPlugin implements Listener {

    private Map<UUID, Integer> fishingLevels = new HashMap<>();
    private Map<UUID, Integer> fishingExp = new HashMap<>();
    private List<CustomFish> fishList = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        initializeFishList();
        getCommand("fishstats").setExecutor(new FishStatsCommand(this));
        getCommand("fishtop").setExecutor(new FishTopCommand(this));
        getCommand("upgradepole").setExecutor(new UpgradePoleCommand(this));
        getCommand("addexp").setExecutor(new AddExpCommand(this));
        getLogger().info("FishingPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        saveCustomConfig();
        getLogger().info("FishingPlugin has been disabled!");
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        if (config.contains("fishing_levels")) {
            for (String uuidString : config.getConfigurationSection("fishing_levels").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                fishingLevels.put(uuid, config.getInt("fishing_levels." + uuidString));
                fishingExp.put(uuid, config.getInt("fishing_exp." + uuidString));
            }
        }
    }

    private void saveCustomConfig() {
        FileConfiguration config = getConfig();
        for (Map.Entry<UUID, Integer> entry : fishingLevels.entrySet()) {
            config.set("fishing_levels." + entry.getKey().toString(), entry.getValue());
            config.set("fishing_exp." + entry.getKey().toString(), fishingExp.get(entry.getKey()));
        }
        saveConfig();
    }

    private void initializeFishList() {
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
        addFish("ウニ", "海のフォアグラと呼ばれる", Material.SEA_PICKLE, Rarity.RARE);
        addFish("フグ", "毒を持つ高級魚", Material.PUFFERFISH, Rarity.RARE);
        addFish("アンコウ", "深海に住む奇妙な魚", Material.COD, Rarity.RARE);
        addFish("ノコギリザメ", "特徴的な吻を持つサメ", Material.COD, Rarity.RARE);
        addFish("イセエビ", "高級食材として知られる甲殻類", Material.COD, Rarity.RARE);
        addFish("タツノオトシゴ", "ユニークな形状の魚", Material.TROPICAL_FISH, Rarity.RARE);

        // Epic fish (8 types)
        addFish("マンボウ", "巨大な体の奇妙な魚", Material.COD, Rarity.EPIC);
        addFish("チョウザメ", "キャビアの源となる魚", Material.COD, Rarity.EPIC);
        addFish("ガー", "古代から姿を変えない魚", Material.COD, Rarity.EPIC);
        addFish("ピラルク", "アマゾンの巨大魚", Material.COD, Rarity.EPIC);
        addFish("オオメジロザメ", "大型の猛烈な捕食者", Material.COD, Rarity.EPIC);
        addFish("シーラカンス", "生きた化石", Material.COD, Rarity.EPIC);
        addFish("リュウグウノツカイ", "深海の不思議な魚", Material.COD, Rarity.EPIC);
        addFish("ラブカ", "原始的な姿の深海ザメ", Material.COD, Rarity.EPIC);

        // Legendary fish (2 types)
        addFish("幻の金色コイ", "伝説の魚", Material.GOLDEN_APPLE, Rarity.LEGENDARY);
        addFish("深海の巨大イカ", "伝説の海獣", Material.DRAGON_EGG, Rarity.LEGENDARY);
    }


    private void addFish(String name, String description, Material material, Rarity rarity) {
        fishList.add(new CustomFish(name, material, description, rarity));
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            UUID playerUUID = player.getUniqueId();
            CustomFish caughtFish = getRandomFish(playerUUID);

            if (event.getCaught() instanceof Item) {
                Item caughtItem = (Item) event.getCaught();
                caughtItem.setItemStack(caughtFish.createItemStack());
            }

            player.sendMessage(caughtFish.getRarity().getColor() + "あなたは " + caughtFish.getName() + " を釣り上げました！");

            int baseExp = caughtFish.getRarity().getExpValue();
            int bonusExp = calculateBonusExp(player.getInventory().getItemInMainHand());
            int totalExp = baseExp + bonusExp;

            addExperience(playerUUID, totalExp);
            player.sendMessage(ChatColor.YELLOW + "獲得経験値: " + totalExp + " (ボーナス: " + bonusExp + ")");
        }
    }

    private int calculateBonusExp(ItemStack fishingRod) {
        if (fishingRod.getType() != Material.FISHING_ROD) return 0;

        int luckOfTheSeaLevel = fishingRod.getEnchantmentLevel(Enchantment.LUCK);
        int lureLevel = fishingRod.getEnchantmentLevel(Enchantment.LURE);

        return (luckOfTheSeaLevel * 5) + (lureLevel * 3);
    }

    private CustomFish getRandomFish(UUID playerUUID) {
        int playerLevel = fishingLevels.getOrDefault(playerUUID, 1);
        double rarityBonus = playerLevel * 0.001; // 0.1% increase per level

        double random = Math.random();
        Rarity selectedRarity;

        if (random < (0.001 + rarityBonus)) selectedRarity = Rarity.LEGENDARY;
        else if (random < (0.01 + rarityBonus)) selectedRarity = Rarity.EPIC;
        else if (random < (0.05 + rarityBonus)) selectedRarity = Rarity.RARE;
        else if (random < (0.2 + rarityBonus)) selectedRarity = Rarity.UNCOMMON;
        else selectedRarity = Rarity.COMMON;

        List<CustomFish> fishOfSelectedRarity = fishList.stream()
                .filter(fish -> fish.getRarity() == selectedRarity)
                .collect(Collectors.toList());

        return fishOfSelectedRarity.get(new Random().nextInt(fishOfSelectedRarity.size()));
    }

    public void addExperience(UUID playerUUID, int exp) {
        int currentExp = fishingExp.getOrDefault(playerUUID, 0) + exp;
        int currentLevel = fishingLevels.getOrDefault(playerUUID, 1);

        while (currentExp >= getRequiredExp(currentLevel)) {
            currentExp -= getRequiredExp(currentLevel);
            currentLevel++;
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.GOLD + "釣りレベルが上がりました！ 現在のレベル: " + currentLevel);
            }
        }

        fishingExp.put(playerUUID, currentExp);
        fishingLevels.put(playerUUID, currentLevel);
    }

    public int getRequiredExp(int level) {
        return 100 * level * level;
    }

    public Map<UUID, Integer> getFishingLevels() {
        return fishingLevels;
    }

    public Map<UUID, Integer> getFishingExp() {
        return fishingExp;
    }

    private enum Rarity {
        COMMON(10, ChatColor.WHITE),
        UNCOMMON(20, ChatColor.GREEN),
        RARE(40, ChatColor.BLUE),
        EPIC(100, ChatColor.DARK_PURPLE),
        LEGENDARY(250, ChatColor.GOLD);

        private final int expValue;
        private final ChatColor color;

        Rarity(int expValue, ChatColor color) {
            this.expValue = expValue;
            this.color = color;
        }

        public int getExpValue() {
            return expValue;
        }

        public ChatColor getColor() {
            return color;
        }
    }

    private static class CustomFish {
        private final String name;
        private final Material material;
        private final String description;
        private final Rarity rarity;

        public CustomFish(String name, Material material, String description, Rarity rarity) {
            this.name = name;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
        }

        public ItemStack createItemStack() {
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(rarity.getColor() + name);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + description);
                lore.add(rarity.getColor() + "レア度: " + rarity.name());
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            }
            return itemStack;
        }

        public String getName() {
            return name;
        }

        public Rarity getRarity() {
            return rarity;
        }
    }
}