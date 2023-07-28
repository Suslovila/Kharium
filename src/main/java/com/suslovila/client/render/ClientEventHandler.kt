package com.suslovila.client.render

import com.suslovila.ExampleMod
import com.suslovila.client.particles.ParticleRenderDispatcher
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeControllerBaseRenderer
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase
import cpw.mods.fml.common.eventhandler.EventPriority
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


    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        handleParticles()
        //handleField(event)
    }

    private fun handleParticles() {
        val profiler = Minecraft.getMinecraft().mcProfiler
        profiler.startSection("botania-particles")
        ParticleRenderDispatcher.dispatch()
        profiler.endSection()
    }

    private fun handleField(event: RenderWorldLastEvent) {
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "testWaste/shieldSphere.png")
        for (pos in TileAntiNodeControllerBase.tiles) {
            if (Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) is TileAntiNodeControllerBase) {
                val tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) as TileAntiNodeControllerBase
                val player = Minecraft.getMinecraft().thePlayer

                glPushMatrix()
                val posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                val posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                val posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                glTranslated(tile.xCoord - posX, tile.yCoord - posY, tile.zCoord - posZ)

                glDepthMask(false)
                glDisable(GL_CULL_FACE)
                glDisable(GL_ALPHA_TEST)
                glEnable(GL_BLEND)
                glDisable(GL_LIGHTING)

                glColor4f(0f, 0f, 1f, 1f)
                glScalef(4f, 4f, 4f)
                TileAntiNodeControllerBaseRenderer.model.renderAll()



                glEnable(GL_CULL_FACE)
                glEnable(GL_ALPHA_TEST)
                glDisable(GL_BLEND)
                glEnable(GL_LIGHTING)
                glDepthMask(true)
                glPopMatrix()

            }
        }
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
        glColor4f(1f, 1f, 1f, 1f)

    }

}