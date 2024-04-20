package com.suslovila.kharium.client.render.tile.tileAntiNodeController

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.tileEntity.TileKharuSnare
import com.suslovila.kharium.utils.RotatableHandler
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.sus_multi_blocked.utils.Position
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX
import kotlin.random.Random

object DischargeFlaskRenderer {
    var flaskModel: IModelCustom
    val flaskTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/discharge_flask.png")
    val flaskOverTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/discharge_flask.png")
    val availableFacings =
        arrayListOf(ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH)

    init {
        flaskModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/discharge_flask.obj"))
    }

    fun render(tile: TileKharuSnare, partialTicks: Float) {
        GL11.glPushMatrix()
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        UtilsFX.bindTexture(flaskTexture)
        GL11.glTranslated(0.0, -0.7, 0.0)
        for (i in 0..4) {
            GL11.glPushMatrix()
            GL11.glRotated((45 + i * 90).toDouble(), 0.0, 1.0, 0.0)
            GL11.glTranslated(2.7, 0.0, 0.0)
            GL11.glRotated(-45.0, 0.0, 0.0, 1.0)
            GL11.glTranslated(0.0, 1.5 * tile.preparationPercent, 0.0)
            GL11.glScaled(0.5, 0.5, 0.5)
            flaskModel.renderOnly("bottom_Cube.004")
            GL11.glPopMatrix()
        }
        if(tile.isActive) renderDischarges(tile)
        GL11.glPopMatrix()


    }

    fun postRender(tile: TileKharuSnare, event: RenderWorldLastEvent) {
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + event.partialTicks
        GL11.glPushMatrix()
        SusGraphicHelper.translateFromPlayerTo(
            SusVec3(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5),
            event.partialTicks
        )
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        UtilsFX.bindTexture(flaskTexture)
        GL11.glTranslated(0.0, -0.7, 0.0)
        for (i in 0..4) {
            GL11.glPushMatrix()
            GL11.glRotated((45 + i * 90).toDouble(), 0.0, 1.0, 0.0)
            GL11.glTranslated(2.7, 0.0, 0.0)
            GL11.glRotated(-45.0, 0.0, 0.0, 1.0)
            GL11.glTranslated(0.0, 1.5 * tile.preparationPercent, 0.0)
            GL11.glScaled(0.5, 0.5, 0.5)
            GL11.glDepthMask(false)
            GL11.glDisable(GL11.GL_CULL_FACE)
            GL11.glDisable(GL11.GL_ALPHA_TEST)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_LIGHTING)

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glColor4f(1f, 1f, 1f, 1f)
            flaskModel.renderPart("glass_Cube.003")

            GL11.glEnable(GL11.GL_CULL_FACE)
            GL11.glEnable(GL11.GL_ALPHA_TEST)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_LIGHTING)
            GL11.glDepthMask(true)
            GL11.glPopMatrix()

        }
        GL11.glPopMatrix()
    }

    fun renderDischarges(tile: TileKharuSnare) {
        if (!Minecraft.getMinecraft().isGamePaused) {
            if (SusUtils.random.nextInt(6) == 5) {
                val offsets = listOf(-1, 1)
                for (x in offsets) {
                    for (z in offsets) {

                        Kharium.proxy.nodeAntiBolt(
                            tile.worldObj,
                            x = tile.xCoord + 0.5f + 2.3f * x,
                            y = tile.yCoord + 0.5f,
                            z = tile.zCoord + 0.5f + 2.3f * z,
                            x2 = tile.xCoord + 0.5f + 3f * x,
                            y2 = tile.yCoord + 0.5f + 0.9f,
                            z2 = tile.zCoord + 0.5f + 3f * z
                        )
                    }
                }
            }
        }
    }
}