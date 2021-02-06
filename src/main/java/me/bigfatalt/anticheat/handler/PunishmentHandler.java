package me.bigfatalt.anticheat.handler;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.events.PunishmentEvent;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Init
public class PunishmentHandler implements AtlasListener {


    @Listen
    public void onPunishment(PunishmentEvent event) {
        PunishmentType punishmentType = event.punishmentType;
        Player player = event.player;
        String reason = event.reason;

        switch (punishmentType) {
            case KICK:
                String[] KICK_BYPASS = {
                                "&e&m---------------------------------\n" +
                                "&6AntiCunt » &eYou would have been kicked but you are op\n" +
                                "&e&m---------------------------------"};

                String[] KICK_BROADCAST = {
                                "&e&m---------------------------------\n" +
                                "&6AntiCunt » &6" + player.getName() + " &ehas been removed from the server\n" +
                                "&e&m---------------------------------"};


                if (player.isOp() || player.hasPermission("AntiCunt.bypass")) {
                    for (String message : KICK_BYPASS) {
                        player.sendMessage(Color.translate(message));
                    }
                    return;
                }

                new BukkitRunnable() {
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player.getName() + " " + Color.translate("&5&l[AntiCunt] &7You have been kicked for " + reason));
                        for (String message : KICK_BROADCAST) {
                            Bukkit.broadcastMessage(Color.translate(message));
                        }
                    }
                }.runTaskLater(AntiCunt.instance, 30);

                break;
            case BAN:
                String[] BAN_BYPASS = {
                                "&e&m---------------------------------\n" +
                                "&6AntiCunt » &6You would have been banned but you are op\n" +
                                "&e&m---------------------------------"};

                String[] BAN_BROADCAST = {
                                "&e&m---------------------------------\n" +
                                "&6AntiCunt » &6" + player.getName() + " &ehas been removed from the server\n" +
                                "&e&m---------------------------------"};


                if (player.isOp() || player.hasPermission("AntiCunt.bypass")) {
                    for (String message : BAN_BYPASS) {
                        player.sendMessage(Color.translate(message));
                    }
                    return;
                }

                new BukkitRunnable() {
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " " + Color.translate("&6&l[AntiCunt] &7You have been banned for Anti-Cheat Detection"));
                        for (String message : BAN_BROADCAST) {
                            Bukkit.broadcastMessage(Color.translate(message));
                        }
                    }
                }.runTaskLater(AntiCunt.instance, 30);

                break;
        }
    }
}
