package com.suslovila.kharium.client.render


import com.suslovila.kharium.client.particles.ParticleRenderDispatcher
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.TileAntiNodeStabilizerRenderer
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.kharium.utils.RotatableHandler
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX

@SideOnly(Side.CLIENT)
class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {

        handleParticles()
        handleStabilizer(event)

    }

    private fun handleStabilizer(event: RenderWorldLastEvent) {
        for (pos in TileAntiNodeStabilizer.tiles) {
            if (Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) is TileAntiNodeStabilizer) {
                val tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) as TileAntiNodeStabilizer

                glPushMatrix()
                SusGraphicHelper.translateFromPlayerTo(SusVec3(
                    tile.xCoord + 0.5,
                    tile.yCoord + 0.5,
                    tile.zCoord+ 0.5),
                    event.partialTicks)

                RotatableHandler.rotateFromOrientation(tile.facing)
                TileAntiNodeStabilizerRenderer.renderGlasses()
                glPopMatrix()

            } else TileAntiNodeStabilizer.tiles.remove(pos)
        }

        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)

    }
    private fun handleParticles() {
        val profiler = Minecraft.getMinecraft().mcProfiler
        profiler.startSection("particles")
        ParticleRenderDispatcher.dispatch()
        profiler.endSection()
    }


}