package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import lombok.val;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "Aim F", experimental = true)
@Punishment
public class AimF extends Check {

    private double multiplier = Math.pow(2.0, 24.0);
    private float lastPitch = -1;
    private int vl;

    public AimF(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            float acceleration = MathUtils.getDelta(playerData.movement.deltaPitch, playerData.movement.lDeltaPitch);
            val gcd = MathUtils.gcd((long) (playerData.movement.deltaPitch * 16777216L), (long) (playerData.movement.lDeltaPitch * 16777216L));
            val deviation = getDeviation(acceleration);

            if (playerData.movement.deltaYaw > 1.f) {
                if (acceleration < 0.1 && gcd < 131072L && deviation > 2) {
                    if (vl++ > 40) {
                        flag("accel+=" + acceleration + " GCD+=" + gcd + " deviation+=" + deviation);
                    }
                } else if (vl > 0) vl--;

                debug("accle+=" + acceleration + " GCD+=" + gcd + " Deviation+=" + deviation + " Cinematic+=" + playerData.misc.cinematic + " sens+=" + playerData.misc.sensitivity);
            }
        }
    }

    public long getDeviation(float pitchChange) {
        if (lastPitch != -1) {
            try {
                long current = (long) (pitchChange * multiplier);
                long last = (long) (lastPitch * multiplier);
                long value = convert(current, last);

                if (value < 0x20000) {
                    return value;
                }
            } catch (Exception e) {
            }
        }

        lastPitch = pitchChange;
        return -1;
    }

    private long convert(long current, long last) {
        if (last <= 16384) return current;
        return convert(last, current % last);
    }
}
