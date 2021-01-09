package me.bigfatalt.anticheat.check.impl.aimassist;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckInfo(name = "AimAssist A", description = "", type = CheckType.COMBAT, autoban = true, enabled = true, maxVl = 20)
public class AimAssistA extends Check {

    public AimAssistA(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) o;
            if (!flyingPacket.isLook()) return;
            if (data.actionProcessor.hitTicks > 5) return;

            float yaw = Math.abs(data.movementProcessor.lDeltaYaw - data.movementProcessor.deltaYaw);
            float acceleration = Math.abs(data.movementProcessor.lYawAcceleration - data.movementProcessor.yawAcceleration);

            if (yaw == acceleration) {
                data.player.sendMessage("Flag");
            }


            data.player.sendMessage(yaw + ", " + acceleration + "");
        }
    }
}
