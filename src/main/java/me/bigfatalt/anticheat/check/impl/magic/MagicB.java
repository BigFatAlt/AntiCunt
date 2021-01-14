package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

import static me.bigfatalt.anticheat.api.punishment.api.PunishmentType.BAN;

@CheckInfo(name = "Magic B", type = CheckType.PACKET, maxVl = 5)
public class MagicB extends Check {

    public MagicB(PlayerData data) {
        super(data);
    }

    private int ticks;

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) o;

            if (data.player.isInsideVehicle() || flyingPacket.isPos()) {
                ticks = 0;
                return;
            }



            if (ticks++ > 20) {
                fail();
                punish("Invalid Packets", BAN);
            }
        }
    }
}
