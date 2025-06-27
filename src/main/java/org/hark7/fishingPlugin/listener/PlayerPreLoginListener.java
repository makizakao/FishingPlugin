package org.hark7.fishingPlugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.hark7.fishingPlugin.FishingPlugin;

public class PlayerPreLoginListener implements Listener {
    FishingPlugin plugin;

    public PlayerPreLoginListener(FishingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        var playerUUID = event.getUniqueId();
        String name = event.getName();
        if (!existPlayerData(playerUUID)) {
            plugin.createPlayerData(name, playerUUID);
        }
    }

    private boolean existPlayerData(java.util.UUID playerUUID) {
        return plugin.playerDataMap().containsKey(playerUUID);
    }
}
