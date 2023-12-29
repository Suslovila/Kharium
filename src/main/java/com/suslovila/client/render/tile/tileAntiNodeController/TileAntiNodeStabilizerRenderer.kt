package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.api.SusTileRenderer
import com.suslovila.api.TileRotatable
import com.suslovila.api.utils.GraphicHelper
import com.suslovila.api.utils.SusUtils
import com.suslovila.api.utils.SusUtils.humilitasColor
import com.suslovila.client.particles.FXSusSmokeSpiral
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.mixinUtils.IFxScaleProvider
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.fx.ParticleEngine
import thaumcraft.client.fx.particles.FXEssentiaTrail
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.config.Config
import java.awt.Color

@SideOnly(Side.CLIENT)
object TileAntiNodeStabilizerRenderer : SusTileRenderer<TileAntiNodeStabilizer>() {
    var glassModel: IModelCustom;
    var coreModel: IModelCustom;

    val glassTexture = ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerGlasses.png")
    val coreTexture = ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerCore.png");

    init {
        glassModel =
            AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        coreModel =
            AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerCore2.obj"))

    }

    override fun renderTileEntityAt(tile: TileEntity?, x: Double, y: Double, z: Double, ticks: Float) {
        tile?.worldObj ?: return
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + ticks

        glPushMatrix()
        glTranslated(x + 0.5, y + 0.5, z + 0.5)
        glPushMatrix()
        (tile as TileRotatable).rotateFromOrientation()
        renderCore()
        renderMagicCircles(time)
        glPopMatrix()
        renderPlasmaWaves(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, ticks, tile)
        renderSpinningEssence(tile as TileAntiNodeStabilizer)

        glPopMatrix()
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    //the reason why I do not implement "render" is drawFloatyLine method, which translates the Coord system from player to the beginning of "vis line"
    //but in RenderTileEntityAt in SusTileRender I already translate matrix - so it causes awful bugs.
    override fun render(tile: TileAntiNodeStabilizer, partialTicks: Float) {
        TODO("Not yet implemented")
    }

    private fun renderMagicCircles(time : Float){
        glPushMatrix()
        glEnable(GL_ALPHA_TEST)
        glDisable(GL_CULL_FACE)
        glAlphaFunc(GL_GREATER, 0.003921569f)
        glEnable(3042)
        glDisable(GL_LIGHTING)
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            0.8f)
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        //values got by testing
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)

        glTranslatef(0.0f, -0.63f, 0f)
        glRotatef(180f, 1f, 0f, 0f)
        glPushMatrix()
        glRotatef((time * 4) % 360, 0f, 1f, 0f)
        val tessellator = Tessellator.instance
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/circle2.png")
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
        //xglScaled(0.6, 0.6, 0.6)
        GraphicHelper.drawGuideArrows()

        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/circle3.png")
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

        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/innerRune.png")
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

    private fun renderTranslatingEssence(tile: TileAntiNodeStabilizer) {
        fun spawnEssence(
            worldObj: World?,
            x: Double,
            y: Double,
            z: Double,
            x2: Double,
            y2: Double,
            z2: Double,
            count: Int,
            color: Int,
            scale: Float
        ) {
            val noRandomMovingParticle = object : FXEssentiaTrail(worldObj, x, y, z, x2, y2, z2, count, color, scale) {


                override fun setGravity(value: Float) {
                    this.particleGravity = 0f
                }

                override fun onUpdate() {
                    prevPosX = posX
                    this.particleMaxAge = 2
                    prevPosY = posY
                    prevPosZ = posZ
                    if (particleAge++ >= particleMaxAge) setDead()
                }

            }
            ParticleEngine.instance.addEffect(worldObj, noRandomMovingParticle)
        }

        for (x in -1 until 2 step 2) {
            spawnEssence(
                tile.worldObj,
                tile.xCoord + 0.5 + 0.3,
                tile.yCoord + 0.5,
                tile.zCoord + 0.5,
                tile.xCoord + 0.5 + x * 0.5,
                tile.yCoord + 0.5,
                tile.zCoord + 0.5,
                1,
                SusUtils.humilitasColor,
                0.4f
            )
        }
        for (z in -1 until 2 step 2) {
            spawnEssence(
                tile.worldObj,
                tile.xCoord + 0.5,
                tile.yCoord + 0.5,
                tile.zCoord + 0.5 + 0.3,
                tile.xCoord + 0.5,
                tile.yCoord + 0.5,
                tile.zCoord + 0.5 + z * 0.5,
                1,
                SusUtils.humilitasColor,
                0.4f
            )
        }
    }

