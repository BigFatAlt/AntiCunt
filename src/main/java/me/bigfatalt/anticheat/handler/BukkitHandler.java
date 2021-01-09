package me.bigfatalt.anticheat.handler;

import me.bigfatalt.anticheat.AntiCunt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitHandler implements Listener {

    private final AntiCunt plugin;

    public BukkitHandler(AntiCunt plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        AntiCunt.instance.dataManager.remove(event.getPlayer());

    }
}
