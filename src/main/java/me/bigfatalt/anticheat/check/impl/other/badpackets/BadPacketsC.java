package me.bigfatalt.anticheat.check.impl.other.badpackets;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "BadPackets C")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class BadPacketsC extends Check {

    public long lastFlying;
    private int vl;

    public BadPacketsC(PlayerData data) {
        super(data);
    }


    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInBlockPlacePacket) {
            long lastDig = MathUtils.elapsed(lastFlying);
            if (lastDig < 20) {
                if (vl++ > 4) {
                    flag("post: " + lastDig);
                }
            } else if (vl > 0) vl--;
        }

        if(object instanceof WrappedInFlyingPacket) {
            lastFlying = System.currentTimeMillis();
        }
    }
}
