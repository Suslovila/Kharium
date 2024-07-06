package com.suslovila.kharium.common.sync.implant;

import com.suslovila.kharium.api.implants.ItemImplant;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;


public class PacketUseImplantSync
        implements IMessage {
    private int slotId;


    public PacketUseImplantSync() {
    }

    public PacketUseImplantSync(int slotId) {
        this.slotId = slotId;
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(slotId);
    }


    public void fromBytes(ByteBuf buffer) {
        slotId = buffer.readInt();
    }

    public static class Handler
            implements IMessageHandler<PacketUseImplantSync, IMessage> {
        public IMessage onMessage(PacketUseImplantSync message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            KhariumPlayerExtendedData data = KhariumPlayerExtendedData.Companion.get(player);
            if (data != null) {
                ItemStack implant = data.getImplantStorage().getStackInSlot(message.slotId);
                if(implant != null) {
                    ItemImplant implantClass = (ItemImplant)implant.getItem();
//                    implantClass.onUseButtonClicked(player, message.slotId, implant);
                }

            }
            return null;
        }
    }
}


