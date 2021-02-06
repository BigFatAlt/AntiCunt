package me.bigfatalt.anticheat.check.impl.other.badpackets;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "BadPackets A")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class BadPacketsA extends Check {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;

            if (Math.abs(flyingPacket.getPitch()) > 90.f) {
                punish("InvalidPackets");
            }
        }
    }
}
