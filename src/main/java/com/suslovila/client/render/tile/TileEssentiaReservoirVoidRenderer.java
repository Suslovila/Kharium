package com.suslovila.client.render.tile;

import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.client.renderers.tile.TileEssentiaReservoirRenderer;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEssentiaReservoir;
@SideOnly(Side.CLIENT)
public class TileEssentiaReservoirVoidRenderer extends TileEssentiaReservoirRenderer {
    private IModelCustom model;
    private static final ResourceLocation RELAY = new ResourceLocation("thaumcraft", "textures/models/reservoir.obj");

    public TileEssentiaReservoirVoidRenderer() {
        this.model = AdvancedModelLoader.loadModel(RELAY);
    }

    public void renderTileEntityAt(TileEssentiaReservoirVoid tile, double par2, double par4, double par6, float par8) {
     int facing = tile.facing.ordinal();
       GL11.glPushMatrix();
       this.translateFromOrientation(par2, par4, par6, facing);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        UtilsFX.bindTexture("textures/models/reservoir.png");
        this.model.renderAll();
       this.renderObsidian(tile, par2, par4, par6, par8);
     GL11.glPopMatrix();
       GL11.glPushMatrix();
     GL11.glTranslated(par2, par4 - 0.5D, par6);
       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
       this.renderLiquid(tile, par2, par4, par6, par8);
      GL11.glPopMatrix();
      UtilsFX.bindTexture(TextureMap.locationBlocksTexture);

    }
    public void renderObsidian(TileEssentiaReservoirVoid te, double x, double y, double z, float f) {
        if(this.field_147501_a.field_147553_e != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.5f, -1.5f,0.65f);
            RenderBlocks renderBlocks = new RenderBlocks();
            Tessellator t = Tessellator.instance;
            renderBlocks.setRenderBounds((double) BlockRenderer.W3, (double)BlockRenderer.W3, (double)BlockRenderer.W3, (double)BlockRenderer.W13, (double)(BlockRenderer.W3 + BlockRenderer.W10), (double)BlockRenderer.W4);
            t.startDrawingQuads();
            t.setColorRGBA_F(1,1,1,1);
            t.setBrightness(200);
            IIcon icon = Blocks.obsidian.getIcon(1,0);
            this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
            renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 1D, 0.0D, icon);
            t.draw();

            GL11.glPopMatrix();
        }
    }
    public void renderLiquid(TileEssentiaReservoirVoid te, double x, double y, double z, float f) {
        if(this.field_147501_a.field_147553_e != null && te.displayAspect != null && te.essentia.visSize() != 0) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            World world = te.getWorldObj();
            RenderBlocks renderBlocks = new RenderBlocks();
            GL11.glDisable(2896);
            float level = (float)te.essentia.visSize() / (float)te.maxAmount;
            Tessellator t = Tessellator.instance;
            renderBlocks.setRenderBounds((double) BlockRenderer.W3, (double)BlockRenderer.W3, (double)BlockRenderer.W3, (double)BlockRenderer.W13, (double)(BlockRenderer.W3 + BlockRenderer.W10 * level), (double)BlockRenderer.W13);
            t.startDrawingQuads();
            t.setColorRGBA_F(te.cr, te.cg, te.cb, 0.9F);
            int bright = 200;
            t.setBrightness(200);
            IIcon icon = ((BlockJar) ConfigBlocks.blockJar).iconLiquid;
            this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture);
            renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0D, 0.5D, 0.0D, icon);
            t.draw();
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }

    private void translateFromOrientation(double x, double y, double z, int orientation) {
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
        if(orientation == 0) {
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        } else if(orientation == 1) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        } else if(orientation != 2) {
            if(orientation == 3) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else if(orientation == 4) {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            } else if(orientation == 5) {
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GL11.glTranslated(0.0D, 0.0D, -0.5D);
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        this.renderTileEntityAt((TileEssentiaReservoirVoid)par1TileEntity, par2, par4, par6, par8);
    }
}
