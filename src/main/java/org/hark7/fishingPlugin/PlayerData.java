package org.hark7.fishingPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hark7.fishingPlugin.CustomFish.*;

public class PlayerData {
    private String playerName; // プレイヤー名
    private int fishingLevel;  // 釣りレベル
    private int fishingExp;    // 釣り経験値
    private final HashMap<Rarity, Integer> caughtFishCount = new HashMap<>();  // 釣った魚の数

    public PlayerData(String playerName, int fishingLevel, int fishingExp, Map<Rarity, Integer> caughtFishCount) {
        this.playerName = playerName;
        this.fishingLevel = fishingLevel;
        this.fishingExp = fishingExp;
        Optional.ofNullable(caughtFishCount)
                .ifPresentOrElse(this.caughtFishCount::putAll, this::getInitialCount);
    }

    public PlayerData(String playerName, int fishingLevel, int fishingExp) {
        this.playerName = playerName;
        this.fishingLevel = fishingLevel;
        this.fishingExp = fishingExp;
        getInitialCount();
    }

    public int getLevel() {
        return fishingLevel;
    }

    public void setLevel(int fishingLevel) {
        this.fishingLevel = fishingLevel;
    }

    public int getExp() {
        return fishingExp;
    }

    public void setExp(int fishingExp) {
        this.fishingExp = fishingExp;
    }
    public Map<Rarity, Integer> getCounts() {
        return caughtFishCount;
    }

    public int getCount(Rarity rarity) {
        return caughtFishCount.getOrDefault(rarity, 0);
    }

    public int getCountAll() {
        return caughtFishCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addCount(Rarity rarity) {
        caughtFishCount.putIfAbsent(rarity, 0);
    }
    public String getPlayerName() {
        return playerName;
    }

    private void getInitialCount() {
        for (Rarity rarity : Rarity.values()) {
            caughtFishCount.putIfAbsent(rarity, 0);
        }
    }
}