package com.suslovila.client.render


import com.suslovila.client.particles.ParticleRenderDispatcher
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeStabilizerRenderer
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.utils.RotatableHandler
import com.suslovila.utils.SusGraphicHelper
import com.suslovila.utils.SusVec3
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