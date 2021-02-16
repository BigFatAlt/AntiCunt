package me.bigfatalt.anticheat.check.impl.movement.fly;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.Color;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;

import java.awt.*;

@CheckType(label = "Fly B", category = Category.Movement)
@Punishment
public class FlyB extends Check {

    private double lDeltaY;
    private int vl;

    public FlyB(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (flyingPacket.isPos()) {
                double deltaY = playerData.movement.fy - playerData.movement.ty;
                double predicted = (lDeltaY - 0.08) * 0.9800000190734863;

                if (playerData.movement.airTicks > 5 && playerData.movement.fallDistance == 0.0) {
                    if (Math.abs(deltaY - predicted) > 0.2) {
                        if (vl++ > 3) {
                            flag("delta+=" + Math.abs(deltaY - predicted));
                        }
                        debug(Color.Green + Math.abs(deltaY - predicted));
                    } else if (vl > 0) vl--;

                    debug("delta=" + Math.abs(deltaY - predicted));
                }

                this.lDeltaY = deltaY;
            }
        }
    }
}
