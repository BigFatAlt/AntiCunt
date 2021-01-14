package me.bigfatalt.anticheat.check.impl.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import com.google.common.util.concurrent.AtomicDouble;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "AutoClicker E", autoban = false, type = CheckType.COMBAT)
public class AutoClickerE extends Check {

    private double lastDeviation;
    private int tick,vl;
    private List<Integer> delays = new ArrayList<>();

    public AutoClickerE(PlayerData data) {
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
                double averageDelay = delays.stream()
                        .mapToDouble(Integer::doubleValue)
                        .average().orElse(0.0);

                AtomicDouble variance = new AtomicDouble(0.0);
                delays.forEach(recent -> variance.getAndAdd(Math.pow(recent - averageDelay, 2.0)));
                double deviation = Math.sqrt(variance.get());

                if (deviation < 0.045 && lastDeviation < 0.045) {
                    if (vl++ > 2) {
                        fail();
                    }
                } else if (vl > 0) vl--;


                lastDeviation = deviation;
                delays.clear();
            }

            tick = 0;

        }

        if (o instanceof WrappedInFlyingPacket) {
            tick++;
        }
    }
}