package org.hark7.fishingPlugin.commands;

import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.commands.handler.*;
import org.hark7.fishingPlugin.commands.handler.UpgradePoleCommand;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.util.CustomLang;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FishPluginのメインコマンドクラス
 * <p>
 * プレイヤーが使用できるコマンドを管理します。
 */
public final class FishCommand extends SimpleCommand {
    private final Map<String, ICommandHandler> commandHandlers;

    /**
     * コンストラクタ
     *
     * @param fishLevelManager FishExpManagerのインスタンス
     * @param saveManager      PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます
     */
    public FishCommand(FishingPlugin plugin, FishLevelManager fishLevelManager, PlayerDataController saveManager) {
        super("fish");
        if (fishLevelManager == null) throw new IllegalArgumentException("FishExpManager cannot be null");
        if (saveManager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        setDescription("FishPlugin Commands");
        setMinArguments(1);
        this.commandHandlers = createCommandHandlers(plugin, fishLevelManager, saveManager);
    }

    /**
     * コマンドハンドラーを作成します。
     * 各コマンドに対応するハンドラーをMapに格納します。
     *
     * @param fishLevelManager FishExpManagerのインスタンス
     * @param saveManager      PlayerDataManagerのインスタンス
     * @return コマンドハンドラーの不変Map
     */
    private Map<String, ICommandHandler> createCommandHandlers(
            FishingPlugin plugin, FishLevelManager fishLevelManager, PlayerDataController saveManager) {
        return Map.of(
                "addexp", new AddExpHandler(fishLevelManager),
                "stats", new StatsHandler(plugin, saveManager, fishLevelManager),
                "top", new TopHandler(plugin, saveManager),
                "resetlevel", new ResetLevelCommand(plugin, saveManager),
                "upgradepole", new UpgradePoleCommand(plugin, saveManager)
        );
    }

    /**
     * コマンドの使用方法を取得します。
     * プレイヤーがコマンドを誤って入力した場合に表示されるメッセージです。
     *
     * @return コマンドの使用方法
     */
    @Override
    protected String[] getMultilineUsageMessage() {
        return CustomLang.ofArray("Commands.Usage");
    }

    /**
     * コマンドの実行処理を行います。
     * サブコマンドを解析し、対応するハンドラーを呼び出します。
     */
    @Override
    protected void onCommand() {
        checkConsole();  // コマンドはプレイヤーのみが実行可能
        var handler = commandHandlers.get(args[0]);
        if (handler == null) {
            tell(getMultilineUsageMessage());
            return;
        }
        handler.execute((Player) sender, args);
    }

    /**
     * コマンドの補完候補を提供します。
     * プレイヤーがコマンドを入力中に、補完候補を表示します。
     *
     * @return 補完候補のリスト
     */
    @Override
    protected List<String> tabComplete() {
        if (sender.isOp()) {
            // op権限を持つプレイヤーは全てのコマンドを補完候補として表示
            if (args.length == 1) return completeLastWord(commandHandlers.keySet());
        } else {
            // 一般プレイヤーは、op権限を持たないコマンドのみを補完候補として表示
            if (args.length == 1) return commandHandlers.entrySet().stream()
                    .filter(entry -> !entry.getValue().useOnlyOp())
                    .map(HashMap.Entry::getKey)
                    .toList();
        }
        // サブコマンドの引数が2つ以上の場合、対応するハンドラーの補完候補を取得
        if (1 < args.length) {
            var handler = commandHandlers.get(args[0]);
            if (handler != null) return handler.tabComplete(args);
        }
        return List.of();
    }
}