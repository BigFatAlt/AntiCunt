package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic G", type = CheckType.PACKET)
public class MagicG extends Check {

    private long lastFlying;
    private int vl;

    public MagicG(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            if (data.actionProcessor.lagging) return;
            lastFlying = System.currentTimeMillis();
        }

        if (o instanceof WrappedInArmAnimationPacket) {
            long lastArm = MathUtils.elapsed(lastFlying);

            if (lastArm < 15) {
                if (vl++ > 3) {
                    fail();
                }
            } else if (vl > 0) vl--;

        }
    }
}
