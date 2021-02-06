package me.bigfatalt.anticheat.api.events;

import cc.funkemunky.api.events.AtlasEvent;
import lombok.AllArgsConstructor;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PunishmentEvent extends AtlasEvent {

    public PunishmentType punishmentType;
    public Player player;
    public String reason;
    public PlayerData data;

}
