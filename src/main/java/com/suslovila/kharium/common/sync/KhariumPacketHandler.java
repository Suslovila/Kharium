package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.Kharium;
import com.suslovila.kharium.common.sync.implant.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class KhariumPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Kharium.NAME.toLowerCase());

    public static void init() {
        int idx = 12;

        INSTANCE.registerMessage(PacketPrimordialExplosions.Handler.class, PacketPrimordialExplosions.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketKharuHotbeds.Handler.class, PacketKharuHotbeds.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncSingleKharuHotbed.Handler.class, PacketSyncSingleKharuHotbed.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketKhariumPlayerExtended.Handler.class, PacketKhariumPlayerExtended.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketImplantSync.Handler.class, PacketImplantSync.class, idx++, Side.CLIENT);

        INSTANCE.registerMessage(PacketUseImplantSync.Handler.class, PacketUseImplantSync.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketEnableImplantSync.Handler.class, PacketEnableImplantSync.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketAllExtendedPlayerSync.Handler.class, PacketAllExtendedPlayerSync.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketOneExtendedPlayerSync.Handler.class, PacketOneExtendedPlayerSync.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketRuneInstallerButtonClicked.Handler.class, PacketRuneInstallerButtonClicked.class, idx++, Side.SERVER);

    }

}
