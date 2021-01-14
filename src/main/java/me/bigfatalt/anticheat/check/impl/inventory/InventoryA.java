package me.bigfatalt.anticheat.check.impl.inventory;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Inventory A", type = CheckType.OTHER)
public class InventoryA extends Check {

    private boolean clicked;

    public InventoryA(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInWindowClickPacket) {
            clicked = true;
        }

        if (o instanceof WrappedInFlyingPacket) {
            if (clicked && !data.actionProcessor.inventory) {
                fail();
            }
            clicked = false;
        }
    }
}
