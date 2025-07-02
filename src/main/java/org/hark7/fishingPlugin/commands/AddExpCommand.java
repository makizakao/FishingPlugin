package org.hark7.fishingPlugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.Lang;

public class AddExpCommand extends SimpleSubCommand {
    private final PlayerDataManager manager;

    protected AddExpCommand(PlayerDataManager manager) {
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

        addExperience(target, expToAdd);
        sender.sendMessage(ChatColor.GREEN + target.getName() + "に" + expToAdd + "の経験値を追加しました。");
        target.sendMessage(ChatColor.GREEN + "管理者により" + expToAdd + "の経験値が追加されました。");
    }

    /**
     * プレイヤーの経験値を追加します。
     * レベルアップ時にはメッセージを送信します。
     *
     * @param player プレイヤー
     * @param exp    追加する経験値
     */
    public void addExperience(Player player, int exp) {
        var playerUUID = player.getUniqueId();
        var playerData = manager.playerDataMap().get(playerUUID);
        int currentExp = playerData.exp() + exp;
        int currentLevel = playerData.level();

        while (currentExp >= getRequiredExp(currentLevel)) {
            currentExp -= getRequiredExp(currentLevel);
            currentLevel++;

            player.sendMessage(Component
                    .text("釣りレベルが上がりました！ 現在のレベル: ")
                    .append(Component.text(currentLevel))
                    .color(NamedTextColor.GOLD));
        }
        manager.setPlayerExp(playerUUID, currentExp);
        manager.setPlayerLevel(playerUUID, currentLevel);
    }
    public int getRequiredExp(int level) {
        return 100 * level * level;
    }
}