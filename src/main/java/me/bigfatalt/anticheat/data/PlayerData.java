package me.bigfatalt.anticheat.data;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.tinyprotocol.api.TinyProtocolHandler;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.impl.aimassist.AimAssistA;
import me.bigfatalt.anticheat.check.impl.autoclicker.*;
import me.bigfatalt.anticheat.check.impl.inventory.InventoryA;
import me.bigfatalt.anticheat.check.impl.inventory.InventoryB;
import me.bigfatalt.anticheat.check.impl.inventory.InventoryC;
import me.bigfatalt.anticheat.check.impl.killaura.KillAuraD;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraA;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraB;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraC;
import me.bigfatalt.anticheat.check.impl.magic.*;
import me.bigfatalt.anticheat.data.info.ActionProcessor;
import me.bigfatalt.anticheat.data.info.MovementProcessor;
import me.bigfatalt.anticheat.parser.PacketParser;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {

    public Player player;

    public ProtocolVersion protocolVersion;

    public UUID debuggingPlayer;
    public Check debuggingCheck;

    public MovementProcessor movementProcessor;
    public ActionProcessor actionProcessor;

    public PacketParser packetParser;

    public Set<Check> checkSet = new HashSet<>();

    public boolean alerts;

    public PlayerData(Player player) {
        this.player = player;
        this.protocolVersion = ProtocolVersion.getVersion(TinyProtocolHandler.getProtocolVersion(player).getVersion());

        this.movementProcessor = new MovementProcessor();
        this.actionProcessor = new ActionProcessor();
        this.packetParser = new PacketParser();


        if (player.hasPermission("loki.alerts")) alerts = true;

        initializeChecks();
    }

    private void addCheck(Check check) {
        checkSet.add(check);
    }

    private void addChecks(Check... checks) {
        for (Check check : checks) {
            addCheck(check);
        }
    }

    private void initializeChecks() {
        addChecks(new AimAssistA(this) );

        addChecks(new AutoClickerA(this), new AutoClickerB(this), new AutoClickerC(this),
                  new AutoClickerD(this), new AutoClickerE(this), new AutoClickerF(this));

        addChecks(new InventoryA(this), new InventoryB(this), new InventoryC(this));

        addChecks(new KillauraA(this), new KillauraB(this), new KillauraC(this), new KillAuraD(this));

        addChecks(
                new MagicA(this), new MagicB(this), new MagicC(this),
                new MagicD(this), new MagicE(this), new MagicF(this),
                new MagicG(this), new MagicH(this), new MagicI(this),
                new MagicJ(this), new MagicK(this), new MagicL(this),
                new MagicL(this), new MagicM(this), new MagicN(this));
    }

    public Check getCheck(String name) {
        return checkSet.stream().filter(check -> check.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }


    public void fireChecks(Object packet) {
        checkSet.parallelStream().forEach(check -> check.handlePacket(packet));
    }

}
