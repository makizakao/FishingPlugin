package org.hark7.fishingPlugin.commands.handler.top;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.handler.ICommandHandler;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataController;
import org.hark7.fishingPlugin.type.item.Fishable.Rarity;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * コマンドハンドラー: top epic
 * <p>
 * プレイヤーの「EPIC」レアリティの魚の数をランキング形式で表示するコマンドを処理します。
 */
public class TopEpicCommand implements ICommandHandler {
    private final PlayerDataController manager;

    /**
     * コンストラクタ
     *
     * @param manager PlayerDataManagerのインスタンス
     * @throws IllegalArgumentException 引数がnullの場合にスローされます。
     */
    public TopEpicCommand(PlayerDataController manager) {
        if (manager == null) throw new IllegalArgumentException("PlayerDataManager cannot be null");
        this.manager = manager;
    }

    @Override
    public void execute(Player player, String[] args) {
        var playerDataList = manager.playerDataMap();
        var sortedList = new ArrayList<Map.Entry<UUID, Integer>>();
        for (Map.Entry<UUID, PlayerData> entry : playerDataList.entrySet()) {
            UUID playerId = entry.getKey();
            PlayerData data = entry.getValue();
            sortedList.add(Map.entry(playerId, data.count(Rarity.EPIC)));
        }
        sortedList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // 降順ソート
        sendLank(player, sortedList);
    }

    /**
     * ランキングをプレイヤーに送信します。
     *
     * @param sender     ランキングを受け取るプレイヤー
     * @param sortedList ランキングデータのリスト
     */
    private void sendLank(Player sender, List<Map.Entry<UUID, Integer>> sortedList) {
        var lang = sender.locale().toLanguageTag();
        var title = CustomLang.of("Commands.top.epic.Title", lang);
        CustomLang.ofSimpleComponent("Commands.top.Title", lang).replace("{title}", title).send(sender);
        for (int i = 0; i < Math.min(10, sortedList.size()); i++) {
            Map.Entry<UUID, Integer> entry = sortedList.get(i);
            var name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
            CustomLang.ofSimpleComponent("Commands.top.epic.Content", lang)
                    .replace("{rank}", String.valueOf(i + 1))
                    .replace("{player}", name != null ? name : "Unknown Player")
                    .replace("{total}", entry.getValue())
                    .send(sender);
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return List.of();
    }

    @Override
    public List<String> permList() {
        return List.of();
    }
}
