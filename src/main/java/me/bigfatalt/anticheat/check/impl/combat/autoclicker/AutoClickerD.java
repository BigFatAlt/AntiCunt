package me.bigfatalt.anticheat.check.impl.combat.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

@Punishment
@CheckType(label = "AutoClicker D", category = Category.Combat)
public class AutoClickerD extends Check {

    private int movements, vl;
    private double lastDeviation;
    private final List<Long> delays = new ArrayList<>();

    public AutoClickerD(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            if (playerData.misc.digging) return;
            if (movements < 5) {
                delays.add((long) (movements * 50.0));

                if (delays.size() == 20) {
                    double deviation = MathUtil.getStandardDeviation(delays);
                    double acceleration = Math.abs(deviation - lastDeviation);
                    double cps = MathUtil.getCps(delays);


                    if (acceleration < 5.0) {
                        if (vl++ > 20) {
                            flag("accleration+= "+ acceleration + " CPS+= " + playerData.misc.cps);
                        }
                    } else if (vl > 0) vl--;

                    lastDeviation = deviation;
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
