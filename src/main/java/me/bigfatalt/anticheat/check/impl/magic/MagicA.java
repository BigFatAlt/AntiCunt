package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

import static me.bigfatalt.anticheat.api.punishment.api.PunishmentType.BAN;

@CheckInfo(name = "Magic A", type = CheckType.PACKET)
public class MagicA extends Check {

    public MagicA(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) o;

            if (Math.abs(flyingPacket.getPitch()) > 90.0f) {
                fail();
                punish("Invalid pitch", BAN);
            }

            debug(Math.abs(flyingPacket.getPitch()) + "");
        }
    }
}
