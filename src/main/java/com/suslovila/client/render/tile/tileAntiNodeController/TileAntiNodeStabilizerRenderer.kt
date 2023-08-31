package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.api.SusTileRenderer
import com.suslovila.api.TileRotatable
import com.suslovila.api.utils.SUSUtils
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.mixinUtils.IFxScaleProvider
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.fx.ParticleEngine
import thaumcraft.client.fx.particles.FXEssentiaTrail
import thaumcraft.client.fx.particles.FXSmokeSpiral
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.config.Config
import java.awt.Color

@SideOnly(Side.CLIENT)
object TileAntiNodeStabilizerRenderer : SusTileRenderer<TileAntiNodeStabilizer>() {
    var glassModel : IModelCustom;
    var coreModel : IModelCustom;

        val glassTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerGlasses.png")
        val coreTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerCore.png");
    init{
        glassModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        coreModel = AdvancedModelLoader.loadModel( ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerCore.obj"))

    }

    override fun renderTileEntityAt(tile: TileEntity?, x: Double, y: Double, z: Double, ticks: Float) {
        tile?.worldObj ?: return
        glPushMatrix()

        glTranslated(x + 0.5,y + 0.5,z + 0.5)

        renderSpinningEssence(tile as TileAntiNodeStabilizer)

        //if(SUSUtils.random.nextInt(5) == 4) renderTranslatingEssence(tile)
        //renderCore()

        glPopMatrix()

        renderPlasmaWaves(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, ticks, tile)

        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)

    }
    //the reason why I do not implement "render" is drowFloatyLine method, which translates the Coord system from player to the beginning of "vis line"
    //but in RenderTileEntityAt in SusTileRender I already translate matrix - so it causes awful bugs.
    override fun render(tile: TileAntiNodeStabilizer, partialTicks: Float) {
        TODO("Not yet implemented")
    }


