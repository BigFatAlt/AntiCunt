package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "KillAura C", type = CheckType.COMBAT)
public class KillauraC extends Check {

    public KillauraC(PlayerData data) {
        super(data);
    }


    private int attacks, vl;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) o;

            if (entityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                if (attacks++ > 2) {
                    if (vl++ > 3) {
                        fail();
                    }
                } else if (vl > 0) vl--;
            }
        }

        if (o instanceof WrappedInArmAnimationPacket) {
            attacks = 0;
        }
    }
}
