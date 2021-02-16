package me.bigfatalt.anticheat.parsers;

import cc.funkemunky.api.tinyprotocol.packet.in.*;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;
import org.bukkit.entity.LivingEntity;

public class MiscParser {

    public void parserDigging(WrappedInBlockDigPacket blockDigPacket, PlayerData data) {
        switch (blockDigPacket.getAction()) {
            case START_DESTROY_BLOCK:
                data.misc.digging = true;
                break;
            case STOP_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
                data.misc.digging = false;
                break;
        }
    }

    public void parserUseEntity(WrappedInUseEntityPacket entityPacket, PlayerData data) {
        if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            data.misc.lastTarget = (LivingEntity) entityPacket.getEntity();
            data.misc.hitTicks = 0;
        }

    }

    public void parserFlying(WrappedInFlyingPacket flyingPacket, PlayerData data) {
        long flying = System.currentTimeMillis() - data.misc.lastFlying;

        if (flying > 0) {
            data.misc.flyingSamples.add(flying);
        }

        if (data.misc.flyingSamples.size() == data.misc.flyingSamples.getMaxSize()) {
            double standardDeviation = MathUtil.getStandardDeviation(data.misc.flyingSamples);

            data.misc.lagging = standardDeviation > 120;
        }

        data.misc.hitTicks++;
        data.misc.lastFlying = System.currentTimeMillis();
    }

    public void parserAction(WrappedInEntityActionPacket actionPacket, PlayerData data) {
        switch (actionPacket.getAction()) {
            case START_SPRINTING:
                data.misc.sprint = true;
                break;
            case STOP_SPRINTING:
                data.misc.sprint = false;
                break;
        }
    }

    public void clientCommand(WrappedInClientCommandPacket clientCommandPacket, PlayerData data) {
        if (clientCommandPacket.getCommand() == WrappedInClientCommandPacket.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            data.misc.inventory = true;
        }
    }

    public void closeInventory(WrappedInCloseWindowPacket wrappedInCloseWindowPacket, PlayerData data) {
        data.misc.inventory = false;
    }

}
