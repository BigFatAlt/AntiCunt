package me.bigfatalt.anticheat.check.impl.movement.fly;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.BlockUtils;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.PlayerUtils;
import cc.funkemunky.api.utils.ReflectionsUtil;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.GameMode;

@CheckType(label = "Fly A", experimental = true)
@Punishment
public class FlyA extends Check {

    private int vl;
    private double pDeltaY;

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket flyingPacket = (WrappedInFlyingPacket) object;
            if (flyingPacket.isLook()) return;

            double deltaY = playerData.movement.deltaY;
            double lastDeltaY = playerData.movement.lDeltaY;

            if (Math.abs(pDeltaY) < 0.005) {
                this.pDeltaY = 0;
            }

            if (playerData.misc.onSlime && playerData.movement.fallDistance > 0) {
                this.pDeltaY = -this.pDeltaY;
            }

            if (!playerData.misc.serverGround) {
                this.pDeltaY -= 0.008;
                this.pDeltaY *= 0.9800000190734863D;
            } else this.pDeltaY = 0.f;

            if (playerData.movement.fallDistance == 0 && playerData.movement.airTicks == 1) {
                this.pDeltaY = PlayerUtils.getJumpHeight(playerData.player);
            }

            if (playerData.misc.serverGround && deltaY != 0) {
                this.pDeltaY = (float) (playerData.movement.fy -
                        ReflectionsUtil.getBlockBoundingBox(
                                BlockUtils.getBlock(
                                        playerData.player.getLocation().toVector().
                                                toLocation(playerData.player.getWorld()).subtract(0.0f, 0.1f, 0.0f))).maxY);
            }

            if (pDeltaY > 0.8) {
                if (playerData.misc.lastWeirdBlocks.hasNotPassed(20) || playerData.misc.lastClimeable.hasNotPassed(40)) return;
                if (vl++ > 40) {
                    flag("pDelta+= " + pDeltaY);
                }
            } else if (vl > 0) vl--;

            debug(Color.translate("pDelta+= " + pDeltaY));
        }
    }
}
