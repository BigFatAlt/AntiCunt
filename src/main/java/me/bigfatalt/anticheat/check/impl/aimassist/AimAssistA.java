package me.bigfatalt.anticheat.check.impl.aimassist;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import com.google.common.collect.Lists;
import me.bigfatalt.anticheat.api.punishment.api.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.api.CheckInfo;
import me.bigfatalt.anticheat.check.api.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@CheckInfo(name = "AimAssist A" ,type = CheckType.COMBAT, maxVl = 50)
public class AimAssistA extends Check {

    private final Deque<Float> samples = Lists.newLinkedList();

    public AimAssistA(PlayerData data) {
        super(data);
    }

    @Override
    public void handlePacket(Object o) {
        if (o instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) o;
            if (!flyingPacket.isLook()) return;
            if (data.actionProcessor.hitTicks > 5) return;

            float deltaYaw = data.movementProcessor.deltaYaw;
            float acceleration = data.movementProcessor.yawAcceleration;

            if (deltaYaw > 1.0f) {
                if (acceleration < 0.0001f && data.movementProcessor.lYawAcceleration < 0.1f) {
                    if (vl++ > 3) {
                        fail();
                        punish("Aimbot", PunishmentType.BAN);
                     }
                   } else if (vl > 0) vl--;
                }
            }
        }
}
