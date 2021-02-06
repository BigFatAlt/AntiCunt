package me.bigfatalt.anticheat.check.impl.combat.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "KillAura B")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class KillAuraB extends Check {

    public boolean sent;
    public int vl;

    public KillAuraB(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) object;

            if (entityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                if (sent) {
                    if (vl++ > 3) {
                        flag("sentDig+= " + sent);
                    }
                } else if (vl > 0) vl--;

            }
        }

        if (object instanceof WrappedInBlockDigPacket) {
            sent = true;
        }

        if (object instanceof WrappedInFlyingPacket) {
            sent = false;
        }
    }
}
