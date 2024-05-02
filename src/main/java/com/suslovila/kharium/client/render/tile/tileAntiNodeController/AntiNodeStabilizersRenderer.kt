package com.suslovila.kharium.client.render.tile.tileAntiNodeController

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusUtils.humilitasColor
import com.suslovila.kharium.client.particles.FXSmokeSpiral
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.mixinUtils.IFxScaleProvider
import com.suslovila.kharium.utils.*
import com.suslovila.sus_multi_blocked.utils.Position
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.fx.ParticleEngine
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

// this class is just a render shape!  It does not represent a real tile entity!
object AntiNodeStabilizersRenderer {
    var glassModel: IModelCustom
    var coreModel: IModelCustom
    val runesOverStabilizer = "textures/blocks/stabilizerCoreOver.png"
    val glassTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/stabilizerGlasses.png")
    val coreTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/stabilizerCore.png")
    val availableFacings =
        arrayListOf(ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH)

    init {
        glassModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        coreModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/stabilizerCore2.obj"))

    }

    // facing here is no connection with tileKharuSnare facing or smth like that!
    fun render(tile: TileKharuSnare, partialTicks: Float) {

        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
        for (facing in availableFacings) {
            glPushMatrix()
            glTranslated(0.0, -5.0 + TileKharuSnareRenderer.correctionOffset - 1, 0.0)
            RotatableHandler.rotateFromOrientation(facing)
            glTranslated(0.0, 5.5, 0.0)
            glScaled(7.0 / 5.0, 1.5, 7.0 / 5.0)
            renderCore(tile, partialTicks)
            renderPlasmaWaves(tile, partialTicks)
            if (tile.enabled) renderSpinningEssence(Position(tile.xCoord, tile.yCoord, tile.zCoord), tile.world, facing)

            glPopMatrix()
            UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
        }
    }

    private fun renderMagicCircles(tile: TileKharuSnare, partialTicks: Float) {
        glPushMatrix()
        glDisable(GL_ALPHA_TEST)
        glDisable(GL_CULL_FACE)
        glAlphaFunc(GL_GREATER, 0.0f)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
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
        glRotatef((SusGraphicHelper.getRenderGlobalTime(partialTicks) * 4) % 360, 0f, 1f, 0f)
        val scaleFactor = tile.getClientPreparationPercent(partialTicks)
        glScaled(scaleFactor, scaleFactor, scaleFactor)
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
        glRotatef(-(SusGraphicHelper.getRenderGlobalTime(partialTicks) * 4 % 360), 0f, 1f, 0f)
        glScaled(scaleFactor, scaleFactor, scaleFactor)

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
        glScaled(scaleFactor, scaleFactor, scaleFactor)

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

    fun renderCore(tile: TileKharuSnare, partialTicks: Float) {
        glPushMatrix()

        glScaled(0.5, 0.5, 0.5)
        UtilsFX.bindTexture(coreTexture)

        glColor4f(1f, 1f, 1f, 1f)
        coreModel.renderAll()
        renderGlowingElements(tile, partialTicks)

        glPopMatrix()
    }


    private fun renderGlowingElements(tile: TileKharuSnare, partialTicks: Float) {
        glPushMatrix()
        glAlphaFunc(516, 0.003921569f)
        glDisable(GL_LIGHTING)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        UtilsFX.bindTexture(Kharium.MOD_ID, runesOverStabilizer)
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        SusGraphicHelper.pushLight()
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            (1f * tile.getClientPreparationPercent(partialTicks)).toFloat()
        )
        coreModel.renderAll()
        glDisable(GL_BLEND)
        glAlphaFunc(516, 0.1f)
        glPopMatrix()
        SusGraphicHelper.popLight()

    }

    private fun renderSpinningEssence(pos: Position, world: World, facing: ForgeDirection) {
        val ortVec = RotatableHandler.getFacingVector(facing).normalize()

        val fx1 = FXSmokeSpiral(
            world,
            pos.x.toDouble() + 0.5 + ortVec.scale(0.8).x + ortVec.scale(5.0).x,
            pos.y.toDouble() + 0.5 - 5.0 + TileKharuSnareRenderer.correctionOffset - 1,
            pos.z.toDouble() + 0.5 + ortVec.scale(0.8).z + ortVec.scale(5.0).z,
            0.2F,
            SusUtils.random.nextInt(360),
            (pos.y),
            RotatableHandler.getFacingVector(facing),
            ResourceLocation("textures/misc/p_large.png")
        )
        (fx1 as? IFxScaleProvider)?.setScale(0.3f) ?: throw Exception("can't cast to Mixin Interface")
        ParticleEngine.instance.addEffect(world, fx1)
    }


    private fun renderPlasmaWaves(tile: TileKharuSnare, partialTicks: Float) {
        glPushMatrix()
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
        for (i in 1..4) {
            glPushMatrix()
            glRotatef(90f * i, 0f, 1f, 0f)
            glTranslated(2.0, -0.4, 0.0)
//            SusGraphicHelper.drawGuideArrows()
            SusGraphicHelper.drawFloatyLine(
                xFrom = -1.5,
                yFrom = 0.7,
                zFrom = 0.0,
                color = humilitasColor,
                ResourceLocation("thaumcraft", "textures/misc/wispy.png"),
                speed = 0.1f,
                Math.min(time, 10.0f) / 10.0f,
                width = 0.3F,
                time = time,
                true,
                tile.getClientPreparationPercent(partialTicks)

            ) {
                glEnable(GL_BLEND)
                glBlendFunc(GL_SRC_ALPHA, GL_ONE)
            }
            glPopMatrix()
        }
        glPopMatrix()
    }

    fun postRender(tile: TileKharuSnare, event: RenderWorldLastEvent) {
        glPushMatrix()
        SusGraphicHelper.translateFromPlayerTo(tile.getPosDouble().add(SusVec3(0.5, 0.5, 0.5)), event.partialTicks)

        for (facing in availableFacings) {
            glPushMatrix()
            glTranslated(0.0, -5.0 + TileKharuSnareRenderer.correctionOffset - 1, 0.0)
            RotatableHandler.rotateFromOrientation(facing)
            glTranslated(0.0, 5.5, 0.0)
            glScaled(7.0 / 5.0, 1.5, 7.0 / 5.0)
            glDepthMask(false)
            renderMagicCircles(tile, event.partialTicks)
            glDepthMask(true)
            renderGlasses()
            glPopMatrix()
        }
        glPopMatrix()
    }

}