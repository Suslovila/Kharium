package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.Kharium;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Kharium.NAME.toLowerCase());

    public static void init() {
        int idx = 0;

        INSTANCE.registerMessage(PacketPrimordialExplosions.Handler.class, PacketPrimordialExplosions.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketKharuHotbeds.Handler.class, PacketKharuHotbeds.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncSingleKharuHotbed.Handler.class, PacketSyncSingleKharuHotbed.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketKhariumPlayerExtended.Handler.class, PacketKhariumPlayerExtended.class, idx++, Side.CLIENT);

    }

    public static EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.side == Side.SERVER ? ctx.getServerHandler().playerEntity : null;
    }

}
