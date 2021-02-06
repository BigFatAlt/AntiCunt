package me.bigfatalt.anticheat.handler;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.impl.PacketReceiveEvent;
import cc.funkemunky.api.events.impl.PacketSendEvent;
import cc.funkemunky.api.tinyprotocol.api.Packet;
import cc.funkemunky.api.tinyprotocol.packet.in.*;
import cc.funkemunky.api.utils.Init;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.data.PlayerData;

@Init
public class PacketHandler implements AtlasListener {

    @Listen
    public void onReceive(PacketReceiveEvent event) {
        PlayerData data = AntiCunt.instance.playerDataManager.getData(event.getPlayer());
        String type = event.getType();
        Object object = event.getPacket();
        long timeStamp = event.getTimeStamp();

        switch (type) {
            case Packet.Client.ARM_ANIMATION:
                WrappedInArmAnimationPacket armAnimationPacket = new WrappedInArmAnimationPacket(object, data.player);

                data.fireCheck(armAnimationPacket, timeStamp);
                break;
            case Packet.Client.FLYING:
            case Packet.Client.POSITION:
            case Packet.Client.POSITION_LOOK:
            case Packet.Client.LOOK:
                WrappedInFlyingPacket flyingPacket = new WrappedInFlyingPacket(object, data.player);

                data.miscParser.parserFlying(flyingPacket, data);
                data.movementParser.parserMovement(flyingPacket, data);
                data.fireCheck(flyingPacket, timeStamp);
                break;
            case Packet.Client.BLOCK_DIG:
                WrappedInBlockDigPacket digPacket = new WrappedInBlockDigPacket(object, data.player);

                data.miscParser.parserDigging(digPacket, data);
                data.fireCheck(digPacket, timeStamp);
                break;
            case Packet.Client.USE_ENTITY:
                WrappedInUseEntityPacket useEntityPacket = new WrappedInUseEntityPacket(object, data.player);

                data.miscParser.parserUseEntity(useEntityPacket, data);
                data.fireCheck(useEntityPacket, timeStamp);
                break;
            case Packet.Client.CUSTOM_PAYLOAD:
                WrappedInCustomPayload customPayload = new WrappedInCustomPayload(object, data.player);

                data.fireCheck(customPayload, timeStamp);
                break;
            case Packet.Client.BLOCK_PLACE:
                WrappedInBlockPlacePacket blockPlacePacket = new WrappedInBlockPlacePacket(object, data.player);

                data.fireCheck(blockPlacePacket, timeStamp);
                break;
            case Packet.Client.HELD_ITEM_SLOT:
                WrappedInHeldItemSlotPacket heldItemSlotPacket = new WrappedInHeldItemSlotPacket(object, data.player);

                data.fireCheck(heldItemSlotPacket, timeStamp);
                break;
            case Packet.Client.ENTITY_ACTION:
                WrappedInEntityActionPacket entityActionPacket = new WrappedInEntityActionPacket(object, data.player);

                data.miscParser.parserAction(entityActionPacket, data);
                data.fireCheck(entityActionPacket, timeStamp);
                break;
            case Packet.Client.CLIENT_COMMAND:
                WrappedInClientCommandPacket clientCommandPacket = new WrappedInClientCommandPacket(object, data.player);

                data.miscParser.clientCommand(clientCommandPacket, data);
                data.fireCheck(clientCommandPacket, timeStamp);
                break;
            case Packet.Client.CLOSE_WINDOW:
                WrappedInCloseWindowPacket closeWindowPacket = new WrappedInCloseWindowPacket(object, data.player);

                data.miscParser.closeInventory(closeWindowPacket, data);
                data.fireCheck(closeWindowPacket, timeStamp);
                break;
            case Packet.Client.WINDOW_CLICK:
                WrappedInWindowClickPacket clickPacket = new WrappedInWindowClickPacket(object, data.player);

                data.fireCheck(clickPacket , timeStamp);
                break;
        }
    }
}
