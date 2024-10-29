package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.item.ItemPortableAspectContainer;
import com.suslovila.sus_multi_blocked.utils.SusNBTHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PacketItemPortableContainer implements IMessage {
    private String aspect;
    private int requiredAmount;
    public PacketItemPortableContainer(String aspect, int amount) {
        this.aspect = aspect;
        requiredAmount = amount;
    }

    public PacketItemPortableContainer() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, aspect);
        buf.writeInt(requiredAmount);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        aspect = ByteBufUtils.readUTF8String(buf);
        requiredAmount = buf.readInt();
    }


    public static class Handler implements IMessageHandler<PacketItemPortableContainer, IMessage> {
        @Override
        public IMessage onMessage(PacketItemPortableContainer packet, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack stack = player.getHeldItem();
            if (stack == null) return null;

            if (stack.getItem() instanceof ItemPortableAspectContainer) {
                SusNBTHelper.INSTANCE.getOrCreateTag(stack).setString(
                        ItemPortableAspectContainer.INSTANCE.getREQUIRED_ASPECT_NBT(), packet.aspect);
                SusNBTHelper.INSTANCE.getOrCreateTag(stack).setInteger(
                        ItemPortableAspectContainer.INSTANCE.getREQUIRED_ASPECT_AMOUNT_NBT(), packet.requiredAmount);
            }
            return null;
        }
    }
}


