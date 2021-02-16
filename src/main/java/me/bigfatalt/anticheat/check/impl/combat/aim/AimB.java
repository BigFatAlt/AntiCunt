package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import lombok.val;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "Aim B", experimental = true, category = Category.Combat)
@Punishment
public class AimB extends Check {

    public double lastAcceleration;
    private int vl;

    public AimB(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            double acceleration = MathUtils.getDelta(playerData.movement.deltaPitch, playerData.movement.lDeltaPitch);
            val gcd = MathUtils.gcd((long) (playerData.movement.deltaPitch * 16777216L), (long) (playerData.movement.lDeltaPitch * 16777216L));

            if (playerData.movement.deltaPitch > 1.0f) {
                if (gcd < 131072L && acceleration < 0.06) {
                    if (vl++ > 15) {
                        flag("GCD+= " + gcd + " accel+= " + acceleration);
                    }
                } else if (vl++ > 0) vl--;
            }

            this.lastAcceleration = acceleration;
        }
    }
}
