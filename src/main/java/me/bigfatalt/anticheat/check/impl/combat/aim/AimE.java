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

@CheckType(label = "Aim E", category = Category.Combat)
@Punishment
public class AimE extends Check {

    public final double EXPANDER = Math.pow(2, 24);
    private int vl;
    public double lastStandardDeviation;
    public EvictingList<Float> evictingList = new EvictingList<>(120);

    public AimE(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            if (playerData.movement.deltaYaw > 0.f && playerData.movement.deltaPitch > 0.f && playerData.movement.deltaYaw < 30.f && playerData.movement.deltaPitch < 30.f) {
                evictingList.add(playerData.movement.deltaYaw);
            }

            if (evictingList.size() == evictingList.getMaxSize()) {
                long gcd = MathUtils.gcd((long) (playerData.movement.deltaYaw * EXPANDER), (long) (playerData.movement.lDeltaYaw * EXPANDER));
                double standardDeviation = MathUtil.getStandardDeviation(evictingList);
                double deviation = Math.abs(lastStandardDeviation - standardDeviation);

                if (playerData.movement.deltaYaw > 1.f) {
                    if (gcd < 131072L && deviation < 0.4) {
                        if (vl++ > 10) {
                            flag("GCD+= " + gcd + " Deviation+= " + deviation);
                        }
                    } else if (vl > 0) vl--;
                    debug("GCD+= " + gcd + " Deviation+= " + deviation);
                }

                this.lastStandardDeviation = standardDeviation;
                evictingList.clear();
            }
        }
    }
}
