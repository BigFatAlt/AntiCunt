package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "KillAura A", description = "Checks for attack packets being sent on post", autoban = true, enabled = true, type = CheckType.COMBAT, maxVl = 20)
public class KillauraA extends Check {

    public KillauraA(PlayerData data) {
        super(data);
    }

    private long lastFlying;
    private int vl;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) o;

            if (entityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                long delay = MathUtils.elapsed(lastFlying);

                if (data.actionProcessor.lagging) return;

                if (delay < 15) {
                   if (vl++ > 5) {
                       fail();
                   }
                } else if (vl > 0) vl--;
            }
        }

        if (o instanceof WrappedInFlyingPacket) {
            lastFlying = System.currentTimeMillis();
        }
    }
}
