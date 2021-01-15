package me.bigfatalt.anticheat.api.commands;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.commands.FunkeArgument;
import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentEvent;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AntiCuntCommand implements CommandExecutor {

    private final AntiCunt plugin;

    public AntiCuntCommand(AntiCunt plugin) {
        this.plugin = plugin;

        plugin.getCommand("AntiCunt").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!sender.hasPermission("anticunt.*") || !sender.isOp()) {
            sender.sendMessage(Color.translate("&cYou do not have the right permissions to execute this command."));
            return true;
        }

        String[] help = {
                "&d&m--------------------------",
                " ",
                "&d&lAntiCunt command help: ",
                " ",
                "&5* &7/anticunt alerts - enable/disable alerts.",
                "&5* &7/anticunt ban <player> - ban a player through anticunt.",
                "&5* &7/anticunt reload <config|server> - reload either the server or the config.",
                "&5* &7/anticunt check <name> <enable|disable> - enable/disable a check.",
                "&5* &7/anticunt checks - list of the checks.",
                "&5* &7/anticunt debug <check> - debug a check..",
                "&5* &7/anticunt gui - open the anticunt gui.",
                " ",
                "&d&m--------------------------"};


        if (args.length == 0) {
            for (String message : help) {
                sender.sendMessage(Color.translate(message));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("alerts")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Color.translate("&cConsole can not use this command."));
                return true;
            }
            PlayerData data = AntiCunt.instance.dataManager.getData((Player) sender);

            data.alerts = !data.alerts;
            String message = "&5[AntiCunt] &falerts have been turned " + (data.alerts ? "&aON" : "&cOFF");
            data.player.sendMessage(Color.translate(message));
            return true;
        }

        if (args[0].equalsIgnoreCase("ban")) {
            Player target = Bukkit.getPlayer(args[1]);

            PlayerData data = AntiCunt.instance.dataManager.getData(target);

            sender.sendMessage(Color.translate("&5[AntiCunt] &c" + target.getName() + " will be banned in the next 5 seconds."));
            Atlas.getInstance().getEventManager().callEvent(new PunishmentEvent(PunishmentType.BAN, target, "manual ban", data));
            sender.sendMessage(Color.translate("&5[AntiCunt] &a" + target.getName() + " has been banned."));

        }

        if (args[0].equalsIgnoreCase("debug")) {
            PlayerData data = AntiCunt.instance.dataManager.getData((Player) sender);

            if (args[1].equalsIgnoreCase("none")) {
                data.debuggingCheck = null;
                data.debuggingPlayer = null;

                data.player.sendMessage(Color.translate("&5[AntiCunt] &cStopped debugging."));
                return true;
            }
            Check check = data.getCheck(args[1].replaceAll("_", " "));

            if (check == null) {
                sender.sendMessage(Color.translate("&5[AntiCunt] &c" + args[1] + " is not a check."));
                return true;
            }

            if (args.length == 2) {
                data.debuggingCheck = check;
                data.debuggingPlayer = ((Player) sender).getUniqueId();

                sender.sendMessage(Color.translate("&5[AntiCunt] &cyou are now debugging " + check.name + "."));
                return true;
            }

            return true;
        }

        return true;
    }
}
