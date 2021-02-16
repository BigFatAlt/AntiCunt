package me.bigfatalt.anticheat.check.api;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.JsonMessage;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.api.events.AntiCuntAlertEvent;
import me.bigfatalt.anticheat.api.events.PunishmentEvent;
import me.bigfatalt.anticheat.data.PlayerData;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.Collections;

public class Check {

    public String label;
    public int maxVL;
    public boolean autoban,enabled, experimental;

    public CheckType checkType;
    public Punishment punish;

    public Category category;

    public PunishmentType punishmentType;

    public final PlayerData playerData;

    public int vl;


    public Check(PlayerData data) {
        this.playerData = data;
        this.checkType = getClass().getAnnotation(CheckType.class);
        this.punish = getClass().getAnnotation(Punishment.class);

        if (!getClass().isAnnotationPresent(CheckType.class)) {
            System.out.println(Color.translate("&cCheck annotation not present in a class."));
        }

        if (!getClass().isAnnotationPresent(Punishment.class)) {
            return;
        }

        this.label = checkType.label();
        this.enabled = checkType.enabled();
        this.experimental = checkType.experimental();
        this.category = checkType.category();

        this.maxVL = punish.maxVL();
        this.autoban = punish.autoban();
        this.punishmentType = punish.punishment();

        if (this.experimental) this.label = checkType.label() + "*";

    }

    public void flag(String information) {
        if (!enabled) return;

        vl++;
        AntiCunt.instance.eventManager.callEvent(new AntiCuntAlertEvent(playerData, playerData.player, label, information, vl));
    }

    public void debug(String debug) {
        AntiCunt.instance.playerDataManager.getEntries().stream()
                .filter(dData -> dData.debuggingPlayer != null && dData.debuggingCheck != null && dData.debuggingCheck.label.equals(label) && dData.debuggingPlayer.equals(playerData.player.getUniqueId()))
                .forEach(dData -> dData.player.sendMessage(Color.translate("&8[&cDebug&8] &7" + debug)));
    }

    public void punish(String message) {
        if (!autoban) return;

        if (vl == maxVL) {
            AntiCunt.getInstance().eventManager.callEvent(new PunishmentEvent(punishmentType, playerData.player, message, playerData));
            vl = 0;
        }
    }



    public void handleCheck(Object object, long timeStamp){}

}
