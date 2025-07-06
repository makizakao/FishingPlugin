package org.hark7.fishingPlugin;

import org.bukkit.Bukkit;
import org.hark7.fishingPlugin.commands.*;
import org.hark7.fishingPlugin.database.DatabaseVersionManager;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.listener.FishListener;
import org.hark7.fishingPlugin.listener.PlayerPreLoginListener;
import org.hark7.fishingPlugin.listener.VillagerAcquireTradeListener;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;
import org.mineacademy.fo.plugin.SimplePlugin;

public class FishingPlugin extends SimplePlugin {
    private final FishTable fishTable = new FishTable();
    private PlayerDataController saveManager;

    /**
     * プラグインの有効化時に呼び出されるメソッド
     * イベントリスナーの登録、設定ファイルの読み込み、レシピの登録を行います。
     */
    @Override
    public void onPluginStart() {
        saveManager = new PlayerDataController(this);
        var dbVersionManager = new DatabaseVersionManager(this, saveManager);
        var fishExpManager = new FishLevelManager(this, saveManager);
        //init
        dbVersionManager.checkAndMigrate();
        fishTable.initializeFishList();
        CustomLang.init(this, getFile());
        var recipeManager = VillagerRecipes.createRecipeManager(fishTable.fishList());
        Bukkit.getPluginManager().registerEvents(new FishListener(this, fishExpManager, saveManager), this);
        Bukkit.getPluginManager().registerEvents(new VillagerAcquireTradeListener(recipeManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPreLoginListener(this, saveManager), this);
        Recipes.register(this);
        // コマンドの追加
        registerCommand(new FishCommand(this, fishExpManager, saveManager));
        getLogger().info("FishingPlugin has been enabled!");
    }

    /**
     * プラグインの無効化時に呼び出されるメソッド
     * 設定ファイルの保存を行います。
     */
    @Override
    public void onPluginStop() {
        saveManager.close();
        getLogger().info("FishingPlugin has been disabled!");
    }
}