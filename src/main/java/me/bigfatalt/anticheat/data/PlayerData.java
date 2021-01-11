package me.bigfatalt.anticheat.data;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.tinyprotocol.api.TinyProtocolHandler;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.impl.aimassist.AimAssistA;
import me.bigfatalt.anticheat.check.impl.autoclicker.AutoClickerA;
import me.bigfatalt.anticheat.check.impl.autoclicker.AutoClickerB;
import me.bigfatalt.anticheat.check.impl.autoclicker.AutoClickerC;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraA;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraB;
import me.bigfatalt.anticheat.check.impl.killaura.KillauraC;
import me.bigfatalt.anticheat.check.impl.magic.MagicA;
import me.bigfatalt.anticheat.check.impl.magic.MagicB;
import me.bigfatalt.anticheat.check.impl.magic.MagicC;
import me.bigfatalt.anticheat.data.info.ActionProcessor;
import me.bigfatalt.anticheat.data.info.MovementProcessor;
import me.bigfatalt.anticheat.parser.PacketParser;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {

    public Player player;

    public ProtocolVersion protocolVersion;


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
        addChecks(new AimAssistA(this));
        addChecks(new AutoClickerA(this), new AutoClickerB(this), new AutoClickerC(this));

        addChecks(new KillauraA(this), new KillauraB(this), new KillauraC(this));

        addChecks(new MagicA(this), new MagicB(this), new MagicC(this));

    }


    public void fireChecks(Object packet) {
        checkSet.parallelStream().forEach(check -> check.handlePacket(packet));
    }

}
