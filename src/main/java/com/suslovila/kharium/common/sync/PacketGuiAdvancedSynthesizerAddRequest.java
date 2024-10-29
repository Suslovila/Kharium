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

public class PacketGuiAdvancedSynthesizerAddRequest implements IMessage {
    private String aspect;
    private int requiredAmount;

    private Position position;

    public PacketGuiAdvancedSynthesizerAddRequest(Aspect aspect, int amount, Position position) {
        this.aspect = aspect.getTag();
        requiredAmount = amount;
        this.position = position;
    }

    public PacketGuiAdvancedSynthesizerAddRequest() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, aspect);
        buf.writeInt(requiredAmount);
        position.writeTo(buf);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        aspect = ByteBufUtils.readUTF8String(buf);
        requiredAmount = buf.readInt();
        position = Position.Companion.readFrom(buf);
    }


    public static class Handler implements IMessageHandler<PacketGuiAdvancedSynthesizerAddRequest, IMessage> {
        @Override
        public IMessage onMessage(PacketGuiAdvancedSynthesizerAddRequest packet, MessageContext ctx) {
            if (!Aspect.aspects.containsKey(packet.aspect)) {
                return null;
            }
            if (packet.requiredAmount < 1) {
                return null;
            }
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Position position = packet.position;
            if (!player.worldObj.blockExists(position.getX(), position.getY(), position.getZ())) return null;
            TileEntity tile = player.worldObj.getTileEntity(position.getX(), position.getY(), position.getZ());
            if (tile instanceof TileAdvancedSynthesizerCore) {
                TileAdvancedSynthesizerCore synthesizerCore = (TileAdvancedSynthesizerCore) tile;
                if (Aspect.aspects.containsKey(packet.aspect))
                    synthesizerCore.addAspectRequest(Aspect.getAspect(packet.aspect), packet.requiredAmount);
            }
            return null;
        }
    }
}


