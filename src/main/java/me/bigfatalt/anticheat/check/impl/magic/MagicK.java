package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInCloseWindowPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic K", type = CheckType.PACKET)
public class MagicK extends Check {

    private long lastFlying;
    private int vl;

    public MagicK(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInBlockDigPacket) {
            long delay = MathUtils.elapsed(lastFlying);

            if (delay < 15) {
                if (vl++ > 2) {
                    fail();
                }
            } else if (vl > 0) vl--;

        }

        if (o instanceof WrappedInFlyingPacket) {
            if (data.actionProcessor.lagging) return;
            lastFlying = System.currentTimeMillis();
        }
    }
}