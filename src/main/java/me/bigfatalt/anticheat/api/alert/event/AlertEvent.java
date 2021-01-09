package me.bigfatalt.anticheat.api.alert.event;

import cc.funkemunky.api.events.AtlasEvent;
import lombok.AllArgsConstructor;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class AlertEvent extends AtlasEvent {

    public PlayerData data;
    public Player player;
    public String tag;
    public int violations;

}
