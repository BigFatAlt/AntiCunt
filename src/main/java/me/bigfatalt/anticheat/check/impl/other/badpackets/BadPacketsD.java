package me.bigfatalt.anticheat.check.impl.other.badpackets;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInHeldItemSlotPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "BadPackets D")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class BadPacketsD extends Check {

    private int lastSlot, vl;

    public BadPacketsD(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInHeldItemSlotPacket) {
            WrappedInHeldItemSlotPacket itemSlotPacket = (WrappedInHeldItemSlotPacket) object;
            if (this.lastSlot == itemSlotPacket.getSlot()) {
                if (vl++ > 2) {
                    flag("slot: " + itemSlotPacket.getSlot() + " last: " + this.lastSlot);
                }
            } else if (vl > 0) vl--;

            this.lastSlot = itemSlotPacket.getSlot();
        }
    }
}
