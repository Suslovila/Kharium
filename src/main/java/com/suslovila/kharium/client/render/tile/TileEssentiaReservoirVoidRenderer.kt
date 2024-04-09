package com.suslovila.kharium.client.render.tile

import com.suslovila.kharium.common.block.tileEntity.TileEssentiaReservoirVoid
import com.suslovila.kharium.utils.RotatableHandler
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX
import thaumcraft.client.renderers.block.BlockRenderer
import thaumcraft.common.blocks.BlockJar
import thaumcraft.common.config.ConfigBlocks
import thaumcraft.common.tiles.TileEssentiaReservoir

object TileEssentiaReservoirVoidRenderer : SusTileRenderer<TileEssentiaReservoirVoid>() {

    private var model: IModelCustom
    val RELAY = ResourceLocation("thaumcraft", "textures/models/reservoir.obj")
    val reservoirTexture = ResourceLocation("thaumcraft", "textures/models/reservoir.png")

    init {
        model = AdvancedModelLoader.loadModel(RELAY)
    }

    override fun render(tile: TileEssentiaReservoirVoid, partialTicks: Float) {

        GL11.glPushMatrix()
        RotatableHandler.rotateFromOrientation(tile.facing)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        UtilsFX.bindTexture(reservoirTexture)
        model.renderAll()
        renderObsidian()
        GL11.glPopMatrix()

        GL11.glPushMatrix()
        GL11.glTranslated(0.0, -0.5, 0.0)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        this.renderLiquid(tile)
        GL11.glPopMatrix()
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    private fun renderObsidian() {
        if (this.field_147501_a.field_147553_e != null) {
            GL11.glPushMatrix()
            GL11.glTranslatef(-0.5f, -1.5f, 0.65f)
            val renderBlocks = RenderBlocks()
            val tesselator = Tessellator.instance
            renderBlocks.setRenderBounds(
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W13.toDouble(),
                (BlockRenderer.W3 + BlockRenderer.W10).toDouble(),
                BlockRenderer.W4.toDouble()
            )
            tesselator.startDrawingQuads()
            tesselator.setColorRGBA_F(1f, 1f, 1f, 1f)
            tesselator.setBrightness(200)
            val icon = Blocks.obsidian.getIcon(1, 0)
            this.field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture)
            renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 1.0, 0.0, icon)
            tesselator.draw()
            GL11.glPopMatrix()
        }
    }

    private fun renderLiquid(tile: TileEssentiaReservoir) {
        if (field_147501_a.field_147553_e != null && tile.displayAspect != null && tile.essentia.visSize() != 0) {
            GL11.glPushMatrix()
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            val world = tile.worldObj
            val renderBlocks = RenderBlocks()
            GL11.glDisable(2896)
            val level = tile.essentia.visSize().toFloat() / tile.maxAmount.toFloat()
            val t = Tessellator.instance
            renderBlocks.setRenderBounds(
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W3.toDouble(),
                BlockRenderer.W13.toDouble(),
                (BlockRenderer.W3 + BlockRenderer.W10 * level).toDouble(),
                BlockRenderer.W13.toDouble()
            )
            t.startDrawingQuads()
            t.setColorRGBA_F(tile.cr, tile.cg, tile.cb, 0.9f)
            val bright = 200
            t.setBrightness(200)
            val icon = (ConfigBlocks.blockJar as BlockJar).iconLiquid
            field_147501_a.field_147553_e.bindTexture(TextureMap.locationBlocksTexture)
            renderBlocks.renderFaceYNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            renderBlocks.renderFaceYPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            renderBlocks.renderFaceZNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            renderBlocks.renderFaceZPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            renderBlocks.renderFaceXNeg(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            renderBlocks.renderFaceXPos(ConfigBlocks.blockEssentiaReservoir, 0.0, 0.5, 0.0, icon)
            t.draw()
            GL11.glEnable(2896)
            GL11.glDisable(3042)
            GL11.glPopMatrix()
        }
    }
}