package org.hark7.fishingPlugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.Lang;

public class AddExpCommand extends SimpleSubCommand {
    private final FishExpManager manager;

    public AddExpCommand(FishExpManager manager) {
        super("addexp");
        this.manager = manager;
        setPermission(Lang.of("Commands.AddExp.Permission"));
        this.setMinArguments(1);
        this.setDescription(Lang.of("Commands.addExp.Description"));
    }

    protected String[] getMultilineUsageMessage() {
        return Lang.ofArray(Lang.of("Commands.addExp.Usage"));
    }

    protected void onCommand() {
        if (!sender.isOp()) {
            tell(Lang.of("Commands.Invalid.PermissionMessage"));
            return;
        }

        if (args.length != 2) {
            tell(Lang.of("Commands.addExp.Usage"));
            return;
        }

        var target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            tell(Lang.of("Commands.Invalid.PlayerNotFound"));
            return;
        }

        int expToAdd;
        try {
            expToAdd = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            tell(Lang.of("Commands.Invalid.NotANumber"));
            return;
        }

        if (expToAdd <= 0) {
            tell(Lang.of("Commands.Invalid.NegativeOrZero"));
            return;
        }

        manager.addExperience(target, expToAdd);
        tell(Lang.of("Commands.addExp.Sender"));
        tell(Lang.of("Commands.addExp.Target"));
    }
}