package me.bigfatalt.anticheat.check.impl.other.badpackets;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "BadPackets B")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class BadPacketsB extends Check {

    private int ticks, vl;

    public BadPacketsB(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;

            if (flyingPacket.isPos() || playerData.player.isInsideVehicle() || playerData.misc.lagging) {
                ticks = 0;
            }

            if (ticks++ > 20) {
                if(vl++ > 5) {

                }
            } else if (vl > 0) vl--;
        }
    }
}
