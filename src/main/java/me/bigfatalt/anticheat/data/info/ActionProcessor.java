package me.bigfatalt.anticheat.data.info;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.EvictingList;
import me.bigfatalt.anticheat.utils.MathUtil;
import org.bukkit.entity.Entity;

public class ActionProcessor {
    public final EvictingList<Long> flyingSamples = new EvictingList<>(50);

    public int hitTicks;
    public long timeStamp, lastFlyingTime;
    public boolean lagging;


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

}
