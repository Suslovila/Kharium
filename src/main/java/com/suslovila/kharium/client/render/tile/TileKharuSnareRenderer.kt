package com.suslovila.kharium.client.render.tile

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusUtils.humilitasColor
import com.suslovila.kharium.client.render.ClientEventHandler
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.SusUtils.humilitasColorObj
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosDouble
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

object TileKharuSnareRenderer : SusTileRenderer<TileKharuSnare>() {
    var coreModel: IModelCustom
    var fieldModel: IModelCustom
    val runesOverStabilizer = "textures/blocks/kharu_snare_over.png"
    val coreTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/kharu_snare.png")
    val correctionOffset = -2.0
    val antiNodeOffset = -5.0

    init {
        coreModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/kharu_snare.obj"))
        fieldModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/field.obj"))

    }

    override fun render(tile: TileKharuSnare, partialTicks: Float) {
        ClientEventHandler.postRenders.add(tile)
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
        glPushMatrix()
        glTranslated(0.0, correctionOffset, 0.0)
        renderCore()
        if (tile.isPrepared) {
            renderTranslatingKharu(tile, partialTicks, time)
        }
        glPopMatrix()
        AntiNodeStabilizersRenderer.render(tile, partialTicks)
        DischargeFlaskRenderer.render(tile, partialTicks)
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    private fun renderTranslatingKharu(tile: TileKharuSnare, partialTicks: Float, time: Float) {
        glDisable(GL_LIGHTING)
        glTranslated(0.0, -1.0 + correctionOffset, 0.0)
        for (i in 0..4) {
            glPushMatrix()
            glRotated((45 + i * 90).toDouble(), 0.0, 1.0, 0.0)
            glTranslated(2.7, 0.0, 0.0)
            val scaleFactor = 3.0
            glScaled(scaleFactor, scaleFactor, scaleFactor)
            SusGraphicHelper.drawFloatyLine(
                -3.0 / scaleFactor,
                -6.3 / scaleFactor,
                0.0,
                1,
                ResourceLocation(Kharium.MOD_ID, "textures/misc/bubble.png"),
                speed = 0.1f,
                Math.min(time, 10.0f) / 10.0f,
                width = 0.3F,
                xScale = 1F,
                yScale = 2F,
                zScale = 1F,
                time = time,
                false,
                tile.getClientPreparationPercent(partialTicks)
            )
            SusGraphicHelper.drawFloatyLine(
                -3.0 / scaleFactor,
                -6.3 / scaleFactor,
                0.0,
                Color.red.rgb,
                ResourceLocation(Kharium.MOD_ID, "textures/misc/bubble.png"),
                speed = 0.05f,
                Math.min(time + 2, 10.0f) / 10.0f,
                width = 0.3F,
                xScale = 1F,
                yScale = 2F,
                zScale = 1F,
                time = time + 2,
                false,
                tile.getClientPreparationPercent(partialTicks)
            )
            glPopMatrix()
        }
    }


    fun renderCore() {
        glPushMatrix()

        glScaled(0.5, 0.5, 0.5)
        UtilsFX.bindTexture(coreTexture)

        glColor4f(1f, 1f, 1f, 1f)
        coreModel.renderAll()
//        renderGlowingElements()

        glPopMatrix()
    }


    private fun renderGlowingElements() {
        glPushMatrix()
        glAlphaFunc(516, 0.003921569f)
        glEnable(3042)
        glDisable(GL_LIGHTING)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        UtilsFX.bindTexture(Kharium.MOD_ID, runesOverStabilizer)
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        SusGraphicHelper.pushLight()
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
        SusGraphicHelper.popLight()
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            1f
        )
        coreModel.renderAll()
        glDisable(GL_BLEND)
        glAlphaFunc(516, 0.1f)
        glPopMatrix()
    }

    fun postRender(tile: TileKharuSnare, event: RenderWorldLastEvent) {
        glPushMatrix()
        SusGraphicHelper.translateFromPlayerTo(
            tile.getPosDouble().add(SusVec3(0.5, antiNodeOffset + correctionOffset - 1 + 0.5, 0.5)),
            event.partialTicks
        )
        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)
        glDisable(GL_ALPHA_TEST)
        glDepthMask(false)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        glDisable(GL_CULL_FACE)
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/whiteBlank.png")
        glColor4d(
            humilitasColorObj.red / 255.0,
            humilitasColorObj.green / 255.0,
            humilitasColorObj.blue / 255.0,
            0.07 * tile.getClientPreparationPercent(event.partialTicks)
        )
        val scaleFactor = 4.0
        glScaled(scaleFactor, scaleFactor, scaleFactor)
        fieldModel.renderAll()
        glPopMatrix()
        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glDisable(GL_ALPHA_TEST)
        glDepthMask(true)
    }

}