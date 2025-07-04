package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.entity.Player;

import java.util.List;

public interface ICommandHandler {
    boolean useOnlyOp();

    void execute(Player player, String[] args);

    List<String> tabComplete(String[] args);
}
