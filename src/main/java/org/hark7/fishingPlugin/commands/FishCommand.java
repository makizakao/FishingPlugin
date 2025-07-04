package org.hark7.fishingPlugin.commands;

import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.handler.*;
import org.hark7.fishingPlugin.commands.handler.top.UpgradePoleCommand;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.util.CustomLang;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;
import java.util.List;


public final class FishCommand extends SimpleCommand {
    private final HashMap<String, ICommandHandler> commandHandlers = new HashMap<>();

    public FishCommand(PlayerDataManager playerDataManager, FishExpManager fishExpManager) {
        super("fish");
        setDescription("FishPlugin Commands");
        setMinArguments(1);
        registerCommandHandlers(playerDataManager, fishExpManager);
    }

    private void registerCommandHandlers(PlayerDataManager playerDataManager, FishExpManager fishExpManager) {
        commandHandlers.put("addexp", new AddExpHandler(fishExpManager));
        commandHandlers.put("stats", new StatsHandler(playerDataManager, fishExpManager));
        commandHandlers.put("top", new TopHandler(playerDataManager));
        commandHandlers.put("resetlevel", new ResetLevelCommand(playerDataManager));
        commandHandlers.put("upgradepole", new UpgradePoleCommand(playerDataManager));
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        return CustomLang.ofArray("Commands.Usage");
    }

    @Override
    protected void onCommand() {
        checkConsole();  // コマンドはプレイヤーのみが実行可能
        commandHandlers.get(args[0]).execute((Player) sender, args);
    }

    @Override
    protected List<String> tabComplete() {
        if (sender.isOp()) {
            if (args.length == 1) return completeLastWord(commandHandlers.keySet());
        } else {
            if (args.length == 1) return commandHandlers.entrySet().stream()
                    .filter(entry -> !entry.getValue().useOnlyOp())
                    .map(HashMap.Entry::getKey)
                    .toList();
        }
        return commandHandlers.get(args[0]).tabComplete(args);
    }
}