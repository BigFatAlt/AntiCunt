package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInHeldItemSlotPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic F", type = CheckType.PACKET)
public class MagicF extends Check {

    private long lastFlying;
    private int vl;

    public MagicF(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInHeldItemSlotPacket) {
            long delay = MathUtils.elapsed(lastFlying);

            if (delay < 15) {
                if (vl++ > 3) {
                    fail();
                    punish("Invalid Packet", PunishmentType.BAN);
                }
            } else if (vl > 0) vl--;
        }

        if (o instanceof WrappedInFlyingPacket) {
            if (data.actionProcessor.lagging) return;
            lastFlying = System.currentTimeMillis();
        }
    }
}
