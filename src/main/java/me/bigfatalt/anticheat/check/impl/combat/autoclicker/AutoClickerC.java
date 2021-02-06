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

@CheckType(label = "AutoClicker C")
@Punishment
public class AutoClickerC extends Check {

    private int movements, vl;
    protected final List<Long> delays = new ArrayList<>();

    public AutoClickerC(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            if (movements < 5) {
                delays.add((long) (movements * 50.0));

                if (delays.size() == 20) {
                    double deviation = MathUtil.getStandardDeviation(delays);
                    double cps = MathUtil.getCps(delays);

                    if (deviation < 5.0) {
                        if (vl++ > 20) {
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