    private fun renderGlowingElements(){
        glPushMatrix()
        glAlphaFunc(516, 0.003921569f)
        glEnable(3042)
        glDisable(GL_LIGHTING)

        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/stabilizerCoreOver.png")
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            1f)
        coreModel.renderAll()
        glDisable(3042)
        glAlphaFunc(516, 0.1f)
        glPopMatrix()
    }
    private fun renderSpinningEssence(tile: TileAntiNodeStabilizer) {
        val deltaPos = tile.getFacingVector().normalize().scale(-0.23)
        val fx1 = FXSusSmokeSpiral(tile.worldObj, tile.xCoord.toDouble() + 0.5 + deltaPos.x, tile.yCoord.toDouble() + 0.5 + deltaPos.y, tile.zCoord.toDouble() + 0.5 + deltaPos.z, 0.2F, SusUtils.random.nextInt(360), (tile.yCoord), tile.getFacingVector(), ResourceLocation("textures/misc/p_large.png"))
        //val fx = FXSmokeSpiral(tile.worldObj, tile.xCoord.toDouble()+0.5, tile.yCoord.toDouble() + 0.75, tile.zCoord.toDouble()+0.5, 0.25F, SUSUtils.random.nextInt(360), (tile.yCoord))
        (fx1 as? IFxScaleProvider)?.setScale(0.3f) ?: throw Exception("can't cast to Mixin Interface")
        ParticleEngine.instance.addEffect(tile.worldObj, fx1)
    }


    //to protect your mentality, I really do not recommend to try to understand what is going here, all numbers are just f*cking picked up randomly
    //if you are not afraid, so the only difficult thing was that "vis line" path had been too wide, and I needed to squeeze it, but squeezing causes other changes, so
    //just with help of universe I got the required result

    //welcome to SH*T-CODE ZONE. ONLY SH*T-CODE INSIDE MY PROJECTS!
    fun renderPlasmaWaves(xCenter: Double, yCenter: Double, zCenter: Double, partialTicks: Float, tile: TileRotatable) {
        glPushMatrix()

        val ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
        when(tile.facing){
            ForgeDirection.DOWN -> {}
            ForgeDirection.UP -> { glRotatef(180f,1f,0f,0f) }
            ForgeDirection.NORTH -> { glRotatef(90f, 1f, 0f, 0f) }
            ForgeDirection.SOUTH -> {glRotatef(-90f, 1f, 0f, 0f)}
            ForgeDirection.WEST -> {glRotatef(-90f, 0f, 0f, 1f)}
            ForgeDirection.EAST -> {glRotatef(90f, 0f, 0f, 1f)}
            else -> {}
        }

        GraphicHelper.drawGuideArrows()
        for(i in 1..4){
            glPushMatrix()
            glRotatef(90f * i, 0f ,1f, 0f)
            drawFloatyLine(xCenter, humilitasColor, "textures/misc/wispy.png", 0.1f,
                Math.min(ticks, 10.0f) / 10.0f, 0.3F, 1F, 2F, 1F, ticks)
            glPopMatrix()
        }

        glPopMatrix()
    }
    fun drawFloatyLine(
        factor: Double, color: Int,
        texture: String?, speed: Float, distance: Float, width: Float, xScale: Float, yScale: Float, zScale: Float, ticks: Float
    ) {

        glTranslated(2.0, -0.4, 0.0)
        GraphicHelper.drawGuideArrows()
        val time = ticks
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green / 255.0f
        val b = co.blue / 255.0f
        glDepthMask(false)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)


        val tessellator = Tessellator.instance
        var dc1x = -1.35
        var dc1y = 1.5
        var dc1z = 0.0


        UtilsFX.bindTexture(texture)
        glDisable(2884)
        tessellator.startDrawing(5)

        tessellator.setBrightness(15728880)

        val dist = MathHelper.sqrt_double(dc1x * dc1x + dc1y * dc1y + dc1z * dc1z)
        val blocks = Math.round(dist)
        val length = blocks * (Config.golemLinkQuality / 2.0f)
        val f9 = 0.0f
        val x0 = 1.0f
        var i = 0
        while (i <= length * distance) {
            val f2 = i / length
            val f3 = 1.0f - Math.abs(i - length / 2.0f) / (length / 2.0f)
            var dx =
                dc1x + (MathHelper.sin(((f3 % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.2f * f3)
            var dy =
                dc1y + (MathHelper.sin(((factor % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.2f * f3)
            var dz =
                dc1z + (MathHelper.sin(((f2 % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.2f * f3)
            tessellator.setColorRGBA_F(r, g, b, f3)
            val x3 = (1.0f - f2) * dist - time * speed

            dx /= xScale
            dy /= yScale
            dz /= zScale

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
                dc1x + (MathHelper.sin(((f3 % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.2f * f3)
            var dy =
                dc1y + (MathHelper.sin(((factor % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.2f * f3)
            var dz =
                dc1z + (MathHelper.sin(((f2 % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.2f * f3)
            tessellator.setColorRGBA_F(r, g, b, f3)
            val x3 = (1.0f - f2) * dist - time * speed

            dx /= xScale
            dy /= yScale
            dz /= zScale

            tessellator.addVertexWithUV(dx * f2 - width, dy * f2, dz * f2, x3.toDouble(), x0.toDouble())
            tessellator.addVertexWithUV(dx * f2 + width, dy * f2, dz * f2, x3.toDouble(), f9.toDouble())
            ++var84
        }
        tessellator.draw()
        glEnable(GL_CULL_FACE)
        glDisable(GL_BLEND)
        glDepthMask(true)
    }

}