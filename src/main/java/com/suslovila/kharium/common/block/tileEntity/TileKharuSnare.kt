package com.suslovila.kharium.common.block.tileEntity

import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.utils.SusVec3
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent

class TileKharuSnare : TileEntity(), PostRendered {

    // a rare situation when I need timer for both sides
    companion object {
        public val activationTime = 50
    }

    val isActive : Boolean
        get() {
            return timer == activationTime
        }
    var timer = 0

    val preparationPercent : Double
        get() {
            return timer.toDouble() / activationTime.toDouble()
        }
    override fun updateEntity() {
        timer = Math.min(timer + 1, activationTime)
    }

    override fun postRender(event: RenderWorldLastEvent) {
        AntiNodeStabilizersRenderer.postRender(SusVec3(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5), event)
        TileKharuSnareRenderer.postRender(SusVec3(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5), event)
        DischargeFlaskRenderer.postRender(this, event)
    }

    override fun getRenderBoundingBox(): AxisAlignedBB = INFINITE_EXTENT_AABB
}