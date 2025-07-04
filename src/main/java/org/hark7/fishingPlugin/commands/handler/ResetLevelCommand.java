package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;

public class ResetLevelCommand implements ICommandHandler {
    private final PlayerDataManager manager;

    public ResetLevelCommand(PlayerDataManager manager) {
        this.manager = manager;
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

        if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("all")) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                var uuid = p.getUniqueId();
                var data = new PlayerData(p.getName(), 1, 0);
                manager.setPlayerData(uuid, data);
            });
            CustomLang.ofSimpleComponent("Commands.resetlevel.All").send(player);
            return;
        }

        var targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {
            CustomLang.ofSimpleComponent("Commands.Invalid.PlayerNotFound").send(player);
            return;
        }

        var uuid = targetPlayer.getUniqueId();
        var data = new PlayerData(targetPlayer.getName(), 1, 0);
        manager.setPlayerData(uuid, data);
        CustomLang.ofSimpleComponent("Commands.resetlevel.Player")
                .replace("{player}", player.getName()).send(player);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            var list = new ArrayList<String>();
            list.add("@a");
            list.add("all");
            Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
            return list;
        }
        return List.of();
    }
}
