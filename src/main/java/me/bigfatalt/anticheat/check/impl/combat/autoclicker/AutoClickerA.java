package me.bigfatalt.anticheat.check.impl.combat.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "AutoClicker A")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class AutoClickerA extends Check {
    private long start;
    private int cps;

    public AutoClickerA(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            if (playerData.misc.digging) return;
            if (timeStamp - start > 1000L) {
                if (cps > 20) {
                    flag("CPS: " + cps);
                    punish("AutoClicker A");
                }
                cps = 0;
                start = timeStamp;
            } else cps++;
        }
    }
}
