package me.bigfatalt.anticheat;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.profiling.BaseProfiler;
import cc.funkemunky.api.profiling.Profiler;
import com.qrakn.honcho.Honcho;
import com.qrakn.honcho.command.example.ExampleCommand;
import lombok.Getter;
import me.bigfatalt.anticheat.api.alert.impl.AlertListener;
import me.bigfatalt.anticheat.api.commands.AntiCuntCommand;
import me.bigfatalt.anticheat.api.commands.impl.Alerts;
import me.bigfatalt.anticheat.api.commands.impl.Ban;
import me.bigfatalt.anticheat.api.punishment.impl.PunishmentListener;
import me.bigfatalt.anticheat.data.manager.DataManager;
import me.bigfatalt.anticheat.handler.BukkitHandler;
import me.bigfatalt.anticheat.handler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class AntiCunt extends JavaPlugin {

    public static AntiCunt instance;

    public DataManager dataManager;

    public Honcho honcho;

    public BaseProfiler baseProfiler;

    public Executor packetExecutor = Executors.newSingleThreadExecutor();
    public Executor alertExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onEnable() {
        instance = this;

        dataManager = new DataManager();
        baseProfiler = new BaseProfiler();


        honcho = new Honcho(this);
        honcho.registerCommand(new AntiCuntCommand());
        honcho.registerCommand(new Alerts());
        honcho.registerCommand(new Ban());


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
