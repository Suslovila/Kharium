package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.client.render.tile.SusTileRenderer
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX

class TileAntiNodeControllerBaseRenderer : SusTileRenderer<TileAntiNodeControllerBase>() {
    //JUST A TEST CLASS FOR WORKING WITH CIRCLES

    companion object {
        val MODEL = ResourceLocation(ExampleMod.MOD_ID, "models/shieldSphere.obj")
        val model: IModelCustom = AdvancedModelLoader.loadModel(MODEL)
    }
    init {
    }

    override fun render(tile: TileAntiNodeControllerBase, par8: Float) {
//        glPushMatrix();
//        glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5f, (float) par6 + 0.5F);
//        float radius = 3f;
//        int maxAm = 16;
//        //glRotated((double) ((tile.timer) % 360), 1,0,0);
//        //glRotated(45, 1,0,0);
//        //glRotated((double) ((tile.timer) % 360), 1,0,0);
//        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/field.png");
//
//        glRotatef(90, 0,1,0);
//        for(int i = 0; i < maxAm; i ++) {
//            glPushMatrix();
//            glRotatef((float) (360 * i) /maxAm, 0,1,0);
//            glTranslatef(radius, 0,0);
//            glRotatef(-90,0,1,0);
//            glScalef(5,5,5);
//            //UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testim.png");
//            model.renderAll();
//            glPopMatrix();
//        }
//        glPopMatrix();
//
////        tile.timer++;
//
//        glPushMatrix();
//       // RenderHelper.enableStandardItemLighting();
//        glEnable(GL_BLEND);
//        glDisable(GL_ALPHA_TEST);
//        glDisable(GL_LIGHTING);
//        //GL11.glDepthMask(false);
//        //GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//        //GL11.glEnable(GL11.GL_BLEND);
//        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        glAlphaFunc(516, 0.003921569F);
//        glColor4f(1,1,1,0.7f);
//        glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5f, (float) par6 + 0.5F);
//        glTranslatef(0,1,0);
//        glRotatef(tile.timer++/4, 0,1,0);
//        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/field.png");
//        glScalef(4,4,4);
//        model.renderAll();
//
//
//        glDisable(GL_BLEND);
//        glDisable(GL_ALPHA_TEST);
//        glEnable(GL_LIGHTING);
//        //GL11.glDepthMask(true);
//        glPopMatrix();
        //RenderHelper.disableStandardItemLighting();
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "testWaste/shieldSphere.png")
        val player = Minecraft.getMinecraft().thePlayer
        GL11.glPushMatrix()
        val posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * par8
        val posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * par8
        val posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * par8
        GL11.glTranslated(tile.xCoord - posX, tile.yCoord - posY, tile.zCoord - posZ)
        GL11.glDepthMask(false)
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glColor4f(0f, 0f, 1f, 1f)
        GL11.glScalef(4f, 4f, 4f)
        model.renderAll()
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDepthMask(true)
        GL11.glPopMatrix()
    }

}
