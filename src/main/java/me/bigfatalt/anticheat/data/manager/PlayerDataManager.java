package me.bigfatalt.anticheat.data.manager;

import com.google.common.collect.Maps;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = Maps.newConcurrentMap();

    public PlayerData getData(final Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(player.getUniqueId()));
    }

    public PlayerData remove(final Player player) {
        final UUID uuid = player.getUniqueId();

        return playerDataMap.remove(uuid);
    }

    public Collection<PlayerData> getEntries() {
        return playerDataMap.values();
    }
}