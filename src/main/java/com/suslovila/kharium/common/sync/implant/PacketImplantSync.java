package com.suslovila.kharium.common.sync.implant;

import com.suslovila.kharium.api.implants.ItemImplant;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public class PacketImplantSync
        implements IMessage {
    private int entityId;
    private int slotId;

    private ItemStack implant;

    public PacketImplantSync() {
    }

    public PacketImplantSync(EntityPlayer player, int slotId, ItemStack implant) {
        this.entityId = player.getEntityId();
        this.slotId = slotId;
        this.implant = implant;
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(slotId);
        ByteBufUtils.writeItemStack(buffer, implant);
    }


    public void fromBytes(ByteBuf buffer) {
        this.entityId = buffer.readInt();
        slotId = buffer.readInt();
        implant = ByteBufUtils.readItemStack(buffer);
    }

    public static class Handler
            implements IMessageHandler<PacketImplantSync, IMessage> {
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketImplantSync message, MessageContext ctx) {
            Entity entity = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityId);
            if (entity instanceof EntityPlayer) {
                KhariumPlayerExtendedData data = KhariumPlayerExtendedData.Companion.get((EntityPlayer) entity);
                if (data != null) {
                    data.getImplantStorage().setInventorySlotContents(message.slotId, message.implant);
                }
            }
            return null;
        }
    }
}


