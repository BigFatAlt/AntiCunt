package me.bigfatalt.anticheat.check.impl.combat.aim;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "Aim D")
@Punishment
public class AimD extends Check {

    private EvictingList<Float> evictingList = new EvictingList<>(120);
    private int vl;

    public AimD(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (!flyingPacket.isLook() || playerData.misc.hitTicks > 5 || playerData.misc.cinimaticTicks > 10) return;

            if (playerData.movement.deltaYaw > 0.f && playerData.movement.deltaPitch > 0.f && playerData.movement.deltaYaw < 30.f && playerData.movement.deltaPitch < 30.f) {
                evictingList.add(playerData.movement.deltaPitch);
            }

            if (evictingList.size() == evictingList.getMaxSize()) {
                int distinct = MathUtil.getDistinct(evictingList);
                int duplicates = evictingList.size() - distinct;
                double average = MathUtil.getAverage(evictingList);

                if (duplicates <= 9 && average <= 30.f) {
                    if (vl++ > 5) {
                        flag("Duplicates+= " + duplicates + " average+= " + average);
                    }
                } else if (vl > 0) vl--;
                evictingList.clear();
            }
        }
    }
}
