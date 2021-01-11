package me.bigfatalt.anticheat.data.info;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.TickTimer;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.EvictingList;
import me.bigfatalt.anticheat.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class ActionProcessor {
    public final EvictingList<Long> flyingSamples = new EvictingList<>(50);

    public int hitTicks;
    public long timeStamp, lastFlyingTime;
    public boolean lagging, digging;

    public TickTimer lastBlockBreakStop = new TickTimer(30);


    public void handleUseEntity(final WrappedInUseEntityPacket wrapper, PlayerData data) {
        if (wrapper.getAction() != WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            return;
        }

        hitTicks = 0;
    }


    public void actionFlying() {
        ++hitTicks;

        long delay = System.currentTimeMillis() - lastFlyingTime;

        if (delay > 0) {
            flyingSamples.add(delay);
        }

        if (flyingSamples.isFull()) {
            double deviation = MathUtil.getStandardDeviation(flyingSamples);

            lagging = deviation > 120;
        }

        lastFlyingTime = System.currentTimeMillis();
    }

    public void onBlockDig(WrappedInBlockDigPacket digPacket) {
        switch (digPacket.getAction()) {
            case START_DESTROY_BLOCK:
                digging = true;
                break;
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                digging = false;
                lastBlockBreakStop.reset();
                break;
        }

    }

}
