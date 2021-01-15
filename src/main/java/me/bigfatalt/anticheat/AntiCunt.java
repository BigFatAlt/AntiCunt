package me.bigfatalt.anticheat;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.profiling.BaseProfiler;
import com.qrakn.honcho.Honcho;
import lombok.Getter;
import me.bigfatalt.anticheat.api.alert.impl.AlertListener;
import me.bigfatalt.anticheat.api.commands.AntiCuntCommand;
import me.bigfatalt.anticheat.api.punishment.impl.PunishmentListener;
import me.bigfatalt.anticheat.api.settings.ConfigSettings;
import me.bigfatalt.anticheat.data.manager.DataManager;
import me.bigfatalt.anticheat.handler.BukkitHandler;
import me.bigfatalt.anticheat.handler.PacketHandler;
import me.bigfatalt.anticheat.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class AntiCunt extends JavaPlugin {

    public static AntiCunt instance;

    public DataManager dataManager;
    public ConfigSettings configSettings;
    public ConfigUtil configUtil;

    public Honcho honcho;

    public BaseProfiler baseProfiler;

    public Executor packetExecutor = Executors.newSingleThreadExecutor();
    public Executor alertExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onEnable() {
        instance = this;
        dataManager = new DataManager();

        configUtil = new ConfigUtil(this, "config.yml");
        configUtil.saveDefaultConfig();
        configSettings = new ConfigSettings();

        baseProfiler = new BaseProfiler();

        new AntiCuntCommand(this);


        registerListeners();
    }

    private void registerListeners() {
        Listener[] listeners = {new BukkitHandler(this)};
        AtlasListener[] atlasListeners = {new PacketHandler(), new PunishmentListener(), new AlertListener()};

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }

        for (AtlasListener atlasListener : atlasListeners) {
            Atlas.getInstance().getEventManager().registerListeners(atlasListener, this);
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
