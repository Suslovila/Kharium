package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.common.item.ItemKharuNetConfigurator;
import com.suslovila.sus_multi_blocked.common.item.ItemMultiBlockFormer;
import com.suslovila.sus_multi_blocked.common.item.Modifier;
import com.suslovila.sus_multi_blocked.common.item.MultiBlockWrapper;
import com.suslovila.sus_multi_blocked.utils.Position;
import com.suslovila.sus_multi_blocked.utils.SusNBTHelper;
import com.suslovila.sus_multi_blocked.utils.SusUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

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

            if (stack.getItem() instanceof ItemKharuNetConfigurator) {
                SusNBTHelper.INSTANCE.getOrCreateTag(stack).setInteger(ItemKharuNetConfigurator.INSTANCE.getCURRENT_PRIORITY_NBT(), packet.priority);
            }
            return null;
        }
    }
}


