package com.suslovila.client.render.block;

import com.suslovila.common.block.container.BlockEssentiaReservoirVoid;
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockEssentiaReservoirRenderer;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockEssentiaReservoir;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEssentiaReservoir;

import static com.suslovila.common.block.ModBlocks.EssentiaReservoirVoidRI;

public class BlockEssentiaReservoirVoidRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler {

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
       block.setBlockBounds(W2, W2, W2, W14, W14, W14);
       renderer.setRenderBoundsFromBlock(block);
            drawFaces(renderer, block, ((BlockEssentiaReservoirVoid) block).icon, true);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEssentiaReservoirVoid(), 0.0D, 0.0D, 0.0D, 0.0F);
       //GL11.glEnable('耺');

    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        block.setBlockBounds(W2, W2, W2, W14, W14, W14);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        return true;
    }

    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    public int getRenderId() {
        return EssentiaReservoirVoidRI;
    }
}