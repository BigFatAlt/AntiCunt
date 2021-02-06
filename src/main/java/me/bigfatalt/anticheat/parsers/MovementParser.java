package me.bigfatalt.anticheat.parsers;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.BlockUtils;
import cc.funkemunky.api.utils.MathUtils;
import lombok.val;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.CollisionHandler;
import me.bigfatalt.anticheat.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovementParser {

    public void parserMovement(WrappedInFlyingPacket flyingPacket, PlayerData data) {
        if (flyingPacket.isPos()) {
            data.movement.fx = data.movement.tx;
            data.movement.fy = data.movement.ty;
            data.movement.fz = data.movement.ty;
            data.movement.tx = flyingPacket.getX();
            data.movement.ty = flyingPacket.getY();
            data.movement.tz = flyingPacket.getZ();

        }

        if (flyingPacket.isLook()) {
            data.movement.fyaw = data.movement.tyaw;
            data.movement.fpitch = data.movement.tpitch;
            data.movement.tyaw = flyingPacket.getYaw();
            data.movement.tpitch = flyingPacket.getPitch();
        }

        data.misc.clientGround = flyingPacket.isGround();
        data.misc.serverGround = data.movement.ty % 1 / 64. < 0.0001;

        if (data.misc.serverGround) {
            data.movement.groundTicks++;

            data.movement.airTicks = 0;
        } else {
            data.movement.airTicks++;

            data.movement.groundTicks = 0;
        }


        if (!data.misc.serverGround && data.movement.deltaY < 0 && !data.misc.inLiquid && !data.misc.onClimbable) {
            data.movement.fallDistance += data.movement.deltaY;
        } else data.movement.fallDistance = 0;

        data.movement.lDeltaH = data.movement.deltaH;
        data.movement.lDeltaV = data.movement.deltaV;
        data.movement.deltaH = Math.hypot(data.movement.tx - data.movement.fx, data.movement.tz - data.movement.fz);
        data.movement.deltaV = data.movement.ty - data.movement.fy;


        data.movement.deltaZ = Math.abs(data.movement.tz - data.movement.fz);
        data.movement.lDeltaZ = data.movement.deltaZ;
        data.movement.deltaX = Math.abs(data.movement.tx - data.movement.fx);
        data.movement.lDeltaX = data.movement.deltaX;
        data.movement.deltaY = (float) (data.movement.ty - data.movement.fy);
        data.movement.lDeltaY = data.movement.deltaY;

        data.movement.lDeltaYaw = data.movement.deltaYaw;
        data.movement.deltaYaw = Math.abs(data.movement.tyaw - data.movement.fyaw);

        data.movement.lDeltaPitch = data.movement.deltaPitch;
        data.movement.deltaPitch = Math.abs(data.movement.tpitch - data.movement.fpitch);

       
        
        parseEnvironment(data);
        parserBlocks(data);
        parserCinematic(data);
        parserSensitivity(data);

    }

    /*
     *
     * Full credit for the cinematic goes to GladUrBad
     *
     */

    private void parserCinematic(PlayerData data) {
        final float yawAccelAccel = Math.abs(data.movement.deltaYaw - data.movement.lDeltaYaw);
        final float pitchAccelAccel = Math.abs(data.movement.deltaPitch - data.movement.lDeltaPitch);

        final boolean invalidYaw = yawAccelAccel < .05 && yawAccelAccel > 0;
        final boolean invalidPitch = pitchAccelAccel < .05 && pitchAccelAccel > 0;

        final boolean exponentialYaw = String.valueOf(yawAccelAccel).contains("E");
        final boolean exponentialPitch = String.valueOf(pitchAccelAccel).contains("E");

        if (data.misc.sensitivity < 100 && (exponentialYaw || exponentialPitch)) {
            data.misc.cinimaticTicks += 3;
        } else if (invalidYaw || invalidPitch) {
            data.misc.cinimaticTicks += 1;
        } else {
            if (data.misc.cinimaticTicks > 0) data.misc.cinimaticTicks--;
        }
        if (data.misc.cinimaticTicks > 20) {
            data.misc.cinimaticTicks--;
        }

        data.misc.cinematic = data.misc.cinimaticTicks > 8 || (AntiCunt.instance.ticks - data.misc.lastCinimaticTicks < 120);

        if (data.misc.cinematic && data.misc.cinimaticTicks > 8) {
            data.misc.lastCinimaticTicks = AntiCunt.instance.ticks;
        }
    }

    /*
     *
     * Full credit for the sensitvity goes to GladUrBad
     *
     */

    private void parserSensitivity(PlayerData data) {
        float gcd = (float) MathUtil.getGcd(data.movement.deltaPitch, data.movement.lDeltaPitch);
        final double sensitivityModifier = Math.cbrt(0.8333 * gcd);
        final double sensitivityStepTwo = (sensitivityModifier / 0.6) - 0.3333;
        final double finalSensitivity = sensitivityStepTwo * 200;

        data.misc.finalSensitivity = finalSensitivity;

        data.misc.sensitivitySamples.add((int)finalSensitivity);

        if (data.misc.sensitivitySamples.size() >= 40) {
            data.misc.sensitivity = MathUtil.getMode(data.misc.sensitivitySamples);

            final float gcdOne = (data.misc.sensitivity / 200F) * 0.6F + 0.2F;
            gcd = gcdOne * gcdOne * gcdOne * 1.2F;

            data.misc.sensitivitySamples.clear();
        }
    }

    private void parserBlocks(PlayerData data) {
        data.misc.inLiquid = false;
        data.misc.onSlime = false;
        data.misc.onClimbable = false;

    }

    private void parseEnvironment(PlayerData data) {

        int startX = Location.locToBlock(data.movement.tx - 0.31);
        int endX = Location.locToBlock(data.movement.tx + 0.31);
        int startY = Location.locToBlock(data.movement.ty - 0.51);
        int endY = Location.locToBlock(data.movement.ty + 2.01);
        int startZ = Location.locToBlock(data.movement.tz - 0.31);
        int endZ = Location.locToBlock(data.movement.tz + 0.31);


        List<Block> blocks = new ArrayList<>();
        for (int bx = startX; bx <= endX; bx++) {
            for (int by = startY; by <= endY; by++) {
                for (int bz = startZ; bz <= endZ; bz++) {
                    Block block = BlockUtils.getBlock(new Location(data.player.getWorld(), bx, by, bz));
                    if (block != null) {
                        if (block.getType() != Material.AIR) {
                            blocks.add(block);
                        }
                    }
                }
            }
        }


        List<Entity> entities;


        if (data.movement.deltaH < 1 && data.movement.deltaV < 1) entities = data.player.getNearbyEntities(1 + data.movement.deltaH, 2 + data.movement.deltaV, 1 + data.movement.deltaH);
        else entities = Collections.emptyList();

        CollisionHandler handler = new CollisionHandler(blocks, entities, data);

        blocks.forEach(block -> {
            if (BlockUtils.isStair(block) || BlockUtils.isSlab(block)) {
                data.misc.lastWeirdBlocks.reset();
            }
        });

        if (handler.isCollidedWith(Material.SLIME_BLOCK)) data.misc.lastSlime.reset(); data.misc.onSlime = true;

        if (handler.isCollidedWith(Material.WATER) || handler.isCollidedWith(Material.STATIONARY_WATER)) data.misc.lastWater.reset(); data.misc.inLiquid = true;
        if (handler.isCollidedWith(Material.LADDER) || handler.isCollidedWith(Material.VINE)) data.misc.lastClimeable.reset(); data.misc.onClimbable = true;


     //   blocks.forEach(block -> data.player.sendMessage(block.getType() +""));

    }

}
