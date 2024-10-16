package com.suslovila.kharium.utils

import com.suslovila.kharium.Kharium
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.obj.WavefrontObject
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.config.Config
import java.awt.Color
import java.nio.ByteBuffer
import javax.vecmath.Matrix4d
import kotlin.math.sign

object SusGraphicHelper {
    val v1 = 0.0
    val v2 = 1.0
    val u1 = 0.0
    val u2 = 1.0

    var cubeModel: IModelCustom
    val whiteBlank = ResourceLocation(Kharium.MOD_ID, "textures/whiteBlank.png")
    var savedLightY: Float = 0.0f
    var savedLightX: Float = 0.0f
    var tessellatorBrightness = 0

    init {
        cubeModel =
            ModelWrapperDisplayList(
                AdvancedModelLoader.loadModel(
                    ResourceLocation(
                        Kharium.MOD_ID,
                        "models/cube.obj"
                    )
                ) as WavefrontObject
            )
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

    fun bindColor(color: Int, alpha: Float, fadeFactor: Float) {
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green / 255.0f
        val b = co.blue / 255.0f
        glColor4f(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha)
    }

    fun translateFromPlayerTo(pos: SusVec3, partialTicks: Float) {
        val player = Minecraft.getMinecraft().thePlayer
        val destX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
        val destY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
        val destZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
        glTranslated(pos.x - destX, pos.y - destY, pos.z - destZ)
    }

    fun EntityPlayer.getRenderPos(partialTicks: Float): SusVec3 {
        val destX = lastTickPosX + (posX - lastTickPosX) * partialTicks
        val destY = lastTickPosY + (posY - lastTickPosY) * partialTicks
        val destZ = lastTickPosZ + (posZ - lastTickPosZ) * partialTicks

        return SusVec3(destX, destY, destZ)
    }

    //draws line from specified graphic cords position to zero of cord system
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

    fun pushBrightness(tessellator: Tessellator) {
        tessellatorBrightness = tessellator.brightness
    }

    fun popBrightness(tessellator: Tessellator) {
        tessellator.brightness = tessellatorBrightness
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

    fun setStandartColors() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun setDefaultBlend() {
        glDisable(GL_LIGHTING)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
    }

    fun disableBlend() {
        glEnable(GL_LIGHTING)
        glDisable(GL_BLEND)
    }


    fun drawStack(itemRender: RenderItem, item: ItemStack?, x: Int, y: Int, zLevel: Float) {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        RenderHelper.enableGUIStandardItemLighting()
        val mc = Minecraft.getMinecraft() ?: return
        glPushMatrix()
        glPushAttrib(GL_TRANSFORM_BIT)
        glEnable(GL12.GL_RESCALE_NORMAL)
        if (item != null) {
            glEnable(GL_LIGHTING)
            glEnable(GL_DEPTH_TEST)
            val prevZ: Float = itemRender.zLevel
            itemRender.zLevel = zLevel
            itemRender.renderWithColor = false
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.zLevel = prevZ
            itemRender.renderWithColor = true
            glDisable(GL_DEPTH_TEST)
            glDisable(GL_LIGHTING)
        }
        glPopAttrib()
        glPopMatrix()
    }


    fun drawFromCenter(radius: Double) {
        val tessellator = Tessellator.instance
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV(-radius, radius, 0.0, u2, v2)
        tessellator.addVertexWithUV(radius, radius, 0.0, u2, v1)
        tessellator.addVertexWithUV(radius, -radius, 0.0, u1, v1)
        tessellator.addVertexWithUV(-radius, -radius, 0.0, u1, v2)
        tessellator.draw()
    }

    fun alignZAxisWithVector(vec3: SusVec3) {

    }

    fun renderTexture(texture: ResourceLocation, scaleX: Double, scaleY: Double, scaleZ: Double) {
        UtilsFX.bindTexture(texture)
        glPushMatrix()
        glEnable(3042)
        glBlendFunc(770, 771)
        glColor4f(1.0f, 0.0f, 1.0f, 1.0f)
        if (Minecraft.getMinecraft().renderViewEntity is EntityPlayer) {
            val tessellator = Tessellator.instance
            val arX = ActiveRenderInfo.rotationX
            val arZ = ActiveRenderInfo.rotationZ
            val arYZ = ActiveRenderInfo.rotationYZ
            val arXY = ActiveRenderInfo.rotationXY
            val arXZ = ActiveRenderInfo.rotationXZ
            tessellator.startDrawingQuads()
            tessellator.setBrightness(220)
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f)
            val v1 = Vec3.createVectorHelper((-arX - arYZ).toDouble(), (-arXZ).toDouble(), (-arZ - arXY).toDouble())
            val v2 = Vec3.createVectorHelper((-arX + arYZ).toDouble(), arXZ.toDouble(), (-arZ + arXY).toDouble())
            val v3 = Vec3.createVectorHelper((arX + arYZ).toDouble(), arXZ.toDouble(), (arZ + arXY).toDouble())
            val v4 = Vec3.createVectorHelper((arX - arYZ).toDouble(), (-arXZ).toDouble(), (arZ - arXY).toDouble())
            val f2 = 0.0
            val f3 = 1.0

            val f4 = 0.0
            val f5 = 1.0
            tessellator.setNormal(0.0f, 0.0f, -1.0f)
            tessellator.addVertexWithUV(
                v1.xCoord * scaleX,
                v1.yCoord * scaleY,
                v1.zCoord * scaleZ,
                f2,
                f5
            )
            tessellator.addVertexWithUV(
                v2.xCoord * scaleX,
                v2.yCoord * scaleY,
                v2.zCoord * scaleZ,
                f3,
                f5
            )
            tessellator.addVertexWithUV(
                v3.xCoord * scaleX,
                v3.yCoord * scaleY,
                v3.zCoord * scaleZ,
                f3,
                f4
            )
            tessellator.addVertexWithUV(
                v4.xCoord * scaleX,
                v4.yCoord * scaleY,
                v4.zCoord * scaleZ,
                f2,
                f4
            )
            tessellator.draw()
        }
        glDisable(3042)
        glPopMatrix()
    }


    fun renderTextureOrth(
        texture: ResourceLocation,
        scaleX: Double,
        scaleY: Double,
        scaleZ: Double,
        lookVector: SusVec3
    ) {

        UtilsFX.bindTexture(texture)
        glPushMatrix()
        glEnable(3042)
        glDisable(GL_CULL_FACE)
        glBlendFunc(770, 771)
        if (Minecraft.getMinecraft().renderViewEntity is EntityPlayer) {
            val lookVecXZProjection = SusVec3(lookVector.x, 0.0, lookVector.z)
            val lookVecYZProjection = SusVec3(0.0, lookVector.y, lookVector.z)

            val angleAroundY = (SusVec3.angleBetweenVec3(SusVec3(0, 0, 1), lookVecXZProjection) * 180.0 / Math.PI) * lookVecXZProjection.x.sign
            val angleAroundX = (SusVec3.angleBetweenVec3(SusVec3(0, 0, 1), lookVecYZProjection) * 180.0 / Math.PI) * lookVecYZProjection.y.sign

            glRotated(angleAroundY, 0.0, 1.0, 0.0)
            glRotated(angleAroundX, 1.0, 0.0, 0.0)

            SusGraphicHelper.drawGuideArrows()
//            GL11.glMultMatrix()


            val normalizedLookVector = lookVector.normalize()

            val upVector = SusVec3(0.0, 1.0, 0.0)
            val xAxis = normalizedLookVector.cross(upVector).normalize()
            val yAxis = xAxis.cross(normalizedLookVector).normalize()

            val rotationMatrix = Matrix4d(
                xAxis.x, yAxis.x, -normalizedLookVector.x, 0.0,
                xAxis.y, yAxis.y, (-normalizedLookVector.y), 0.0,
                xAxis.z, yAxis.z, (-normalizedLookVector.z), 0.0,
                0.0, 0.0, 0.0, 1.0
            )

            with(rotationMatrix) {
                val doubleBuffer = ByteBuffer.allocateDirect(16 * java.lang.Double.BYTES).asDoubleBuffer()
                doubleBuffer.put(m00).put(m01).put(m02).put(m03)
                doubleBuffer.put(m10).put(m11).put(m12).put(m13)
                doubleBuffer.put(m20).put(m21).put(m22).put(m23)
                doubleBuffer.put(m30).put(m31).put(m32).put(m33)
                doubleBuffer.flip()
                glMultMatrix(doubleBuffer)
            }

            drawGuideArrows()
            this.drawFromCenter(1.0)
        }
        glDisable(3042)
        glPopMatrix()
    }

}
