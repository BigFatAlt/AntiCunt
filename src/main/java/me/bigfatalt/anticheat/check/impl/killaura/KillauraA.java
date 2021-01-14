package me.bigfatalt.anticheat.check.impl.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "KillAura A", type = CheckType.COMBAT)
public class KillauraA extends Check {

    public KillauraA(PlayerData data) {
        super(data);
    }

    private long lastFlying;
    private int vl;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket entityPacket = (WrappedInUseEntityPacket) o;

            if (entityPacket.getAction().equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                long delay = MathUtils.elapsed(lastFlying);

                if (data.actionProcessor.lagging) return;

                if (delay < 15) {
                   if (vl++ > 5) {
                       fail();
                       punish("KillAura", PunishmentType.BAN);
                   }
                } else if (vl > 0) vl--;
            }
        }

        if (o instanceof WrappedInFlyingPacket) {
            lastFlying = System.currentTimeMillis();
        }
    }
}
