package org.hark7.fishingPlugin.commands.handler.top;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hark7.fishingPlugin.commands.handler.ICommandHandler;
import org.hark7.fishingPlugin.database.PlayerData;
import org.hark7.fishingPlugin.database.PlayerDataManager;
import org.hark7.fishingPlugin.util.CustomLang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TopLevelCommand implements ICommandHandler {
    private final PlayerDataManager manager;

    public TopLevelCommand(PlayerDataManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean useOnlyOp() {
        return false;
    }

    @Override
    public void execute(Player player, String[] args) {
        var playerDataList = manager.playerDataMap();
        var sortedList = new ArrayList<Map.Entry<UUID, Integer>>();
        for (Map.Entry<UUID, PlayerData> entry : playerDataList.entrySet()) {
            UUID playerId = entry.getKey();
            PlayerData data = entry.getValue();
            sortedList.add(Map.entry(playerId, data.level()));
        }
        sortedList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // 降順ソート
        sendLank(player, sortedList);
    }

    private void sendLank(Player sender, List<Map.Entry<UUID, Integer>> sortedList) {
        var lang = sender.locale().toLanguageTag();
        var title = CustomLang.of("Commands.top.level.Title", lang);
        CustomLang.ofSimpleComponent("Commands.top.Title", lang).replace("{title}", title).send(sender);
        for (int i = 0; i < Math.min(10, sortedList.size()); i++) {
            Map.Entry<UUID, Integer> entry = sortedList.get(i);
            var name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
            CustomLang.ofSimpleComponent("Commands.top.level.Content", lang)
                    .replace("{rank}", String.valueOf(i + 1))
                    .replace("{player}", name != null ? name : "Unknown Player")
                    .replace("{level}", entry.getValue())
                    .send(sender);
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return List.of();
    }
}
