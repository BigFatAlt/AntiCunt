package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;
import org.bukkit.entity.Player;

/*
 * I saw this check somewhere i cant remember where but credit goes to the dev cause i was working on something like this.
 */

@CheckInfo(name = "KillAura D", type = CheckType.COMBAT)
public class KillAuraD extends Check {

    private double lastHorizontal;
    private int vl;

    public KillAuraD(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) o;

            if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if (entityPacket.getEntity() instanceof Player) {

                    double deltaX = Math.abs(data.movementProcessor.fx - data.movementProcessor.tx);
                    double deltaZ = Math.abs(data.movementProcessor.fz - data.movementProcessor.tz);
                    double deltaXZ = MathUtil.magnitude(deltaX, deltaZ);

                    if (deltaXZ > 0.0) {
                        double acceleration = Math.abs(deltaXZ - lastHorizontal);

                        if (acceleration < 1e-02 && data.movementProcessor.deltaYaw > 30.f && data.movementProcessor.deltaPitch > 15.f) {
                            if (vl++ > 3) {
                                fail();
                            }
                        } else if (vl > 0) vl--;
                    }

                    lastHorizontal = deltaXZ;

                }
            }
        }
    }
}
