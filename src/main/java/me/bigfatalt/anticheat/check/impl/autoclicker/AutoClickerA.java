package me.bigfatalt.anticheat.check.impl.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "AutoClicker A", description = "Checks for players clicking 20+ cps", enabled = true, autoban = false, maxVl = 20, type = CheckType.COMBAT)
public class AutoClickerA extends Check {

    public AutoClickerA(PlayerData data) {
        super(data);
    }

    private long start;
    private int cps;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInArmAnimationPacket) {
            if (data.actionProcessor.digging) return;
            if (data.actionProcessor.timeStamp - start > 1000L) {
                if (cps > 20) {
                    fail();
                }
                cps = 0;
                start = data.actionProcessor.timeStamp;
            } else cps++;
        }
    }
}