    fun renderGlasses(){
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
     fun renderCore(){
         glPushMatrix()

         glScaled(0.5, 0.5, 0.5)
         UtilsFX.bindTexture(coreTexture)

         glColor4f(1f, 1f, 1f, 1f)
             coreModel.renderAll()

         glPopMatrix()
     }
    private fun renderTranslatingEssence(tile : TileAntiNodeStabilizer) {
        fun spawnEssence(worldObj: World?, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double, count: Int, color: Int, scale: Float){
            val noRandomMovingParticle = object : FXEssentiaTrail(worldObj, x, y, z, x2, y2, z2, count, color, scale) {


                override fun setGravity(value: Float) {this.particleGravity = 0f}

                override fun onUpdate() { prevPosX = posX
                    this.particleMaxAge = 2
                    prevPosY = posY
                    prevPosZ = posZ
                    if (particleAge++ >= particleMaxAge) setDead()
                }

            }

            ParticleEngine.instance.addEffect(worldObj, noRandomMovingParticle)
        }

        for(x in -1 until  2 step 2) {
            spawnEssence(tile.worldObj, tile.xCoord + 0.5 + 0.3, tile.yCoord + 0.5, tile.zCoord + 0.5, tile.xCoord + 0.5 + x * 0.5, tile.yCoord + 0.5,
                tile.zCoord + 0.5, 1, SUSUtils.himilitasColor, 0.4f)
        }
        for(z in -1 until  2 step 2) {
            spawnEssence(tile.worldObj, tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5 + 0.3, tile.xCoord + 0.5, tile.yCoord + 0.5,
                tile.zCoord + 0.5 + z * 0.5, 1, SUSUtils.himilitasColor, 0.4f)
        }

    }



        private fun renderSpinningEssence(tile : TileAntiNodeStabilizer){
//        val fx = object() : FXSmokeSpiral(tile.worldObj, tile.xCoord.toDouble()+0.5, tile.yCoord.toDouble() + 0.75, tile.zCoord.toDouble()+0.5, 0.25F, SUSUtils.random.nextInt(360), (tile.yCoord)){
//            override fun renderParticle(
//                tessellator: Tessellator, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
//
//                glColor4f(1.0f, 1.0f, 1.0f, 0.66f * particleAlpha)
//                val particle = (1.0f + particleAge.toFloat() / particleMaxAge.toFloat() * 4.0f).toInt()
//                val r1 = start.toFloat() + 720.0f * ((particleAge.toFloat() + f) / particleMaxAge.toFloat())
//                val r2 = 90.0f - 180.0f * ((particleAge.toFloat() + f) / particleMaxAge.toFloat())
//                var mX = -MathHelper.sin(r1 / 180.0f * 3.1415927f) * MathHelper.cos(r2 / 180.0f * 3.1415927f)
//                var mZ = MathHelper.cos(r1 / 180.0f * 3.1415927f) * MathHelper.cos(r2 / 180.0f * 3.1415927f)
//                var mY = -MathHelper.sin(r2 / 180.0f * 3.1415927f)
//                mX = mX * radius
//                mY = mY * radius
//                mZ = mZ * radius
//                val var8 = (particle % 16).toFloat() / 16.0
//                val var9 = var8 + 0.0624375
//                val var10 = (particle / 16).toFloat() / 16.0
//                val var11 = var10 + 0.0624375
//                val var12 = 0.15f * particleScale
//                val var13 = (posX + mX.toDouble() - interpPosX).toFloat()
//                val var14 = (Math.max(posY + mY.toDouble(), (miny.toFloat() + 0.1f).toDouble()) - interpPosY).toFloat()
//                val var15 = (posZ + mZ.toDouble() - interpPosZ).toFloat()
//                val var16 = 1.0f
//                tessellator.setBrightness(getBrightnessForRender(f))
//                tessellator.setColorRGBA_F(particleRed * var16, particleGreen * var16,
//                    particleBlue * var16, 0.66f * particleAlpha)
//                tessellator.addVertexWithUV(
//                    (var13 - f1 * var12 - f4 * var12).toDouble(),
//                    (var14 - f2 * var12).toDouble(),
//                    (var15 - f3 * var12 - f5 * var12).toDouble(), var9, var11)
//                tessellator.addVertexWithUV(
//                    (var13 - f1 * var12 + f4 * var12).toDouble(),
//                    (var14 + f2 * var12).toDouble(),
//                    (var15 - f3 * var12 + f5 * var12).toDouble(), var9, var10)
//                tessellator.addVertexWithUV(
//                    (var13 + f1 * var12 + f4 * var12).toDouble(),
//                    (var14 + f2 * var12).toDouble(),
//                    (var15 + f3 * var12 + f5 * var12).toDouble(), var8, var10)
//                tessellator.addVertexWithUV(
//                    (var13 + f1 * var12 - f4 * var12).toDouble(),
//                    (var14 - f2 * var12).toDouble(),
//                    (var15 + f3 * var12 - f5 * var12).toDouble(), var8, var11)
//            }
//        }
            glPushMatrix()
            glRotatef(90f,1f,0f,0f)
       val fx = FXSmokeSpiral(tile.worldObj, tile.xCoord.toDouble()+0.5, tile.yCoord.toDouble() + 0.75, tile.zCoord.toDouble()+0.5, 0.25F, SUSUtils.random.nextInt(360), (tile.yCoord))
        (fx as? IFxScaleProvider)?.setScale(0.3f) ?: throw Exception("can't cast to Mixin Interface")
        val c = Color(SUSUtils.himilitasColor)
        fx.setRBGColorF(c.red.toFloat() / 255.0f, c.green.toFloat() / 255.0f, c.blue.toFloat() / 255.0f)
        ParticleEngine.instance.addEffect(tile.worldObj, fx)
            glPopMatrix()
    }




    //to protect your mentality, I really do not recommend to try to understand what is going here, all numbers are just f*cking picked up randomly
    //if you are not afraid, so the only difficult thing was that "vis line" path had been too wide, and I needed to squeeze it, but squeezing causes other changes, so
    //just with help of universe I got the required result

    //welcome to SH*T-CODE ZONE. ONLY SH*T-CODE INSIDE MY PROJECTS!
     fun renderPlasmaWaves(xCenter: Double, yCenter: Double, zCenter: Double, partialTicks: Float, tile : TileRotatable){
         glPushMatrix()


         val ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
         for(x in -1 until  2 step 2) {
             glPushMatrix()
             drawFloatyLine(
                 xCenter + x * 0.7, yCenter + 1.15, zCenter, xCenter + 2 * x, yCenter - 0.4, zCenter,
                 partialTicks, SUSUtils.himilitasColor, "textures/misc/wispy.png", 0.05f,
                 Math.min(ticks, 10.0f) / 10.0f, 0.3F, 1F, 2.4F, 1.7F, tile::rotateFromOrientation)
             glPopMatrix()
         }
             for(z in -1 until  2 step 2){
                 glPushMatrix()
                    drawFloatyLine(xCenter, yCenter + 1.15, zCenter + z * 0.7, xCenter, yCenter - 0.4, zCenter + 2 * z,
                        partialTicks, SUSUtils.himilitasColor,"textures/misc/wispy.png", 0.05f,
                        Math.min(ticks, 10.0f) / 10.0f, 0.3f, 1.7F, 2.4F, 1F, tile::rotateFromOrientation)
                 glPopMatrix()

                }

         glPopMatrix()
    }

    fun drawFloatyLine(
        x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double, partialTicks: Float, color: Int,
        texture: String?, speed: Float, distance: Float, width: Float, xScale : Float, yScale : Float, zScale : Float, rotationMethod : () -> Unit = {}) {

        val player = Minecraft.getMinecraft().renderViewEntity
        val iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks.toDouble()
        val iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks.toDouble()
        val iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks.toDouble()
        glTranslated(x2 - iPX, y2 - iPY, z2 - iPZ)
        val time = (System.nanoTime() / 30000000L)
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green/ 255.0f
        val b = co.blue / 255.0f
        glDepthMask(false)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)

        //test rotation just to understand what's going on
        //TODO: remove
        rotationMethod()

        val tessellator = Tessellator.instance
        val dc1x = x - x2
        val dc1y = y - y2
        val dc1z = z - z2
        UtilsFX.bindTexture(texture)
        glDisable(2884)
        tessellator.startDrawing(5)

        tessellator.setBrightness(15728880)

        val dist = MathHelper.sqrt_double(dc1x * dc1x + dc1y * dc1y + dc1z * dc1z)
        val blocks = Math.round(dist)
        val length = blocks * (Config.golemLinkQuality / 2.0f)
        val f9 = 0.0f
        val f10 = 1.0f
        var i = 0
        while (i <= length * distance) {
            val f2 = i / length
            val f3 = 1.0f - Math.abs(i - length / 2.0f) / (length / 2.0f)
            var dx = dc1x + (MathHelper.sin(((z % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.5f * f3)
            var dy = dc1y + (MathHelper.sin(((x % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.5f * f3)
            var dz = dc1z + (MathHelper.sin(((y % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.5f * f3)
            tessellator.setColorRGBA_F(r, g, b, f3)
            val f13 = (1.0f - f2) * dist - time * speed

            dx /= xScale
            dy /= yScale
            dz /= zScale

            tessellator.addVertexWithUV(dx * f2, dy * f2 - width, dz * f2, f13.toDouble(), f10.toDouble())
            tessellator.addVertexWithUV(dx * f2, dy * f2 + width, dz * f2, f13.toDouble(), f9.toDouble())
            ++i
        }
        tessellator.draw()

        tessellator.startDrawing(5)
        var var84 = 0
        while (var84 <= length * distance) {
            val f2 = var84.toFloat() / length
            val f3 = 1.0f - Math.abs(var84 - length / 2.0f) / (length / 2.0f)
            var dx = dc1x + (MathHelper.sin(((z % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality/ 2.0f) - (time % 32767.0f / 5.0f)) / 4.0).toFloat()) * 0.5f * f3)
            var dy = dc1y + (MathHelper.sin(((x % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f)- (time % 32767.0f / 5.0f)) / 3.0).toFloat()) * 0.5f * f3)
            var dz = dc1z + (MathHelper.sin(((y % 16.0 + (dist * (1.0f - f2) * Config.golemLinkQuality/ 2.0f) - (time % 32767.0f / 5.0f)) / 2.0).toFloat()) * 0.5f * f3)
            tessellator.setColorRGBA_F(r, g, b, f3)
            val f13 = (1.0f - f2) * dist - time * speed

            dx /= xScale
            dy /= yScale
            dz /= zScale

            tessellator.addVertexWithUV(dx * f2 - width, dy * f2, dz * f2, f13.toDouble(), f10.toDouble())
            tessellator.addVertexWithUV(dx * f2 + width, dy * f2, dz * f2, f13.toDouble(), f9.toDouble())
            ++var84
        }
        tessellator.draw()
        glEnable(GL_CULL_FACE)
        glDisable(GL_BLEND)
        glDepthMask(true)
    }


}