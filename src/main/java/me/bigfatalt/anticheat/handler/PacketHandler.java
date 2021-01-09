package me.bigfatalt.anticheat.handler;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.impl.PacketReceiveEvent;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.data.PlayerData;

public class PacketHandler implements AtlasListener {

    @Listen
    public void onPacketReceive(PacketReceiveEvent event) {
        PlayerData data = AntiCunt.instance.dataManager.getData(event.getPlayer());
        data.actionProcessor.timeStamp = event.getTimeStamp();
        data.packetParser.parserReceivingPackets(data, event.getPacket(), event.getType());
    }
}
