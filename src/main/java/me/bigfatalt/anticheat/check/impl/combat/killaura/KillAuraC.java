package me.bigfatalt.anticheat.check.impl.combat.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "KillAura C")
@Punishment
public class KillAuraC extends Check {

    private int movements, vl;
    protected final EvictingList<Integer> delays = new EvictingList<>(120);

    public KillAuraC(PlayerData data) {
        super(data);
    }


    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) object;

            if (useEntityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                if (movements >= 5) {
                    delays.add(movements);
                    if (delays.size() == delays.getMaxSize()) {
                        double standardDeviation = MathUtil.getStandardDeviation(delays);
                        double average = MathUtil.getAverage(delays);
                        if (standardDeviation < 0.3) {
                            if (vl++ > 10) {
                                flag("Deviation+= " + standardDeviation);
                            }
                        } else if (vl > 0) vl--;
                        delays.clear();
                    }

                    movements = 0;
                }
            }
        }

        if (object instanceof WrappedInFlyingPacket) {
            movements++;
        }
    }
}
