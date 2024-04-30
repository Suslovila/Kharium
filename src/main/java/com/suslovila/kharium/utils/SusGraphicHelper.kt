package com.suslovila.kharium.utils

import com.suslovila.kharium.Kharium
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.config.Config
import java.awt.Color

object SusGraphicHelper {
    var cubeModel: IModelCustom
    val whiteBlank = ResourceLocation(Kharium.MOD_ID, "textures/whiteBlank.png")
    var savedLightY: Float = 0.0f
    var savedLightX: Float = 0.0f

    init {
        cubeModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/cube.obj"))
    }

    enum class BasicDirection(val vec3: SusVec3) {
        NORTH(SusVec3(0, 0, -1)),
        SOUTH(SusVec3(0, 0, 1)),
        EAST(SusVec3(1, 0, 0)),
        WEST(SusVec3(-1, 0, 0)),
        UP(SusVec3(0, 1, 0)),
        DOWN(SusVec3(0, -1, 0))
    }

    @JvmStatic
    fun drawGuideArrows() {
        with(Tessellator.instance) {
            glDisable(GL_TEXTURE_2D)
            glLineWidth(2F)

            startDrawing(GL_LINES)
            setColorRGBA_F(0F, 0F, 1F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(0.0, 0.0, 2.0)
            draw()

            startDrawing(GL_LINES)
            setColorRGBA_F(0F, 1F, 0F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(0.0, 2.0, 0.0)
            draw()

            startDrawing(GL_LINES)
            setColorRGBA_F(1F, 0F, 0F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(2.0, 0.0, 0.0)
            draw()

            glLineWidth(1F)
            glEnable(GL_TEXTURE_2D)
        }
    }

    fun bindColor(tessellator: Tessellator, color: Int, alpha: Float, fadeFactor: Float) {
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green / 255.0f
        val b = co.blue / 255.0f
        tessellator.setColorRGBA_F(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha)
    }

    fun translateFromPlayerTo(pos: SusVec3, partialTicks: Float) {
        val player = Minecraft.getMinecraft().thePlayer
        val destX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
        val destY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
        val destZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
        glTranslated(pos.x - destX, pos.y - destY, pos.z - destZ)
    }


    //draws line from specified position to zero of cord system
    fun drawFloatyLine(
        xFrom: Double,
        yFrom: Double,
        zFrom: Double,
        color: Int,
        texture: ResourceLocation,
        speed: Float,
        distance: Float,
        width: Float,
        time: Float,
        isTranslucent: Boolean,
        alpha: Double,
        renderPreparations: () -> Unit
    ) {
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green / 255.0f
        val b = co.blue / 255.0f
        if (isTranslucent) {
            glDepthMask(false)
        }

        val tessellator = Tessellator.instance

        UtilsFX.bindTexture(texture)
        glDisable(2884)
        tessellator.startDrawing(5)

        tessellator.setBrightness(15728880)
        renderPreparations()
        val dist = MathHelper.sqrt_double(xFrom * xFrom + yFrom * yFrom + zFrom * zFrom)
        val blocks = Math.round(dist)
        val length = blocks * (Config.golemLinkQuality / 2.0f)
        val f9 = 0.0f
        val x0 = 1.0

        var i = 0
        while (i <= length * distance) {
            val f2 = i / length
            val f3 = 1.0f - Math.abs(i - length / 2.0f) / (length / 2.0f)
            var dx =
                xFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.2f * f3)
            var dy =
                yFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.2f * f3)
            var dz =
                zFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.2f * f3)
            tessellator.setColorRGBA_F(r, g, b, (f3 * alpha).toFloat())
            val x3 = (1.0f - f2) * dist - time * speed

            tessellator.addVertexWithUV(dx * f2, dy * f2 - width, dz * f2, x3.toDouble(), x0.toDouble())
            tessellator.addVertexWithUV(dx * f2, dy * f2 + width, dz * f2, x3.toDouble(), f9.toDouble())
            ++i
        }
        tessellator.draw()

        tessellator.startDrawing(5)
        var var84 = 0
        while (var84 <= length * distance) {
            val f2 = var84.toFloat() / length
            val f3 = 1.0f - Math.abs(var84 - length / 2.0f) / (length / 2.0f)
            var dx =
                xFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.2f * f3)
            var dy =
                yFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.2f * f3)
            var dz =
                zFrom + (MathHelper.sin((((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.2f * f3)
            tessellator.setColorRGBA_F(r, g, b, (f3 * alpha).toFloat())
            val x3 = (1.0f - f2) * dist - time * speed

            tessellator.addVertexWithUV(dx * f2 - width, dy * f2, dz * f2, x3.toDouble(), x0.toDouble())
            tessellator.addVertexWithUV(dx * f2 + width, dy * f2, dz * f2, x3.toDouble(), f9.toDouble())
            ++var84
        }
        tessellator.draw()
        if (isTranslucent) {
            glEnable(GL_CULL_FACE)
            glDisable(GL_BLEND)
            glDepthMask(true)
        }
    }

    fun pushLight() {
        savedLightX = OpenGlHelper.lastBrightnessX
        savedLightY = OpenGlHelper.lastBrightnessY
    }

    fun popLight() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, savedLightX, savedLightY)
    }

    fun getRenderGlobalTime(partialTicks: Float) =
        Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks

    fun setMaxBrightness() {
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        //values got by testing
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
    }
}
