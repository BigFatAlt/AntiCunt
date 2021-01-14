package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.types.BaseBlockPosition;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic C", type = CheckType.COMBAT)
public class MagicC extends Check {

    public MagicC(PlayerData data) {
        super(data);
    }

    private int ticks;
    private long system;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInBlockPlacePacket) {
            if (data.actionProcessor.lagging) return;
            long elapsed = MathUtils.elapsed(system);

            if (elapsed < 15) {
                if (ticks++ > 5) {
                    fail();
                    punish("Scaffold", PunishmentType.BAN);
                }
            } else if (ticks > 0) ticks--;
        }

        if (o instanceof WrappedInFlyingPacket) {
            system = System.currentTimeMillis();
        }
    }
}
