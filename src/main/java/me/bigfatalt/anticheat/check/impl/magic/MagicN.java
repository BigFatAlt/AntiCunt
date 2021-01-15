package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInSteerVehiclePacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic N", type = CheckType.PACKET)
public class MagicN extends Check {

    private boolean sent;
    private int vl;

    public MagicN(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInSteerVehiclePacket) {
            sent = true;
        }

        if (o instanceof WrappedInFlyingPacket) {
            if (!data.player.isInsideVehicle()) {
                if (sent) {
                   if (vl++ > 2) {
                       fail();
                   }
                } else if (vl > 0) vl--;
            }
        }
    }
}
