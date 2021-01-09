package me.bigfatalt.anticheat.data.manager;

import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    public static final Map<Player, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    public PlayerData getData(final Player player) {
        return playerDataMap.computeIfAbsent(player, uuid -> new PlayerData(player));
    }

    public PlayerData remove(final Player player) {
        final UUID uuid = player.getUniqueId();

        return playerDataMap.remove(uuid);
    }

    public Collection<PlayerData> getEntries() {
        return playerDataMap.values();
    }
}