package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.manager.FishLevelManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.List;

/**
 * コマンドハンドラー: addexp
 * <p>
 * プレイヤーに経験値を追加するコマンドを処理します。
 * 使用権限はオペレーターのみです。
 */
public class AddExpHandler implements ICommandHandler {
    private final FishLevelManager manager;

    /**
     * コンストラクタ
     *
     * @param manager FishExpManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public AddExpHandler(FishLevelManager manager) {
        // 引数のチェック
        if (manager == null) throw new IllegalArgumentException("FishExpManager cannot be null");
        this.manager = manager;
    }

    @Override
    public void execute(Player sender, String[] args) {
        var lang = sender.locale().toLanguageTag();
        if (!sender.isOp()) {
            CustomLang.ofSimpleComponent("Commands.Invalid.PermissionMessage", lang).send(sender);
            return;
        }
        if (args.length != 3) {
            CustomLang.ofSimpleComponentList("Commands.addexp.Usage", lang).forEach(l -> l.send(sender));
            return;
        }
        var target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CustomLang.ofSimpleComponent("Commands.Invalid.PlayerNotFound", lang).send(sender);
            return;
        }
        int expToAdd;
        try {
            expToAdd = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            CustomLang.ofSimpleComponent("Commands.Invalid.NotANumber", lang).send(sender);
            return;
        }
        if (expToAdd <= 0) {
            CustomLang.ofSimpleComponent("Commands.Invalid.NegativeOrZero", lang).send(sender);
            return;
        }
        manager.addExperience(target, expToAdd);
        CustomLang.ofSimpleComponent("Commands.addexp.Sender", lang)
                .replace("{player}", target.getName())
                .replace("{expValue}", expToAdd)
                .send(sender);
        CustomLang.ofSimpleComponent("Commands.addexp.Target", lang)
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

    @Override
    public List<String> permList() {
        return List.of(
                "fishingplugin.op"
        );
    }
}