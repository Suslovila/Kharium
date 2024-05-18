package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.event.KharuTickHandler;
import com.suslovila.kharium.common.worldSavedData.KharuHotbed;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class PacketSyncSingleKharuHotbed implements IMessage {
    private KharuHotbed hotbed;

    private SYNC_TYPE syncType;

    public PacketSyncSingleKharuHotbed() {
    }

    public PacketSyncSingleKharuHotbed(KharuHotbed hotbed, SYNC_TYPE type) {
        this.hotbed = hotbed;
        syncType = type;
    }

    public void toBytes(ByteBuf buffer) {
        hotbed.writeTo(buffer);
        buffer.writeInt(syncType.ordinal());
    }

    public void fromBytes(ByteBuf buffer) {
        hotbed = KharuHotbed.Companion.readFrom(buffer);
        syncType = SYNC_TYPE.values()[buffer.readInt()];
    }

    public static class Handler implements IMessageHandler<PacketSyncSingleKharuHotbed, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncSingleKharuHotbed packet, MessageContext ctx) {
            ArrayList<KharuHotbed> onClient = KharuTickHandler.INSTANCE.getClientKharuHotbeds();
            if(packet.syncType == SYNC_TYPE.ADD) {
                onClient.add(packet.hotbed);
            }
            else if (packet.syncType == SYNC_TYPE.REMOVE) {
                onClient.remove(packet.hotbed);
            }
            return null;
        }
    }

    public enum SYNC_TYPE {
        ADD,
        REMOVE
    }
}
