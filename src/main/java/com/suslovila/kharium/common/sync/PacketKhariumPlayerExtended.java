
package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.api.implants.ImplantStorage;
import com.suslovila.kharium.common.event.KharuTickHandler;
import com.suslovila.kharium.common.worldSavedData.KharuHotbed;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class PacketKhariumPlayerExtended implements IMessage {

    int kharuAmount;
    ImplantStorage implantStorage;
    public PacketKhariumPlayerExtended() {
    }

    public PacketKhariumPlayerExtended(KhariumPlayerExtendedData data) {
        this.kharuAmount = data.getKharuAmount();
        this.implantStorage = data.getImplantStorage();

    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(kharuAmount);
        implantStorage.writeTo(buffer);
    }

    public void fromBytes(ByteBuf buffer) {
        kharuAmount = buffer.readInt();
        implantStorage = ImplantStorage.Companion.readFrom(buffer);
    }

    public static class Handler implements IMessageHandler<PacketKhariumPlayerExtended, IMessage> {
        @Override
        public IMessage onMessage(PacketKhariumPlayerExtended packet, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(player == null) return null;
            KhariumPlayerExtendedData playerEx = KhariumPlayerExtendedData.Companion.get(player);
            if(playerEx == null) return null;

            playerEx.setKharuAmount(packet.kharuAmount);
            playerEx.setImplantStorage(packet.implantStorage);

            return null;
        }
    }
}
