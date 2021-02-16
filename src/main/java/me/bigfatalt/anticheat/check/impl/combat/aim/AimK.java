package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "Aim K", category = Category.Combat)
@Punishment
public class AimK extends Check {

    private float lastYawDiff;

    public AimK(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 30 || playerData.misc.cinematic || playerData.misc.lastTarget == null) return;

            float yawDiff = Math.abs(playerData.movement.fyaw - playerData.movement.tyaw);

            long offset = 16777216L;
            long gcd = MathUtil.getGcd((long) (yawDiff * offset), (long) (lastYawDiff * offset));

            if (playerData.movement.fyaw != playerData.movement.tyaw && playerData.movement.fpitch != playerData.movement.tyaw) {
                if (Math.abs(playerData.movement.fpitch - playerData.movement.tpitch) > 0.0 && Math.abs(playerData.movement.fpitch) != 90.f) {
                    if (gcd < 131072L) {
                        debug("Yaw+=" + yawDiff + ", GCD+=" + gcd);
                    }
                }
            }

            this.lastYawDiff = yawDiff;
        }
    }
}
