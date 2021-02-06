package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "Aim C", experimental = true)
@Punishment
public class AimC extends Check {

    public double lastAcceleration, lastDelta;
    private int vl;

    public AimC(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            double acceleration = MathUtils.getDelta(playerData.movement.deltaYaw, playerData.movement.lDeltaYaw);
            double delta = MathUtils.getDelta(lastAcceleration, acceleration);
            double gcd = MathUtil.getGcd(delta, lastDelta);

            if (playerData.movement.deltaYaw > 1.0f) {
                if (delta < 0.001 && gcd < 0.003) {
                    if (vl++ > 50) {
                        flag("Delta+=" + delta + " GCD+=" + gcd);
                    }
                } else if (vl > 0) vl--;
            }

            this.lastDelta = delta;
            this.lastAcceleration = acceleration;
        }
    }
}
