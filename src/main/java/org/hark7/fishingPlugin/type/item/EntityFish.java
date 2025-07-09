package org.hark7.fishingPlugin.type.item;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerFishEvent;
import org.hark7.fishingPlugin.FishingPlugin;
import org.hark7.fishingPlugin.manager.FishLevelManager;

@SuperBuilder
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class EntityFish implements Fishable {
    private final Component name;
    private final Rarity rarity;
    private final EntityType entity;

    @Override
    public void onFish(PlayerFishEvent event, FishLevelManager manager, FishingPlugin plugin) {
        var location = event.getHook().getLocation();
        var world = event.getHook().getWorld();
        Bukkit.getScheduler().runTask(plugin, () -> {
            world.spawnEntity(location, entity);
            if (event.getCaught() != null) event.getCaught().remove();
        });
    }

    @Override
    public Component name() {
        return name;
    }

    @Override
    public FishType fishType() {
        return FishType.Entity;
    }

    @Override
    public Rarity rarity() {
        return rarity;
    }

    public abstract static class EntityFishBuilder<C extends EntityFish, B extends EntityFishBuilder<C, B>> {

        public B nameFromEntity() {
            if (entity == null) throw new IllegalStateException("EntityType must be set before.");
            if (this.name != null)
                throw new IllegalStateException("'name' is already set. Cannot set it again from EntityType.");
            this.name = Component.translatable(entity.translationKey());
            return self();
        }
    }
}
