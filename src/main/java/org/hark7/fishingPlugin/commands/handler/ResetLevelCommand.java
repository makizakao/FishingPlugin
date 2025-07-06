package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;

/**
 * コマンドハンドラー: resetlevel
 * <p>
 * プレイヤーのレベルをリセットするコマンドを処理します。使用権限はオペレーターのみです。
 */
public class ResetLevelCommand implements ICommandHandler {
    private final FishingPlugin plugin;
    private final PlayerDataController manager;
    private static final String ALL_PLAYERS_ARG = "@a";
    private static final String ALL_PLAYERS_ARG_ALT = "all";

    /**
     * コンストラクタ
     *
     * @param plugin  FishingPluginのインスタンス
     * @param manager PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public ResetLevelCommand(FishingPlugin plugin, PlayerDataController manager) {
        if (plugin == null) throw new IllegalArgumentException("FishingPlugin cannot be null");
        if (manager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public boolean useOnlyOp() {
        return true;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp()) {
            CustomLang.ofSimpleComponent("Commands.Invalid.PermissionMessage").send(player);
            return;
        }

        if (args.length != 2) {
            CustomLang.ofSimpleComponent("Commands.resetlevel.Usage").send(player);
            return;
        }
        // 全員のレベルをリセットする場合の処理
        if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("all")) {
            Bukkit.getOnlinePlayers().forEach(this::resetPlayerLevel);
            CustomLang.ofSimpleComponent("Commands.resetlevel.All").send(player);
            return;
        }

        // 特定のプレイヤーのレベルをリセットする場合の処理
        var targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            CustomLang.ofSimpleComponent("Commands.Invalid.PlayerNotFound").send(player);
            return;
        }
        resetPlayerLevel(targetPlayer);
        CustomLang.ofSimpleComponent("Commands.resetlevel.Player")
                .replace("{player}", player.getName()).send(player);
    }

    /**
     * 指定されたプレイヤーのレベルをリセットします。
     *
     * @param target リセット対象のプレイヤー
     */
    private void resetPlayerLevel(Player target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            var uuid = target.getUniqueId();
            var data = new PlayerData(target.getName(), 1, 0);
            manager.savePlayerData(uuid, data);
        });
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            var list = new ArrayList<String>();
            list.add(ALL_PLAYERS_ARG);
            list.add(ALL_PLAYERS_ARG_ALT);
            Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
            return list;
        }
        return List.of();
    }
}
