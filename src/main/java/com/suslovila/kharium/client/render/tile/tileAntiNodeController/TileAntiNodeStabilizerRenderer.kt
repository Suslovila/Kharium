package com.suslovila.kharium.client.render.tile.tileAntiNodeController

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.render.tile.SusTileRenderer
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusUtils.humilitasColor
import com.suslovila.kharium.client.particles.FXSmokeSpiral
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.kharium.mixinUtils.IFxScaleProvider
import com.suslovila.kharium.utils.RotatableHandler
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.fx.ParticleEngine
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

@SideOnly(Side.CLIENT)
object TileAntiNodeStabilizerRenderer : SusTileRenderer<TileAntiNodeStabilizer>() {
    var glassModel: IModelCustom;
    var coreModel: IModelCustom;
    val runesOverStabilizer = "textures/blocks/stabilizerCoreOver.png"
    val glassTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/stabilizerGlasses.png")
    val coreTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/stabilizerCore.png")

    init {
        glassModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        coreModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/stabilizerCore2.obj"))

    }

    override fun render(tile: TileAntiNodeStabilizer, partialTicks: Float) {
        tile.world ?: return
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks

        glPushMatrix()
        RotatableHandler.rotateFromOrientation(tile.facing)
        renderCore()
        glDepthMask(false)
        renderMagicCircles(time)
        glDepthMask(true)
        glPopMatrix()

        renderPlasmaWaves(partialTicks, tile)
        renderSpinningEssence(tile)

        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    private fun renderMagicCircles(time: Float) {
        glPushMatrix()
        glEnable(GL_ALPHA_TEST)
        glDisable(GL_CULL_FACE)
        glAlphaFunc(GL_GEQUAL, 0f)
        glEnable(3042)
        glDisable(GL_LIGHTING)
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            0.8f
        )
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        //values got by testing
        SusGraphicHelper.pushLight()
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
        SusGraphicHelper.popLight()
        glTranslatef(0.0f, -0.63f, 0f)
        glRotatef(180f, 1f, 0f, 0f)
        glPushMatrix()
        glRotatef((time * 4) % 360, 0f, 1f, 0f)
        val tessellator = Tessellator.instance
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/blocks/circle2.png")
        tessellator.setBrightness(15728880)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV(-2.0, 0.0, 2.0, 0.0, 0.0)
        tessellator.addVertexWithUV(2.0, 0.0, 2.0, 1.0, 0.0)
        tessellator.addVertexWithUV(2.0, 0.0, -2.0, 1.0, 1.0)
        tessellator.addVertexWithUV(-2.0, 0.0, -2.0, 0.0, 1.0)

        tessellator.draw()
        glPopMatrix()
        glPushMatrix()
        glTranslated(0.0, 0.01, 0.0)
        glRotatef(-(time * 4 % 360), 0f, 1f, 0f)
        SusGraphicHelper.drawGuideArrows()

        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/blocks/circle3.png")
        tessellator.setBrightness(15728880)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 0.0)
        tessellator.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 0.0)
        tessellator.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 1.0)
        tessellator.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 1.0)
        tessellator.draw()

        glPopMatrix()
        glPushMatrix()
        glTranslated(0.0, 0.02, 0.0)
        glScaled(0.5, 0.5, 0.5)

        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/blocks/innerRune.png")
        tessellator.setBrightness(15728880)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 0.0)
        tessellator.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 0.0)
        tessellator.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 1.0)
        tessellator.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 1.0)
        tessellator.draw()
        glPopMatrix()

        glDisable(3042)
        glAlphaFunc(516, 0.1f)
        glEnable(GL_CULL_FACE)

        glPopMatrix()
    }

    fun renderGlasses() {
        glPushMatrix()

        glScaled(0.5, 0.5, 0.5)
        UtilsFX.bindTexture(glassTexture)
        glDepthMask(false)
        glDisable(GL_CULL_FACE)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glColor4f(1f, 1f, 1f, 1f)
        glScaled(0.996, 0.996, 0.996)
        glassModel.renderAll()

        glEnable(GL_CULL_FACE)
        glEnable(GL_ALPHA_TEST)
        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glDepthMask(true)

        glPopMatrix()
    }

    fun renderCore() {
        glPushMatrix()

        glScaled(0.5, 0.5, 0.5)
        UtilsFX.bindTexture(coreTexture)

        glColor4f(1f, 1f, 1f, 1f)
        coreModel.renderAll()
        renderGlowingElements()

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

    private fun renderSpinningEssence(tile: TileAntiNodeStabilizer) {
        val deltaPos = RotatableHandler.getFacingVector(tile.facing).normalize().scale(-0.23)
        val fx1 = FXSmokeSpiral(
            tile.world,
            tile.xCoord.toDouble() + 0.5 + deltaPos.x,
            tile.yCoord.toDouble() + 0.5 + deltaPos.y,
            tile.zCoord.toDouble() + 0.5 + deltaPos.z,
            0.2F,
            SusUtils.random.nextInt(360),
            (tile.yCoord),
            RotatableHandler.getFacingVector(tile.facing),
            ResourceLocation("textures/misc/p_large.png")
        )
        (fx1 as? IFxScaleProvider)?.setScale(0.3f) ?: throw Exception("can't cast to Mixin Interface")
        ParticleEngine.instance.addEffect(tile.world, fx1)
    }


    private fun renderPlasmaWaves(partialTicks: Float, tile: TileAntiNodeStabilizer) {
//        glPushMatrix()
//        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
//        RotatableHandler.rotateFromOrientation(tile.facing)
//        for (i in 1..4) {
//            glPushMatrix()
//            glRotatef(90f * i, 0f, 1f, 0f)
//            glTranslated(2.0, -0.4, 0.0)
//            SusGraphicHelper.drawFloatyLine(
//                xFrom = -1.35,
//                yFrom = 1.5,
//                zFrom = 0.0,
//                color = humilitasColor,
//                ResourceLocation("thaumcraft", "textures/misc/wispy.png"),
//                speed = 0.1f,
//                Math.min(time, 10.0f) / 10.0f,
//                width = 0.3F,
//                xScale = 1F,
//                yScale = 2F,
//                zScale = 1F,
//                time = time,
//                true,
//
//            )
//            glPopMatrix()
//        }
//        glPopMatrix()
    }

}