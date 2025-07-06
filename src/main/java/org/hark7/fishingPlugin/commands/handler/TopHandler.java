package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.commands.handler.top.*;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.*;

/**
 * コマンドハンドラー: top
 * <p>
 * プレイヤーのトップランキングを表示するコマンドを処理します。
 */
public class TopHandler implements ICommandHandler {
    private final FishingPlugin plugin;
    private final Map<String, ICommandHandler> handlers;

    /**
     * コンストラクタ
     *
     * @param manager PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public TopHandler(FishingPlugin plugin, PlayerDataController manager) {
        if (plugin == null) throw new IllegalArgumentException("FishingPlugin cannot be null");
        if (manager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        this.handlers = createHandlers(plugin, manager);
        this.plugin = plugin;
    }

    /**
     * コマンドハンドラーを作成します。
     *
     * @param manager PlayerDataManagerのインスタンス
     * @return ハンドラーの不変マップ
     */
    private Map<String, ICommandHandler> createHandlers(FishingPlugin plugin, PlayerDataController manager) {
        return Map.of(
                "level", new TopLevelCommand(manager),
                "total", new TopTotalCommand(manager),
                "scrap", new TopScrapCommand(manager),
                "common", new TopCommonCommand(manager),
                "uncommon", new TopUncommonCommand(manager),
                "rare", new TopRareCommand(manager),
                "epic", new TopEpicCommand(manager),
                "legendary", new TopLegendaryCommand(manager)
        );
    }

    @Override
    public boolean useOnlyOp() {
        return false;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            CustomLang.ofSimpleComponent("Commands.top.Usage", player.locale().toLanguageTag()).send(player);
            return;
        }
        var handler = handlers.get(args[1]);
        if (handler == null) {
            CustomLang.ofSimpleComponent("Commands.top.Usage", player.locale().toLanguageTag()).send(player);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> handler.execute(player, args));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(handlers.keySet());
        } else if (args.length == 3) {
            var handler = handlers.get(args[1]);
            if (handler != null) {
                return handler.tabComplete(args);
            }
        }
        return List.of();
    }
}