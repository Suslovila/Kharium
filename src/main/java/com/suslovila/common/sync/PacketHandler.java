package com.suslovila.common.sync;

import com.suslovila.ExampleMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ExampleMod.NAME.toLowerCase());

    public static void init() {
        int idx = 0;

        INSTANCE.registerMessage(PacketPrimordialExplosions.Handler.class, PacketPrimordialExplosions.class, idx++, Side.CLIENT);

    }
}
