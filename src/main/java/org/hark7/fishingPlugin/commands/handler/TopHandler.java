package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.handler.top.*;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.*;

public class TopHandler implements ICommandHandler {
    private final PlayerDataManager manager;
    private final Map<String, ICommandHandler> handlers = new HashMap<>();

    public TopHandler(PlayerDataManager manager) {
        this.manager = manager;
        registerHandlers();
    }

    private void registerHandlers() {
        handlers.put("level", new TopLevelCommand(manager));
        handlers.put("total", new TopTotalCommand(manager));
        handlers.put("scrap", new TopScrapCommand(manager));
        handlers.put("common", new TopCommonCommand(manager));
        handlers.put("uncommon", new TopUncommonCommand(manager));
        handlers.put("rare", new TopRareCommand(manager));
        handlers.put("epic", new TopEpicCommand(manager));
        handlers.put("legendary", new TopLegendaryCommand(manager));
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
        handlers.get(args[1]).execute(player, args);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(handlers.keySet());
        } else if (args.length == 3) {
            return handlers.get(args[1]).tabComplete(args);
        }
        return List.of();
    }
}