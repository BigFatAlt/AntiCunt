package me.bigfatalt.anticheat.parser;

import cc.funkemunky.api.tinyprotocol.api.Packet;
import cc.funkemunky.api.tinyprotocol.packet.in.*;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.data.PlayerData;

public class PacketParser {


    public void parserReceivingPackets(PlayerData data, Object packet, String type) {
        switch (type) {
            case Packet.Client.ARM_ANIMATION:
                WrappedInArmAnimationPacket armAnimationPacket = new WrappedInArmAnimationPacket(packet, data.player);

                data.fireChecks(armAnimationPacket);
                break;
            case Packet.Client.ABILITIES:
                WrappedInAbilitiesPacket abilitiesPacket = new WrappedInAbilitiesPacket(packet, data.player);

                data.fireChecks(abilitiesPacket);
                break;
            case Packet.Client.BLOCK_DIG:
                WrappedInBlockDigPacket blockDigPacket = new WrappedInBlockDigPacket(packet, data.player);

                data.actionProcessor.onBlockDig(blockDigPacket);
                data.fireChecks(blockDigPacket);
                break;
            case Packet.Client.BLOCK_PLACE:
                WrappedInBlockPlacePacket blockPlacePacket = new WrappedInBlockPlacePacket(packet, data.player);

                data.fireChecks(blockPlacePacket);
                break;
            case Packet.Client.ENTITY_ACTION:
                WrappedInEntityActionPacket entityActionPacket = new WrappedInEntityActionPacket(packet, data.player);

                data.fireChecks(entityActionPacket);
                break;
            case Packet.Client.USE_ENTITY:
                WrappedInUseEntityPacket useEntityPacket = new WrappedInUseEntityPacket(packet, data.player);

                data.actionProcessor.handleUseEntity(useEntityPacket, data);
                data.fireChecks(useEntityPacket);
                break;
            case Packet.Client.FLYING:
            case Packet.Client.POSITION_LOOK:
            case Packet.Client.LOOK:
            case Packet.Client.POSITION:
                WrappedInFlyingPacket flyingPacket = new WrappedInFlyingPacket(packet, data.player);

                data.movementProcessor.parseMovement(flyingPacket, data);
                data.actionProcessor.actionFlying();
                data.fireChecks(flyingPacket);
                break;
        }
    }
}
