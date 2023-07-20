package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod;
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import com.suslovila.utils.SUSUtils.random
import com.suslovila.utils.SUSUtils.randomSign
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.config.ConfigBlocks;

 class TileAntiNodeWatcherRenderer : TileEntitySpecialRenderer() {
    private val mechanicalEyeModel : IModelCustom

    private val eyeModel : IModelCustom
    lateinit var  baseModel : IModelCustom
    private val lenses = listOf<Lens>(Lens(0.5), Lens(0.5), Lens(0.5))
     private val wholeLength = lenses.sumOf { it.width };

    companion object{
        val eyeTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/watcher_eye.png");
          val baseTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/watcher_base.png");
    }
    init{
        //baseModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher_base.obj"));
        eyeModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher_eye.obj"));
        mechanicalEyeModel = AdvancedModelLoader.loadModel( ResourceLocation("thaumcraft", "textures/models/scanner.obj"));
    }

    fun renderWatcher(tile : TileAntiNodeWatcher,par2 : Double, par4 : Double, par6 : Double, partialTicks : Float) {
        renderMechanicalEye(tile, par2, par4, par6, partialTicks);
    }


      private fun renderMechanicalEye(tile : TileAntiNodeWatcher, par2 : Double, par4 : Double, par6 : Double, partialTicks : Float) {
        glPushMatrix();
        UtilsFX.bindTexture("thaumcraft", "textures/models/scanner.png");
          //starting rotation
          glRotated(90.0, 1.0,0.0,0.0)
          lenses.forEach{lens -> if(lens.turningSide == 0 && random.nextInt(100) == 50) lens.turningSide = randomSign() }
          glTranslated(0.0, -wholeLength / 2,0.0)
          lenses.forEach{
              with(it) {
                  glTranslated(0.0, width / 2, 0.0)
                  glPushMatrix()
                  //glScaled(it.width, 1.0,1.0)
                  if (angle % 60 == 0 && random.nextBoolean()) turningSide = 0
                  angle += (timer % 360) * turningSide
                  glRotatef(angle.toFloat() / 20, 0F, 1F, 0F)
                  mechanicalEyeModel.renderAll()
                  glPopMatrix()

                  timer = (timer + 1) % 360
              }
          }
        mechanicalEyeModel.renderAll();
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture);
        glPopMatrix();
    }

    private fun renderEye(tile : TileAntiNodeWatcher, par2 : Double, par4 : Double, par6 : Double, partialTicks : Float) {
        glPushMatrix();

        glTranslated(0.0, 0.08 * Math.sin((Minecraft.getSystemTime() /500).toDouble()), 0.0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

    override fun renderTileEntityAt(par1TileEntity : TileEntity, par2 : Double, par4 : Double, par6 : Double, par8 : Float) {
        glPushMatrix();
        glTranslated(par2+0.5, par4+0.5, par6+0.5);
        this.renderWatcher(par1TileEntity as TileAntiNodeWatcher, par2, par4, par6, par8);
        glPopMatrix();
    }

}
