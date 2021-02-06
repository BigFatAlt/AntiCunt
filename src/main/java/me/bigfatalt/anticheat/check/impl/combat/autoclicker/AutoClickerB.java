package me.bigfatalt.anticheat.check.impl.combat.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckType(label = "AutoClicker B")
@Punishment
public class AutoClickerB extends Check {

    private int movements, vl;
    protected final List<Integer> delays = new ArrayList<>();

    public AutoClickerB(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == 50) {
                    double deviation = MathUtil.getStandardDeviation(delays);
                    double cps = MathUtil.getCps(delays);

                    if (deviation < 0.045) {
                        if (vl++ > 3) {
                            flag("STD: " + deviation + " CPS: " + cps);
                        }
                    } else if (vl > 0) vl--;

                    delays.clear();
                }

            }
            movements = 0;
        }

        if (object instanceof WrappedInFlyingPacket) {
            movements++;
        }

    }
}
