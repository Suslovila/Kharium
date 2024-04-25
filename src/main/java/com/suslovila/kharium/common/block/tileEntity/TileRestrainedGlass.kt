package com.suslovila.kharium.common.block.tileEntity

import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.client.render.tile.RestrainGlassRenderer
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.utils.SusVec3
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.client.event.RenderWorldLastEvent

class TileRestrainedGlass : TileEntity(), PostRendered {

    override fun postRender(event: RenderWorldLastEvent) {
        RestrainGlassRenderer.postRender(this, event)
    }

    override fun onDataPacket(net: NetworkManager?, pkt: S35PacketUpdateTileEntity?) {
        super.onDataPacket(net, pkt)
    }

    override fun getDescriptionPacket(): Packet {
        return S35PacketUpdateTileEntity()
    }
}