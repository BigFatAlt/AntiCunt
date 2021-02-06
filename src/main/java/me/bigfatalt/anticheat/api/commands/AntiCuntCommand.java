package me.bigfatalt.anticheat.api.commands;

import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AntiCuntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("AntiCunt.commands")) {
            commandSender.sendMessage(Color.translate("&cYou do not have enough permissions to execute this command."));
            return true;
        }

        Player player = (Player) commandSender;
        PlayerData data = AntiCunt.instance.playerDataManager.getData(player);

        if (args.length == 0) {
            commandSender.sendMessage(Color.translate("&c/anticunt <alerts|debug|GUI>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("alerts")) {

            data.alertsEnabled = !data.alertsEnabled;
            String message = "&6&lAntiCunt&e> &falerts have been turned " + (data.alertsEnabled ? "&aEnabled" : "&cDisabled");
            data.player.sendMessage(Color.translate(message));
            return true;
        }

        if (args[0].equalsIgnoreCase("debug")) {
            if (args.length > 1) {

                if (args[1].equalsIgnoreCase("none")) {
                    data.debuggingCheck = null;
                    data.debuggingPlayer = null;

                    data.player.sendMessage(Color.translate("&6&lAntiCunt&e> &cStopped debugging."));
                    return true;
                }
                Check check = data.checkManager.getCheck(args[1].replaceAll("_", " "));

                if (check == null) {
                    player.sendMessage(Color.translate("&6&lAntiCunt&e> &c" + args[1] + " is not a check."));
                    return true;
                }


                if (args.length == 2) {
                    data.debuggingCheck = check;
                    data.debuggingPlayer = player.getUniqueId();

                    player.sendMessage(Color.translate("&6&lAntiCunt&e> &cyou are now debugging " + check.label + "."));
                    return true;
                }

                return true;
            } else player.sendMessage(Color.translate("&6&lAntiCunt&e> &c<none|check>"));

            return true;
        }



        return false;
    }
}
