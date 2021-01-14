package me.bigfatalt.anticheat.check.impl.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import com.google.common.util.concurrent.AtomicDouble;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker F", autoban = false, type = CheckType.COMBAT)
public class AutoClickerF extends Check {

    private int tick,vl;
    private List<Integer> delays = new ArrayList<>();

    public AutoClickerF(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInArmAnimationPacket) {
            if (data.actionProcessor.digging) return;

            if (tick < 10) {
                delays.add(tick);
            }

            if (delays.size() == 50) {
                double kurtosis = MathUtil.getKurtosis(delays);

                if (kurtosis < 0D) {
                    if (vl++ > 4) {
                        fail();
                    }
                } else if (vl > 0) vl--;

                delays.clear();
            }

            tick = 0;

        }

        if (o instanceof WrappedInFlyingPacket) {
            tick++;
        }
    }
}