package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

public class AddExpHandler implements ICommandHandler {
    private final FishExpManager manager;

    public AddExpHandler(FishExpManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Player sender, String[] args) {
        var lang = sender.locale().toLanguageTag();
        if (!sender.isOp()) {
            CustomLang.ofComponent("Commands.Invalid.PermissionMessage", lang).send(sender);
            return;
        }
        if (args.length != 3) {
            CustomLang.ofComponentList("Commands.addexp.Usage", lang).forEach(l -> l.send(sender));
            return;
        }
        var target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CustomLang.ofComponent("Commands.Invalid.PlayerNotFound", lang).send(sender);
            return;
        }
        int expToAdd;
        try {
            expToAdd = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            CustomLang.ofComponent("Commands.Invalid.NotANumber", lang).send(sender);
            return;
        }
        if (expToAdd <= 0) {
            CustomLang.ofComponent("Commands.Invalid.NegativeOrZero", lang).send(sender);
            return;
        }
        manager.addExperience(target, expToAdd);
        CustomLang.ofComponent("Commands.addexp.Sender", lang)
                .replace("{player}", target.getName())
                .replace("{expValue}", expToAdd)
                .send(sender);
        CustomLang.ofComponent("Commands.addexp.Target", lang)
                .replace("{expValue}", expToAdd)
                .send(target);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
        return List.of();
    }
}