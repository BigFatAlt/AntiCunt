package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;



/*
 * This was taken form a open sourced project called Frequency
 * Link to it below and full credit to who ever made it
 * https://github.com/ElevatedDev/Frequency/blob/master/src/main/java/xyz/elevated/frequency/check/impl/killaura/KillAuraE.java
 */


@CheckInfo(name = "KillAura B", type = CheckType.COMBAT)
public class KillauraB extends Check {

    private int movements, lastMovements, invalid, total, vl;

    public KillauraB(PlayerData data) {
        super(data);
    }
    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) o;
            if (useEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                final boolean proper = movements < 4 && lastMovements < 4;

                if (proper) {
                    final boolean flag = movements == lastMovements;

                    if (flag) {
                        ++invalid;
                    }

                    if (++total == 25) {

                        if (invalid > 20) {
                            if (vl++ > 3) {
                                fail();
                            }
                        } else if (vl > 0) vl--;

                        total = 0;
                    }
                }

                lastMovements = movements;
                movements = 0;
            }
        }

        if (o instanceof WrappedInFlyingPacket) {
            movements++;
        }
    }
}
