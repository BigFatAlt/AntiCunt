package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic M", type = CheckType.PACKET)
public class MagicM extends Check {

    private int vl;

    public MagicM(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) o;
            if (useEntityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                if (data.actionProcessor.inventory) {
                    if (vl++ > 2) {
                        fail();
                        punish("Packet", PunishmentType.BAN);
                    }
                } else if (vl > 0) vl--;
            }
        }
    }
}
