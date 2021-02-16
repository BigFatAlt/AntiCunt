package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import lombok.val;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "Aim J", experimental = true, category = Category.Combat)
@Punishment
public class AimJ extends Check {

    public float lastPitch;
    private int vl;

    public AimJ(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 30 || playerData.misc.cinematic) return;

            float pitchDiff = Math.abs(playerData.movement.fpitch - playerData.movement.tpitch);
            long gcd = MathUtil.getGcd((long) (pitchDiff * MathUtil.EXPANDER), (long) (lastPitch * MathUtil.EXPANDER));

            if (playerData.movement.fyaw != playerData.movement.tyaw && playerData.movement.fpitch != playerData.movement.tyaw) {
                if (Math.abs(playerData.movement.fpitch - playerData.movement.tpitch) > 0.0 && Math.abs(playerData.movement.fpitch) != 90.f) {
                    if (gcd < 131072L && pitchDiff < 0.4) {
                        vl++;
                        debug(Color.Green + "Pitch+=" + pitchDiff + ", GCD+=" + gcd + ", VL+=" + vl);
                        if (vl > 30) {
                            flag("GCD+=" + gcd + " pitch+=" + pitchDiff);
                        }
                    } else if (vl > 0) vl--;
                    debug("Pitch+=" + pitchDiff + ", GCD+=" + gcd);
                }
            }
            this.lastPitch = pitchDiff;
        }
    }
}
