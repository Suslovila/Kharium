package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.item.ItemConfigurator;
import com.suslovila.sus_multi_blocked.utils.SusNBTHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PacketItemKharuNetConfigurator implements IMessage {
    private int priority;
    public PacketItemKharuNetConfigurator(int priority) {
        this.priority = priority;
    }

    public PacketItemKharuNetConfigurator() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(priority);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        priority = buf.readInt();
    }


    public static class Handler implements IMessageHandler<PacketItemKharuNetConfigurator, IMessage> {
        @Override
        public IMessage onMessage(PacketItemKharuNetConfigurator packet, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack stack = player.getHeldItem();
            if (stack == null) return null;

            if (stack.getItem() instanceof ItemConfigurator) {
                SusNBTHelper.INSTANCE.getOrCreateTag(stack).setInteger(ItemConfigurator.INSTANCE.getCURRENT_PRIORITY_NBT(), packet.priority);
            }
            return null;
        }
    }
}


