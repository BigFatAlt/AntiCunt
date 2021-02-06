package me.bigfatalt.anticheat.api.events;

import cc.funkemunky.api.events.AtlasEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class AntiCuntAlertEvent extends AtlasEvent {

    public PlayerData data;
    public Player player;
    public String tag, information;
    public int violations;

}