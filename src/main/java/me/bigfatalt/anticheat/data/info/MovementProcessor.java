package me.bigfatalt.anticheat.data.info;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.data.PlayerData;

public class MovementProcessor {
    public double fx, fy, fz; // Last location
    public double tx, ty, tz; // Current location

    public double deltaH;
    public double lDeltaH;

    public float fpitch, fyaw;
    public float tpitch, tyaw;

    public float deltaYaw, deltaPitch, pitchDifference, yawDifference, yawAcceleration, pitchAcceleration;
    public float lDeltaYaw, lDeltaPitch, lPitchDifference, lYawDifference, lYawAcceleration, lPitchAcceleration;

    public void parseMovement(WrappedInFlyingPacket packet, PlayerData data) {
        if (packet.isPos()) {
            this.fx = this.tx;
            this.fy = this.ty;
            this.fz = this.tz;
            this.tx = packet.getX();
            this.ty = packet.getY();
            this.tz = packet.getZ();
        }

        if (packet.isLook()) {
            this.fyaw = this.tyaw;
            this.fpitch = this.tpitch;
            this.tyaw = packet.getYaw();
            this.tpitch = packet.getPitch();
        }

        this.lDeltaYaw = this.deltaYaw;
        this.deltaYaw = MathUtils.getAngleDelta(this.tyaw, this.fyaw);
        this.lYawAcceleration = this.yawAcceleration;
        this.yawAcceleration = MathUtils.getDelta(this.lDeltaYaw, this.deltaYaw);

        this.lDeltaPitch = this.deltaPitch;
        this.deltaPitch = MathUtils.getDelta(this.fpitch, this.tpitch);
        this.lPitchAcceleration = this.pitchAcceleration;
        this.pitchAcceleration = MathUtils.getDelta(this.lDeltaPitch, this.deltaPitch);

    }
}
