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
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX

@SideOnly(Side.CLIENT)
class ClientEventHandler {


    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        handleParticles()
        handleField(event)
    }

    private fun handleParticles() {
        val profiler = Minecraft.getMinecraft().mcProfiler
        profiler.startSection("botania-particles")
        ParticleRenderDispatcher.dispatch()
        profiler.endSection()
    }

    private fun handleField(event: RenderWorldLastEvent) {
        for (i in TileAntiNodeControllerBase.tiles.indices) {
            val pos = TileAntiNodeControllerBase.tiles[i]
            if (Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) is TileAntiNodeControllerBase) {
                val tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) as TileAntiNodeControllerBase
                val player = Minecraft.getMinecraft().thePlayer
                GL11.glPushMatrix()
                val posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                val posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                val posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                GL11.glTranslated(tile.xCoord - posX, tile.yCoord - posY, tile.zCoord - posZ)

                /* 181 */GL11.glDepthMask(false)
                /* 182 */GL11.glDisable(GL11.GL_CULL_FACE)
                /* 183 */GL11.glDisable(GL11.GL_ALPHA_TEST)
                /* 184 */GL11.glEnable(GL11.GL_BLEND)
                /* 185 */GL11.glDisable(GL11.GL_LIGHTING)
                GL11.glColor4f(0f, 0f, 1f, 255f)
                UtilsFX.bindTexture(ExampleMod.MOD_ID, "testWaste/shieldSphere.png")
                GL11.glScalef(4f, 4f, 4f)
                TileAntiNodeControllerBaseRenderer.model.renderAll()


                //glDisable(GL_BLEND);
                //glDisable(GL_ALPHA_TEST);
                //glEnable(GL_LIGHTING);
                GL11.glEnable(GL11.GL_CULL_FACE)
                /* 215 */GL11.glEnable(GL11.GL_ALPHA_TEST)
                /* 216 */GL11.glDisable(GL11.GL_BLEND)
                /* 217 */GL11.glEnable(GL11.GL_LIGHTING)
                /* 218 */GL11.glDepthMask(true)
                /* 219 */GL11.glPopMatrix()
            }
        }
    }
}