package me.bigfatalt.anticheat.handler;

import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import me.bigfatalt.anticheat.AntiCunt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

@Init
public class ConnectionHandler implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        AntiCunt.instance.playerDataManager.remove(event.getPlayer());
    }
}
