package com.suslovila.kharium.common.sync.implant;

import com.suslovila.kharium.api.implants.ImplantStorage;
import com.suslovila.kharium.extendedData.KhariumDataForSync;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PacketAllExtendedPlayerSync
        implements IMessage {

    private List<KhariumDataForSync> allData;

    public PacketAllExtendedPlayerSync() {
    }

    public PacketAllExtendedPlayerSync(List<KhariumPlayerExtendedData> data) {
        this.allData = data.stream().map(oneData -> new KhariumDataForSync(
                oneData.getPlayer().getEntityId(),
                oneData.getImplantStorage(),
                oneData.getKharuAmount()
                )
        ).collect(Collectors.toList());
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(allData.size());
        for(KhariumDataForSync oneData : allData) {
            buffer.writeInt(oneData.getPlayerId());
            oneData.getImplantStorage().writeTo(buffer);
            buffer.writeInt(oneData.getKharuAmount());
        }
    }


    public void fromBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        this.allData = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            int id = buffer.readInt();
            ImplantStorage storage = ImplantStorage.Companion.readFrom(buffer);
            int kharu = buffer.readInt();
            this.allData.add(new KhariumDataForSync(id, storage, kharu));
        }
    }

    public static class Handler
            implements IMessageHandler<PacketAllExtendedPlayerSync, IMessage> {
        public IMessage onMessage(PacketAllExtendedPlayerSync message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().thePlayer.worldObj;
            for(KhariumDataForSync data : message.allData) {
                Entity entity = world.getEntityByID(data.getPlayerId());
                if (entity instanceof EntityPlayer) {
                    KhariumPlayerExtendedData clientData = KhariumPlayerExtendedData.Companion.get((EntityPlayer) entity);
                    if (clientData != null) {
                        clientData.setImplantStorage(data.getImplantStorage());
                        clientData.setKharuAmount(data.getKharuAmount());
                    }
                }
            }
            return null;
        }
    }
}


