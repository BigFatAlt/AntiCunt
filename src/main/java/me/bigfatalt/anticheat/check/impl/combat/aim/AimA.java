package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "Aim A", category = Category.Combat)
@Punishment
public class AimA extends Check {

    private double lastDelta;
    private int vl;

    public AimA(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            double delta = MathUtils.getDelta(playerData.movement.deltaYaw, playerData.movement.lDeltaYaw);
            double acceleration = MathUtils.getDelta(delta, lastDelta);

            if (playerData.movement.deltaYaw > 1.0f) {
                if (acceleration % 1 == 0 && delta % 1 == 0) {
                    if (vl++ > 20) {
                        flag("accle: " + acceleration + " delta: " + delta);
                    }
                } else if (vl > 0) vl--;


                debug("Delta+= " + delta + " Accel+= " + acceleration);
                this.lastDelta = delta;
            }
        }
    }
}
