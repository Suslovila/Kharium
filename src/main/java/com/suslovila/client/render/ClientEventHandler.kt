package com.suslovila.client.render

import com.suslovila.ExampleMod
import com.suslovila.api.TileRotatable
import com.suslovila.client.particles.ParticleRenderDispatcher
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeControllerBaseRenderer
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeStabilizerRenderer
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
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


        //handleField(event)

    }


    private fun handleStabilizer(event: RenderWorldLastEvent) {
        for (pos in TileAntiNodeStabilizer.tiles) {
            if (Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) is TileAntiNodeStabilizer) {
                val tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) as TileAntiNodeStabilizer
                val player = Minecraft.getMinecraft().thePlayer

                glPushMatrix()

                val posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                val posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                val posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                glTranslated(tile.xCoord - posX + 0.5, tile.yCoord - posY+ 0.5, tile.zCoord - posZ+ 0.5)
                tile.rotateFromOrientation()
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