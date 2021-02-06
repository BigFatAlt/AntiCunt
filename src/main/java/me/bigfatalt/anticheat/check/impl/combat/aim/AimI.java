package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import lombok.val;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "Aim I", experimental = true)
@Punishment
public class AimI extends Check {

    private double multiplier = Math.pow(2.0, 24.0);
    private float lastPitch = -1;
    private int vl;

    public AimI(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 30) return;

            float delta = playerData.movement.deltaPitch;
            val gcd = MathUtils.gcd((long) (playerData.movement.deltaPitch * 16777216L), (long) (playerData.movement.lDeltaPitch * 16777216L));
            val deviation = getDeviation(delta);

            if (playerData.movement.deltaYaw > 1.f) {
                if (delta < 0.2f && gcd < 131072L && deviation > 2) {
                    vl++;
                    debug(Color.Green + "delta+=" + delta + " GCD+=" + gcd + " Deviation+=" + deviation + " Vl+=" + vl);
                    if (vl > 30) {
                        flag("delta+=" + delta + " GCD+=" + gcd + " deviation+=" + deviation);
                    }
                } else if (vl > 0) vl--;

                debug("delta+=" + delta + " GCD+=" + gcd + " Deviation+=" + deviation);
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