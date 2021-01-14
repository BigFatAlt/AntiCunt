package me.bigfatalt.anticheat.check.impl.inventory;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Inventory B", type = CheckType.OTHER, maxVl = 5)
public class InventoryB extends Check {

    private int invTicks,vl;

    public InventoryB(PlayerData data) {
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

                if (deltaXZ > 0.250) {
                    vl++;
                    if (vl > 3) {
                        fail();
                        punish("Inventory", PunishmentType.BAN);
                    }
                } else if (vl > 0) vl--;

                invTicks = 0;
            }
        }
    }
}
