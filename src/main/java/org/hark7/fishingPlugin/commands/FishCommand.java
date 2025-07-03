package org.hark7.fishingPlugin.commands;

import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.handler.AddExpHandler;
import org.hark7.fishingPlugin.commands.handler.ICommandHandler;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.hark7.fishingPlugin.util.CustomLang;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;
import java.util.List;


public final class FishCommand extends SimpleCommand {
    private final HashMap<String, ICommandHandler> commandHandlers = new HashMap<>();

    public FishCommand(FishExpManager manager) {
        super("fish");
        setDescription("FishPlugin Commands");
        setMinArguments(1);
        registerCommmandHandlers(manager);
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        return CustomLang.ofArray("Commands.Usage");
    }

    private void registerCommmandHandlers(FishExpManager manager) {
        commandHandlers.put("addexp", new AddExpHandler(manager));
    }

    @Override
    protected void onCommand() {
        checkConsole();  // コマンドはプレイヤーのみが実行可能
        commandHandlers.get(args[0]).execute((Player) sender, args);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) return completeLastWord(commandHandlers.keySet());
        return commandHandlers.get(args[0]).tabComplete(args);
    }
}