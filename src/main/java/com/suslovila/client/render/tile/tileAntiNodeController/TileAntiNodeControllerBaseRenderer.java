package com.suslovila.client.render.tile.tileAntiNodeController;

import com.suslovila.client.render.models.ModelAntiNodeControllerBase;
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.examplemod.ExampleMod;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelCentrifuge;
import thaumcraft.common.tiles.TileCentrifuge;
import thaumcraft.common.tiles.TileNodeStabilizer;

import static org.lwjgl.opengl.GL11.*;

public class TileAntiNodeControllerBaseRenderer extends TileEntitySpecialRenderer {
    private IModelCustom model;
    private static final ResourceLocation MODEL = new ResourceLocation(ExampleMod.MOD_ID, "models/test.obj");

    public TileAntiNodeControllerBaseRenderer() {
        this.model = AdvancedModelLoader.loadModel(MODEL);
    }
    public void renderTileEntityAt(TileAntiNodeControllerBase tile, double par2, double par4, double par6, float par8) {
        glPushMatrix();
        glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5f, (float) par6 + 0.5F);
        float radius = 4.5f;
        int maxAm = 32;
        glRotated((double) ((tile.timer) % 360), 1,0,0);
        glRotated(45, 1,0,0);

        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");

        glRotatef(90, 0,1,0);
        for(int i = 0; i < maxAm; i ++) {
            glPushMatrix();
            glRotatef((float) (360 * i) /maxAm, 0,1,0);
            glTranslatef(radius, 0,0);
            glRotatef(-90,0,1,0);
            glScalef(3,3,3);
            UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");
            model.renderAll();
            glPopMatrix();
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5f, (float) par6 + 0.5F);
        glRotated((double) ((tile.timer) % 360), 1,0,0);
        glRotatef(45, 0,0,1);



        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");
//        glScalef(2,2,2);
//        model.renderAll();
        glRotatef(90, 0,1,0);
        for(int i = 0; i < maxAm; i ++) {
            glPushMatrix();
            glRotatef((float) (360 * i) /maxAm, 0,1,0);
            glTranslatef(radius, 0,0);
            glRotatef(-90,0,1,0);
            glScalef(4,4,4);
            UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");
            model.renderAll();
            glPopMatrix();
        }
        glPopMatrix();



        glPushMatrix();
        glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5f, (float) par6 + 0.5F);
        glRotated((double) ((tile.timer) % 360), 1,0,0);
        glRotatef(45, 0,1,0);
        //float angle =  360 * System.nanoTime() % 100000;


        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");
//        glScalef(3,3,3);
//        model.renderAll();
        glRotatef(90, 0,1,0);
        for(int i = 0; i < maxAm; i ++) {
            glPushMatrix();
            glRotatef((float) (360 * i) /maxAm, 0,1,0);
            glTranslatef(radius, 0,0);
            glRotatef(-90,0,1,0);
            glScalef(5,5,5);
            UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/antinode/controller/testIm.png");
            model.renderAll();
            glPopMatrix();
        }
        glPopMatrix();
        tile.timer++;
    }
@Override
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        this.renderTileEntityAt((TileAntiNodeControllerBase)par1TileEntity, par2, par4, par6, par8);
    }
}
