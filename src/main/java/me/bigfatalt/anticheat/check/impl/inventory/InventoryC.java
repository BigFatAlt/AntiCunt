package me.bigfatalt.anticheat.check.impl.inventory;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Inventory C", type = CheckType.OTHER)
public class InventoryC extends Check {

    private int invTicks,vl;
    private boolean clicked;

    public InventoryC(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            if (data.actionProcessor.inventory) {
                invTicks++;
            } else invTicks = 0;

            if (invTicks > 20) {
                double deltaX = Math.abs(data.movementProcessor.fx - data.movementProcessor.tx);
                double deltaZ = Math.abs(data.movementProcessor.fz - data.movementProcessor.tz);
                double deltaXZ = Math.abs(deltaX - deltaZ);

                if (clicked) {
                    if (deltaXZ > 0.097) {
                        if (vl++ > 2) {
                            fail();
                        }
                    } else if (vl > 0) vl--;
                }

                invTicks = 0;
            }
        }

        if (o instanceof WrappedInWindowClickPacket) {
            clicked = true;
        }

    }
}
