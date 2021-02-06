package me.bigfatalt.anticheat;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.EventManager;
import cc.funkemunky.api.profiling.Profiler;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.MiscUtils;
import lombok.Getter;
import me.bigfatalt.anticheat.api.commands.AntiCuntCommand;
import me.bigfatalt.anticheat.api.events.TickEvent;
import me.bigfatalt.anticheat.data.manager.CheckManager;
import me.bigfatalt.anticheat.data.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class AntiCunt extends JavaPlugin {

    @Getter
    public static AntiCunt instance;

    public PlayerDataManager playerDataManager;
    public EventManager eventManager;

    public Profiler profiler;

    public Executor packetExecutor = Executors.newSingleThreadExecutor();
    public Executor alertExecutor = Executors.newSingleThreadExecutor();

    public int ticks;


    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Atlas")) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load AnitCunt. Atlas not found.");
            return;
        }



        MiscUtils.printToConsole("Starting AntiCunt.....");
        long start = System.nanoTime();
        instance = this;

        this.playerDataManager = new PlayerDataManager();
        this.eventManager = Atlas.getInstance().getEventManager();


        registerCommands();
        runTasks();

        Atlas.getInstance().initializeScanner(this, true, true);
        long complete = System.nanoTime() - start;
        MiscUtils.printToConsole("AntiCunt loaded in " + MathUtils.round(complete / 1E6D, 3)  + " ms.");
    }

    private void runTasks() {
        //This allows us to use ticks for time comparisons to allow for more parrallel calculations to actual Minecraft
        //and it also has the added benefit of being lighter than using System.currentTimeMillis.
        new BukkitRunnable() {
            public void run() {
                TickEvent tickEvent = new TickEvent(ticks++);

                eventManager.callEvent(tickEvent);
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
    }

    private void registerCommands() {
        this.getCommand("anticunt").setExecutor(new AntiCuntCommand());

    }

    public void onDisable() {
        MiscUtils.printToConsole("Disabling AniCunt");
        this.playerDataManager.getEntries().clear();
        this.eventManager.clearAllRegistered();
        Atlas.getInstance().getFunkeCommandManager().getCommands().clear();

    }
}
