package me.bigfatalt.anticheat.check.impl.magic;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "Magic L" , type = CheckType.PACKET)
public class MagicL extends Check {

    private float lastYaw, lastPitch;
    private int vl;

    public MagicL(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket) o;
            if (!packet.isLook()) return;
            if (data.actionProcessor.hitTicks > 5) return;

            float yaw = packet.getYaw();
            float pitch = packet.getPitch();

            if (yaw == lastYaw && pitch == lastPitch) {
                if (vl++ > 5) {
                    fail();
                }
            } else if (vl > 0) vl--;

            this.lastYaw = yaw;
            this.lastPitch = pitch;
        }
    }
}
