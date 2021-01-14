package me.bigfatalt.anticheat.api.commands.impl;

import cc.funkemunky.api.utils.Color;
import com.qrakn.honcho.command.CommandMeta;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.commands.AntiCuntCommand;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.image.ColorConvertOp;

@CommandMeta(label = "alerts", permission = "anticunt.command.alerts")
public class Alerts extends AntiCuntCommand {

    @Override
    public void execute(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cConsole can not use this command."));
            return;
        }

        Player player = (Player) sender;
        PlayerData data = AntiCunt.instance.dataManager.getData(player);

        data.alerts = !data.alerts;
        String message = "&5[AntiCunt] &falerts have been turned " + (data.alerts ? "&aON" : "&cOFF");
        data.player.sendMessage(Color.translate(message));

    }
}
