package me.bigfatalt.anticheat.check.impl.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker C*", autoban = false, type = CheckType.COMBAT)
public class AutoClickerC extends Check {

    public AutoClickerC(PlayerData data) {
        super(data);
    }

    protected final List<Integer> delays = new ArrayList<>();
    private int tick, vl;
    private double lastDeviation;


    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInArmAnimationPacket) {
            if (data.actionProcessor.digging) return;
            if (tick < 10) {
                delays.add(tick);

                if (delays.size() == 20) {
                    double deviation = MathUtil.getStandardDeviation(delays);

                    if (Math.abs(deviation - lastDeviation) < 0.05) {
                        if (vl++ > 5) {
                            fail();
                        }
                    } else if (vl > 0) vl--;

                    delays.clear();
                    this.lastDeviation = deviation;
                }

            }

            tick = 0;
        }

        if (o instanceof WrappedInFlyingPacket) {
            tick++;
        }
    }
}