package me.bigfatalt.anticheat.handler;

import cc.funkemunky.api.events.AtlasEvent;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.JsonMessage;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.events.AntiCuntAlertEvent;
import me.bigfatalt.anticheat.data.PlayerData;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;

@Init
public class AlertHandler implements AtlasListener {

    @Listen
    public void onAlert(AntiCuntAlertEvent event)  {
        PlayerData data = event.data;
        String label = event.tag;
        int vl = event.violations;

        final TextComponent alertMessage = new TextComponent(Color.translate("&6&lAntiCunt&e> &f" + event.player.getName() + " &7violated &f" + label + " &7[" + vl + "]"));

            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    Color.translate("&6&lInformation: \n&f" + event.information + "\n" + "\n&6&lPing:" + "\n&f" + data.misc.ping + "\n")).create()));


        AntiCunt.instance.alertExecutor.execute(() -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> data.alertsEnabled || data.devServer)
                    .forEach(p -> p.spigot().sendMessage(alertMessage));
        });
    }
}
