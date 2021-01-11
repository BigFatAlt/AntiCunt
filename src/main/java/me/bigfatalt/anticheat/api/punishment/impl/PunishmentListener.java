package me.bigfatalt.anticheat.api.punishment.impl;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.tinyprotocol.api.TinyProtocolHandler;
import cc.funkemunky.api.tinyprotocol.packet.out.WrappedPacketPlayOutWorldParticle;
import cc.funkemunky.api.tinyprotocol.packet.types.enums.WrappedEnumParticle;
import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentEvent;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PunishmentListener implements AtlasListener {

    @Listen
    public void onPunishment(PunishmentEvent event) {
        PunishmentType punishmentType = event.punishmentType;
        Player player = event.player;
        String reason = event.reason;
        Check check = event.check;

        switch (punishmentType) {
            case KICK:
                String[] KICK_BYPASS = {
                                "&d&m---------------------------------\n" +
                                "&dAntiCunt » &5You would have been kicked but you are op\n" +
                                "&d&m---------------------------------"};

                String[] KICK_BROADCAST = {
                                "&d&m---------------------------------\n" +
                                "&dAntiCunt » &5" + player.getName() + " &5has been removed from the server\n" +
                                "&d&m---------------------------------"};


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
                                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(Color.translate(message)));
                            }
                        }
                    }.runTaskLater(AntiCunt.instance, 30);

                break;
            case BAN:
                String[] BAN_BYPASS = {
                                "&d&m---------------------------------\n" +
                                "&dAntiCunt » &5You would have been banned but you are op\n" +
                                "&d&m---------------------------------"};

                String[] BAN_BROADCAST = {
                                "&d&m---------------------------------\n" +
                                "&dAntiCunt » &5" + player.getName() + " &5has been removed from the server\n" +
                                "&d&m---------------------------------"};


                if (!check.autoban) return;

                if (player.isOp() || player.hasPermission("AntiCunt.bypass")) {
                    for (String message : BAN_BYPASS) {
                        player.sendMessage(Color.translate(message));
                    }
                    return;
                }

                new BukkitRunnable() {
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " " + Color.translate("&d&l[AntiCunt] &7You have been banned for Anti-Cheat Detection"));
                        for (String message : BAN_BROADCAST) {
                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(Color.translate(message)));
                        }
                    }
                }.runTaskLater(AntiCunt.instance, 30);

                break;
            case CANCEL:
                Location to = player.getLocation();
                Location from = player.getLocation();

                Vector vector = to.toVector();


                to.setDirection(vector.setX(from.getX()));
                break;

        }

    }

    public void banAnimation(Player player, Location location) {
        TinyProtocolHandler.sendPacket(player, new WrappedPacketPlayOutWorldParticle(WrappedEnumParticle.EXPLOSION_NORMAL, true , (float) location.getX(),(float) location.getY(),(float) location.getZ(), 0F, 0F, 0F, 1, 5));

    }
}
