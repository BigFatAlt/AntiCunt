package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.*;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic E", type = CheckType.PACKET)
public class MagicE extends Check {

    private int lastSlot, vl;

    public MagicE(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInHeldItemSlotPacket) {
            WrappedInHeldItemSlotPacket itemSlotPacket = (WrappedInHeldItemSlotPacket) o;

            int slot = itemSlotPacket.getSlot();

            if (lastSlot == slot) {
                if (vl++ > 3) {
                    fail();
                    punish("Invalid packet", PunishmentType.BAN);
                }
            } else if (vl > 0) vl--;

            this.lastSlot = slot;
        }
    }
}
