package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod;
import com.suslovila.api.SusTileRenderer
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import com.suslovila.api.utils.SusUtils
import com.suslovila.api.utils.SusUtils.random
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.codechicken.lib.math.MathHelper
import thaumcraft.common.config.ConfigBlocks;
import kotlin.math.abs

object TileAntiNodeWatcherRenderer : SusTileRenderer<TileAntiNodeWatcher>() {
    private val mechanicalEyeModel : IModelCustom

    private val eyeModel : IModelCustom
    lateinit var  baseModel : IModelCustom
    //private val lenses = listOf<Lens>(Lens(0.5), Lens(0.5), Lens(0.5))

        val eyeTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/watcher_eye.png");
          val baseTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/watcher_base.png");

    init{
        //baseModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher_base.obj"));
        eyeModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher_eye.obj"));
        mechanicalEyeModel = AdvancedModelLoader.loadModel( ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher.obj"));
    }

    override fun render(tile: TileAntiNodeWatcher, partialTicks: Float) {
        this.renderMechanicalEye(tile, partialTicks);
    }
      private fun renderMechanicalEye(tile : TileAntiNodeWatcher, partialTicks : Float) {
          var playermp = Minecraft.getMinecraft().thePlayer
          glPushMatrix()

            UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/scanner2.png");
            //starting rotation
            glRotated(90.0, 1.0,0.0,0.0)
            glScaled(0.7,0.7,0.7)
            tile.lenses.forEach { lens ->
              with(lens) {
                  if (turningSide == 0) {
                      if (random.nextInt(120) == 50) {
                          turningSide = SusUtils.randomSign()
                          speedDelta = abs(defaultSpeedDelta)
                          spinningSpeed = 0.0
                          //if(random.nextInt(100) == 50) lens.spinningSpeed = nextDouble(1.0, 2.0)
                      }
                  }
              }
          }


          glPushMatrix()
              glScaled(0.7,8.0,0.7)
              mechanicalEyeModel.renderOnly("scanner")
          glPopMatrix()


          glTranslated(0.0, -tile.wholeLength / 2,0.0)


          tile.lenses.forEach{
              with(it) {
                  glTranslated(0.0, width / 2, 0.0)
                  glPushMatrix()

                      spinningSpeed = Math.min(spinningSpeed + speedDelta, 1.0)
                      speedDelta = Math.min(speedDelta + 0.01 * if(speedDelta > 0) 1 else -1, defaultSpeedDelta)
                      if(spinningSpeed <= 0) turningSide = 0
                      if (random.nextInt(80) == 30) speedDelta*=-1
                      if(turningSide != 0) angle = (angle + (spinningSpeed * turningSide)) % 360
                      glRotatef(angle.toFloat(), 0F, 1F, 0F)

                      for(i in 1..6) mechanicalEyeModel.renderOnly("inner$i")
                      mechanicalEyeModel.renderOnly("scanner")

                      renderOuterCrystals()

                      renderGlasses(playermp)

                  glPopMatrix()

                  glTranslated(0.0, width / 2, 0.0)
                  UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/scanner2.png");

              }
          }
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture);
        glPopMatrix()
    }

        private fun renderOuterCrystals(){
            glDepthMask(false)
            glDisable(GL_CULL_FACE)
            glDisable(GL_ALPHA_TEST)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glDisable(GL_LIGHTING)

            glColor4f(1f, 1f, 1f, 0.3f)
            for(i in 1..6) mechanicalEyeModel.renderOnly("outer$i")
            glEnable(GL_CULL_FACE)
            glEnable(GL_ALPHA_TEST)
            glDisable( GL_BLEND)
            glEnable( GL_LIGHTING)
            glDepthMask(true)

        }
    private fun renderGlasses(player : EntityPlayer){
        glDepthMask(false)
        glPushMatrix()
        glRotated(30.0, 0.0, 1.0,0.0)
        glRotated(90.0, 1.0,0.0,0.0)
        glPushMatrix()
        UtilsFX.renderQuadCenteredFromTexture(ResourceLocation("thaumcraft", "textures/models/scanscreen.png"), 2.5F, 1.0F, 1.0F, 1.0F, (190.0F + MathHelper.sin(((player.ticksExisted - player.worldObj.rand.nextInt(2)).toDouble())) * 10.0F + 10.0F).toInt(), 771, 1.0F);
        glPopMatrix()
        glRotated(180.0, 1.0,0.0,0.0)
        UtilsFX.renderQuadCenteredFromTexture(ResourceLocation("thaumcraft", "textures/models/scanscreen.png"), 2.5F, 1.0F, 1.0F, 1.0F, (190.0F + MathHelper.sin(((player.ticksExisted - player.worldObj.rand.nextInt(2)).toDouble())) * 10.0F + 10.0F).toInt(), 771, 1.0F);
        glPopMatrix()
        glDepthMask(true)

    }
    private fun renderEye(tile : TileAntiNodeWatcher, par2 : Double, par4 : Double, par6 : Double, partialTicks : Float) {
        glPushMatrix();

        glTranslated(0.0, 0.08 * Math.sin((Minecraft.getSystemTime() /500).toDouble()), 0.0);
         glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        UtilsFX.bindTexture(eyeTexture);
        val player = Minecraft.getMinecraft().thePlayer;
        val posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        val posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        val posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        val dx = posX - (tile.xCoord + 0.5);
        val dz = posZ - (tile.zCoord + 0.5);
        val xz = Math.sqrt(dx * dx + dz * dz);
        val dy = posY + player.getEyeHeight() - (tile.yCoord + 0.5);
        val angle = (Math.toDegrees(((Math.atan2(dx, dz)))));
        glRotated(angle + 180, 0.0, 1.0, 0.0);
        glRotated(Math.toDegrees(((Math.atan2(dy, xz)))), 1.0, 0.0, 0.0);
        glRotated(12 * Math.cos((Minecraft.getSystemTime() /1000).toDouble()), 0.0, 0.0, 1.0);

        eyeModel.renderAll();
        glPopMatrix();
        this.renderObsidian(tile, par2, par4, par6, partialTicks);

        UtilsFX.bindTexture(TextureMap.locationBlocksTexture);
    }
    private fun renderObsidian(te : TileAntiNodeWatcher, x : Double, y : Double, z : Double, f : Float) {
        if(this.field_147501_a.field_147553_e != null) {
            glPushMatrix();
            glTranslatef(-0.5f, -1.5f,0.65f);
            val renderBlocks = RenderBlocks();
            val t = Tessellator.instance;
            renderBlocks.setRenderBounds(BlockRenderer.W3.toDouble(), BlockRenderer.W3.toDouble(), BlockRenderer.W3.toDouble(), BlockRenderer.W13.toDouble(), ((BlockRenderer.W3 + BlockRenderer.W10).toDouble()), BlockRenderer.W4.toDouble());
            t.startDrawingQuads();
            t.setColorRGBA_F(1F, 1F, 1F, 1F);
            t.setBrightness(200);
            val icon = Blocks.glass.getIcon(1,0);
            this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
            renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon);
            t.draw();

            glPopMatrix();
        }
    }

}
