package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.multiStructure.synthesizer.advancedSynthesizer.TileAdvancedSynthesizerCore;
import com.suslovila.sus_multi_blocked.utils.Position;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;

public class PacketGuiAdvancedSynthesizerChangeAspectAmount implements IMessage {
    private int addedAmount;
    private int requestId;

    private Position position;

    public PacketGuiAdvancedSynthesizerChangeAspectAmount(int requestId, int amount, Position position) {
        addedAmount = amount;
        this.requestId = requestId;
        this.position = position;
    }

    public PacketGuiAdvancedSynthesizerChangeAspectAmount() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(addedAmount);
        buf.writeInt(requestId);
        position.writeTo(buf);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        addedAmount = buf.readInt();
        requestId = buf.readInt();
        position = Position.Companion.readFrom(buf);
    }


    public static class Handler implements IMessageHandler<PacketGuiAdvancedSynthesizerChangeAspectAmount, IMessage> {
        @Override
        public IMessage onMessage(PacketGuiAdvancedSynthesizerChangeAspectAmount packet, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Position position = packet.position;
            if (!player.worldObj.blockExists(position.getX(), position.getY(), position.getZ())) return null;
            TileEntity tile = player.worldObj.getTileEntity(position.getX(), position.getY(), position.getZ());
            if (tile instanceof TileAdvancedSynthesizerCore) {
                TileAdvancedSynthesizerCore synthesizerCore = (TileAdvancedSynthesizerCore) tile;
                synthesizerCore.addAmountToRequest(packet.requestId, packet.addedAmount);
            }
            return null;
        }
    }
}


