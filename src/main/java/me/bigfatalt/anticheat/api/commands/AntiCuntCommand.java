package me.bigfatalt.anticheat.api.commands;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.api.utils.menu.button.Button;
import cc.funkemunky.api.utils.menu.button.ClickAction;
import cc.funkemunky.api.utils.menu.type.impl.ChestMenu;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntiCuntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("AntiCunt.commands")) {
            commandSender.sendMessage(Color.translate("&cYou do not have enough permissions to execute this command."));
            return true;
        }

        Player player = (Player) commandSender;
        PlayerData data = AntiCunt.instance.playerDataManager.getData(player);

        String[] help = {
                "&7&m--------------------------",
                " ",
                "&6&lAntiCunt &ecommand help:",
                " ",
                "&6* &7/anticunt alerts - enable/disable anticheat alerts.",
                "&6* &7/anticunt debug <check|none> - debug a check.",
                "&6* &7/anticunt resetvl - reset check vls.",
                "&6* &7/anticunt gui - AntiCunt GUI.",
                " ",
                "&7&m--------------------------"};

        if (args.length == 0) {
            commandSender.sendMessage(Color.translate("&c/anticunt <alerts|debug|GUI|V>"));
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

        if (args[0].equalsIgnoreCase("resetVl")) {
            data.resetVLs();
            player.sendMessage(Color.translate("&6&lAntiCunt&e> &aReset all checks Vls."));
            return true;
        }

        if (args[0].equalsIgnoreCase("gui")) {

            openMainMenu(player, data);
            player.sendMessage(Color.translate("&6&lAntiCunt&e> &aopening gui....."));
            return true;
        }

        return false;
    }

    private static Button createButton(boolean moveable, ItemStack stack, ClickAction action) {
        return new Button(moveable, stack, action);
    }

    public void openMainMenu(Player player, PlayerData playerData) {
        ChestMenu chestMenu = new ChestMenu(Color.translate("&6&lAntiCunt GUI"), 3);

        chestMenu.setItem(11, createButton(false, MiscUtils.createItem(Material.CHEST, 1, Color.Yellow + "Check Settings"), ((player2, informationPair) -> {
            if(informationPair.getClickType().toString().contains("LEFT")) {
                openCheckGUI(player2, playerData);
            }
        })));

        chestMenu.setItem(15, createButton(false, MiscUtils.createItem(Material.REDSTONE, 1, Color.Yellow + "Todo add this"), ((player2, informationPair) -> {
            if(informationPair.getClickType().toString().contains("LEFT")) {
                player.sendMessage("ADD");
            }
        })));

        chestMenu.setItem(13, mainButton());

        chestMenu.showMenu(player);
    }

    private void openCheckGUI(Player player2, PlayerData data) {
        ChestMenu menu = new ChestMenu(Color.Gold + "Edit Checks", 6);

        data.checkManager.getChecks().forEach(check -> menu.addItem(check(check, data)));


        menu.showMenu(player2);
    }

    public Button mainButton() {
        String[] lore = {
                "&eYou are currently using AntiCunt Version " + AntiCunt.instance.getDescription().getVersion() + ".",
                "&eif you need support please join our discord by left clicking this book."};

        String message = null;

        for (String msg : lore) {
            message = msg;
        }


        return createButton(  false, MiscUtils.createItem(Material.BOOK, 1, Color.translate("&6AntiCunt"),"&eYou are currently using AntiCunt Version " + AntiCunt.instance.getDescription().getVersion() + ".", "&eif you need support please join our discord by left clicking this book."), (player, informationPair) -> {
            switch (informationPair.getClickType()) {
                case LEFT:
                    player.sendMessage(Color.translate("&6&lAntiCunt&e> &fhttps://discord.gg/vn5UGvrF9b"));
                    break;
            }
        });
    }

    public Button check(Check check, PlayerData data) {
        List<Check> checks = new ArrayList<>(data.checkManager.getChecks());

        return createButton(false,
                MiscUtils.createItem(
                        Material.PAPER,
                        1,
                        Color.Gold + check.label,
                        Color.translate(
                                "&eEnabled: " + (check.enabled ? "&aEnabled" : "&cDisabled")),
                                   Color.translate("&eAutoBan: " + (check.autoban ? "&aEnabled" : "&cDisabled"))),
                ((player2, infoPair) -> {
                    switch(infoPair.getClickType()) {
                        case LEFT:
                            check.enabled = !check.enabled;
                            data.player.sendMessage(Color.translate("&6&lAntiCunt&e> &f" + check.label + " has been " + (check.enabled ? "&aEnabled" : "&cDisabled")));
                            for (int i = 0; i < data.checkManager.getChecks().size(); i++) {
                                infoPair.getMenu().setItem(i, check(checks.get(i), data));
                            }
                            infoPair.getMenu().buildInventory(false);
                            break;
                        case RIGHT:
                            check.autoban = !check.autoban;
                            data.player.sendMessage(Color.translate("&6&lAntiCunt&e> &fautoban for " + check.label + " has been " + (check.autoban ? "&aEnabled" : "&cDisabled")));
                            data.resetVLs();
                            for (int i = 0; i < data.checkManager.getChecks().size(); i++) {
                                infoPair.getMenu().setItem(i, check(checks.get(i), data));
                            }
                            infoPair.getMenu().buildInventory(false);
                    }
                }));

    }
}
