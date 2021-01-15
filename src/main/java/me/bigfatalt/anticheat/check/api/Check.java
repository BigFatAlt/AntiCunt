package me.bigfatalt.anticheat.check.api;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.Color;
import com.google.common.collect.Lists;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.alert.event.AlertEvent;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentEvent;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.data.PlayerData;

import java.util.List;

public abstract class Check {

    public String name,description;
    public boolean autoban,enabled;
    public int maxVL;

    public int vl;

    public CheckType type;

    public CheckInfo info;

    public final PlayerData data;

    public final List<Long> alerts = Lists.newArrayList();

    public Check(PlayerData data) {
        this.data = data;
        this.info = this.getClass().getAnnotation(CheckInfo.class);

        this.name = info.name();
        this.description = info.description();
        this.autoban = info.autoban();
        this.enabled = info.enabled();
        this.maxVL = info.maxVl();
        this.type = info.type();

    }

    public void fail() {
        long now = System.currentTimeMillis();

        if (alerts.contains(now)) return;

        alerts.add(now);

        vl = (int) alerts.stream().filter(violation -> violation + 9000L > System.currentTimeMillis()).count();

        Atlas.getInstance().getEventManager().callEvent(new AlertEvent(data, data.player, name, vl));
    }

    public void punish(String punish, PunishmentType punishmentType) {
        if (!autoban) return;

        if (vl == maxVL) {
            Atlas.getInstance().getEventManager().callEvent(new PunishmentEvent(punishmentType, data.player, punish, data));
            alerts.clear();
        }
    }

    public void debug(String debug) {
        AntiCunt.instance.dataManager.playerDataMap.values().stream()
                .filter(dData -> dData.debuggingPlayer != null && dData.debuggingCheck != null && dData.debuggingCheck.name.equals(name) && dData.debuggingPlayer.equals(data.player.getUniqueId()))
                .forEach(dData -> dData.player.sendMessage(Color.translate("&c[DEBUG] &7" + debug)));

    }


   public abstract void handlePacket(Object o);
}
