package com.suslovila.kharium.client.render.tile.rune

import com.suslovila.kharium.client.render.tile.SusTileRenderer
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import thaumcraft.client.lib.UtilsFX

object TileRuneRenderer : SusTileRenderer<TileRune>() {
    override fun render(tile: TileRune, partialTicks: Float) {
        if (tile.snare == null || !tile.isOwnLevelFinalised) {
            UtilsFX.bindTexture(tile.cubeCoreTexture)
            SusGraphicHelper.setStandartColors()
            SusGraphicHelper.cubeModel.renderAll()
            UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
        }
    }
}