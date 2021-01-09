package me.bigfatalt.anticheat.check.impl.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker B", description = "checks the deviation of a clicker", enabled = true, autoban = false, maxVl = 20, type = CheckType.COMBAT)
public class AutoClickerB extends Check {

    public AutoClickerB(PlayerData data) {
        super(data);
    }

    protected final List<Integer> delays = new ArrayList<>();
    private int tick, vl;


    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInArmAnimationPacket) {
            if (tick < 10) {
                delays.add(tick);

                if (delays.size() == 100) {
                    double deviation = MathUtil.getStandardDeviation(delays);

                    if (deviation < 0.45) {
                        if (vl++ > 2) {
                            fail();
                        }
                    } else if (vl < 0) vl--;
                    delays.clear();
                }

            }

            tick = 0;
        }

        if (o instanceof WrappedInFlyingPacket) {
            tick++;
        }
    }
}