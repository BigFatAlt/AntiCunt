package me.bigfatalt.anticheat.api.alert.impl;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.ConfigSetting;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.alert.event.AlertEvent;
import me.bigfatalt.anticheat.api.settings.ConfigSettings;
import org.bukkit.Bukkit;


public class AlertListener implements AtlasListener {

    @Listen
    public void onAlert(AlertEvent event) {
        String message = ConfigSettings.alertMessage.replace("%player", event.player.getName()).replace("%name", event.tag).replace("%vl", String.valueOf(event.violations));


        AntiCunt.instance.alertExecutor.execute(() ->
                Bukkit.getOnlinePlayers()
                        .stream().filter(player -> event.data.alerts)
                        .forEach(player -> player.sendMessage(Color.translate(message))));
    }
}
