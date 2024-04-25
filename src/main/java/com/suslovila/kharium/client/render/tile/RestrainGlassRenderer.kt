package com.suslovila.kharium.client.render.tile

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.render.ClientEventHandler
import com.suslovila.kharium.common.block.tileEntity.TileRestrainedGlass
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getBlockFromPos
import com.suslovila.kharium.utils.getPosition
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX

object RestrainGlassRenderer : SusTileRenderer<TileRestrainedGlass>() {
    val glassTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/warded_glass_1.png")


    fun postRender(tile: TileRestrainedGlass, event: RenderWorldLastEvent) {
        glPushMatrix()
        SusGraphicHelper.translateFromPlayerTo(
            SusVec3(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5),
            event.partialTicks
        )
        val world = Minecraft.getMinecraft().theWorld
        val block = tile.world.getBlockFromPos(tile.getPosition())
        glColor4d(1.0, 1.0, 1.0, 1.0)
        UtilsFX.bindTexture(glassTexture)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glDepthMask(false)

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glScaled(0.99, 0.99, 0.99)
        glColor4f(1f, 1f, 1f, 1f)
        SusGraphicHelper.cubeModel.renderAll()
        val brightness: Int = block.getMixedBrightnessForBlock(world, tile.xCoord, tile.yCoord, tile.zCoord)
        val tessellator = Tessellator.instance;
        tessellator.setBrightness(brightness)
        glCullFace(GL_FRONT)
        SusGraphicHelper.cubeModel.renderAll()
        glCullFace(GL_BACK)
        SusGraphicHelper.cubeModel.renderAll()
        glDepthMask(true)

        glDisable(GL_ALPHA_TEST)
        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)

        glPopMatrix()
    }

    override fun render(tile: TileRestrainedGlass, partialTicks: Float) {
        ClientEventHandler.postRenders.add(tile)
    }
}