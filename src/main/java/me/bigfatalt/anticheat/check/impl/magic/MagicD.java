package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic D", maxVl = 30, type = CheckType.COMBAT)
public class MagicD extends Check {

    public MagicD(PlayerData data) {
        super(data);
    }

    private int ticks, vl;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInBlockPlacePacket) {
            ticks++;
        }

        if (o instanceof WrappedInFlyingPacket) {
            ticks = 0;
        }

        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) o;
            if (data.actionProcessor.lagging) return;

            if (useEntityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                if (ticks >= 1) {
                    if (vl++ > 3) {
                        fail();
                        punish("Invalid packet", PunishmentType.BAN);
                    }
                } else if (vl > 0) vl--;
            }
        }
    }
}
