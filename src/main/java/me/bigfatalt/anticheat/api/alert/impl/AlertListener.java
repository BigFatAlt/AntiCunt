package me.bigfatalt.anticheat.api.alert.impl;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.alert.event.AlertEvent;
import org.bukkit.Bukkit;

public class AlertListener implements AtlasListener {

    @Listen
    public void onAlert(AlertEvent event) {
        String message = "&5&lAntiCunt&d> &f" + event.player.getName() + " &7violated &f" + event.tag + " &d(&5" + event.violations + "&d)" ;


        AntiCunt.instance.alertExecutor.execute(() ->
                Bukkit.getOnlinePlayers()
                        .stream().filter(player -> event.data.alerts)
                        .forEach(player -> player.sendMessage(Color.translate(message))));
    }
}
