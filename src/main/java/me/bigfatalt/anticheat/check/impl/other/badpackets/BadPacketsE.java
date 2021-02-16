package me.bigfatalt.anticheat.check.impl.other.badpackets;

import cc.funkemunky.api.tinyprotocol.packet.in.*;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "BadPackets E", category = Category.Other)
@Punishment
public class BadPacketsE extends Check {

    private long lastFlying;

    public BadPacketsE(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            long lastArm = System.currentTimeMillis() - lastFlying;

            debug("lastArm+= " + lastArm);

        }

        if (object instanceof WrappedInFlyingPacket) {
            lastFlying = System.currentTimeMillis();

        }
    }
}
