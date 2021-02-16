package me.bigfatalt.anticheat.data;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.tinyprotocol.api.TinyProtocolHandler;
import cc.funkemunky.api.utils.TickTimer;
import cc.funkemunky.api.utils.math.MCSmooth;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import com.google.common.collect.Lists;
import javafx.util.Pair;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.manager.CheckManager;
import me.bigfatalt.anticheat.parsers.MiscParser;
import me.bigfatalt.anticheat.parsers.MovementParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {

    public Player player;

    public UUID debuggingPlayer, UUID;
    public Check debuggingCheck;

    public ProtocolVersion protocolVersion;
    public CheckManager checkManager;

    public MovementParser movementParser;
    public MiscParser miscParser;

    public Movement movement;
    public Misc misc;

    public boolean devServer, alertsEnabled, devAlerts;

    public PlayerData(UUID uuid) {
        this.UUID = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.protocolVersion = ProtocolVersion.getVersion(TinyProtocolHandler.getProtocolVersion(player).getVersion());


        this.movementParser = new MovementParser();
        this.miscParser = new MiscParser();

        this.movement = new Movement();
        this.misc = new Misc();

        this.checkManager = new CheckManager(this);


        if (player.hasPermission("AntiCunt.alert")) alertsEnabled = true;
        if (player.hasPermission("AntiCunt.developer")) devAlerts = true;

    }


    public void fireCheck(Object object, long timeStamp) {
        AntiCunt.instance.packetExecutor
                .execute(() -> checkManager.getChecks().stream()
                        .filter(check -> check.enabled)
                        .forEach(check -> check.handleCheck(object, timeStamp)));
    }

    public void resetVLs() {
        checkManager.getChecks().forEach(check -> check.vl = 0);
    }


    public class Movement {
        public double fx, fy, fz; // Last location
        public double tx, ty, tz; // Current location
        public float fyaw, fpitch, tyaw, tpitch;

        public MCSmooth yawSmooth = new MCSmooth(), pitchSmooth = new MCSmooth();

        public double deltaH, deltaV, deltaX, deltaZ, deltaY, lDeltaX, lDeltaZ, lDeltaY, lDeltaV, lDeltaH, fallDistance;
        public float deltaYaw, lDeltaYaw, deltaPitch, lDeltaPitch, cinematicYawDelta, cinematicPitchDelta;
        public int airTicks, groundTicks;


    }

    public class Misc {
        public final EvictingList<Long> flyingSamples = new EvictingList<>(50);
        public final EvictingList<Pair<Location, Integer>> targetLocations = new EvictingList<>(40);
        public final ArrayDeque<Integer> sensitivitySamples = new ArrayDeque<>();


        public LivingEntity lastTarget;

        public long lastFlying;
        public TickTimer
                lastSlime = new TickTimer(20), lastWeirdBlocks = new TickTimer(10), lastWater = new TickTimer(20),lastClimeable = new TickTimer(20);

        public boolean inLiquid, onSlime, onClimbable;
        public boolean digging, sprint, serverGround, clientGround,lServerGround, inventory, lagging, cinematic;
        public int hitTicks, sensitivity , cinimaticTicks, lastCinimaticTicks, cps;
        public long lastKeepAlive, ping;

        public double finalSensitivity;


    }



}
