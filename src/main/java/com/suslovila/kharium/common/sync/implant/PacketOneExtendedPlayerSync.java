package com.suslovila.kharium.common.sync.implant;

import com.suslovila.kharium.api.implants.ImplantStorage;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;


public class PacketOneExtendedPlayerSync
        implements IMessage {
    private int entityId;
    private ImplantStorage implants;
    private int kharuAmount;

    public PacketOneExtendedPlayerSync() {
    }

    public PacketOneExtendedPlayerSync(KhariumPlayerExtendedData data, EntityPlayer player) {
        this.entityId = player.getEntityId();
        this.implants = data.getImplantStorage();
        this.kharuAmount = data.getKharuAmount();
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityId);
        implants.writeTo(buffer);
        buffer.writeInt(kharuAmount);
    }


    public void fromBytes(ByteBuf buffer) {
        this.entityId = buffer.readInt();
        implants = ImplantStorage.Companion.readFrom(buffer);
        kharuAmount = buffer.readInt();
    }

    public static class Handler
            implements IMessageHandler<PacketOneExtendedPlayerSync, IMessage> {
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketOneExtendedPlayerSync message, MessageContext ctx) {
            Entity entity = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityId);
            if (entity instanceof EntityPlayer) {
                KhariumPlayerExtendedData data = KhariumPlayerExtendedData.Companion.get((EntityPlayer) entity);
                if (data != null) {
                    data.setImplantStorage(message.implants);
                    data.setKharuAmount(message.kharuAmount);
                }
            }
            return null;
        }
    }
}


