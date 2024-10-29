package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.client.implantInfluence.IllusionRenderer;
import com.suslovila.kharium.client.implantInfluence.Illusion;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketAddIllusion implements IMessage {
    private Illusion illusion;
    public PacketAddIllusion(Illusion illusion) {
        this.illusion = illusion;
    }

    public PacketAddIllusion() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        illusion.writeTo(buf);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        illusion = Illusion.Companion.readFrom(buf);
    }


    public static class Handler implements IMessageHandler<PacketAddIllusion, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketAddIllusion packet, MessageContext ctx) {
            IllusionRenderer.INSTANCE.getIllusions().add(packet.illusion);

            return null;
        }
    }
}


