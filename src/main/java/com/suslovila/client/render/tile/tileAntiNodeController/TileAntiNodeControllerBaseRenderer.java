package com.suslovila.client.render.tile.tileAntiNodeController;

import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.ExampleMod;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

import static org.lwjgl.opengl.GL11.*;

public class TileAntiNodeControllerBaseRenderer extends TileEntitySpecialRenderer {
    //JUST A TEST CLASS FOR WORKING WITH CIRCLES
    public static IModelCustom model;
    private static final ResourceLocation MODEL = new ResourceLocation(ExampleMod.MOD_ID, "models/shieldSphere.obj");

    public TileAntiNodeControllerBaseRenderer() {
       this.model = AdvancedModelLoader.

               loadModel(MODEL);
    }
    public void renderTileEntityAt(TileAntiNodeControllerBase tile, double par2, double par4, double par6, float par8) {
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
    }
@Override
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        this.renderTileEntityAt((TileAntiNodeControllerBase)par1TileEntity, par2, par4, par6, par8);
    }
}
