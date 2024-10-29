package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.event.KharuTickHandler;
import com.suslovila.kharium.common.event.PrimordialExplosionHandler;
import com.suslovila.kharium.common.worldSavedData.Explosion;
import com.suslovila.kharium.common.worldSavedData.KharuHotbed;
import com.suslovila.kharium.utils.SusVec3;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class PacketKharuHotbeds implements IMessage {
    private ArrayList<KharuHotbed> hotbeds = new ArrayList<>();

    public PacketKharuHotbeds() {
    }

    public PacketKharuHotbeds(ArrayList<KharuHotbed> hotbeds) {
        this.hotbeds = hotbeds;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(hotbeds.size());
        for (KharuHotbed hotbed : hotbeds) {
            hotbed.writeTo(buffer);
        }
    }

    public void fromBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        for (int i = 0; i < length; i++) {
            KharuHotbed hotbed = KharuHotbed.Companion.readFrom(buffer);
            hotbeds.add(hotbed);
        }
    }

    public static class Handler implements IMessageHandler<PacketKharuHotbeds, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketKharuHotbeds packet, MessageContext ctx) {
            ArrayList<KharuHotbed> onClient = KharuTickHandler.INSTANCE.getClientKharuHotbeds();
            onClient.clear();
            onClient.addAll(packet.hotbeds);

            return null;
        }
    }
}
