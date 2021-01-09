package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "KillAura B", description = "checks if the player sends more attack packets then swing packets", autoban = true, enabled = true, type = CheckType.COMBAT, maxVl = 20)
public class KillauraB extends Check {

    public KillauraB(PlayerData data) {
        super(data);
    }


    private int swings, attacks, vl;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) o;

            if (entityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                attacks++;
            }
        }

        if (o instanceof WrappedInArmAnimationPacket) {
            swings++;
        }

        if (o instanceof WrappedInFlyingPacket) {
            if (attacks > swings) {
                if (vl++ > 5) {
                    fail();
                }
                attacks = swings = 0;
            } else if (vl > 0) vl--;
        }
    }
}
