package me.bigfatalt.anticheat.api.commands.impl;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.Color;
import com.qrakn.honcho.command.CommandMeta;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.commands.AntiCuntCommand;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentEvent;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "ban", permission = "anticunt.permission.ban")
public class Ban extends AntiCuntCommand {

    @Override
    public void execute(CommandSender sender, String args) {
        Player target = Bukkit.getPlayer(args);

        PlayerData data = AntiCunt.instance.dataManager.getData(target);

        sender.sendMessage(Color.translate("&5[AntiCunt] &c" + target.getName() + " will be banned in the next 5 seconds."));
        Atlas.getInstance().getEventManager().callEvent(new PunishmentEvent(PunishmentType.BAN, target, "manual ban", data));
        sender.sendMessage(Color.translate("&5[AntiCunt] &a" + target.getName() + " has been banned."));

    }
}
