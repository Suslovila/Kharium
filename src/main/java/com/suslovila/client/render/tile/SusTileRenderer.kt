package com.suslovila.client.render.tile

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11.*
import java.lang.Exception

abstract class SusTileRenderer<T : TileEntity> : TileEntitySpecialRenderer() {
    override fun renderTileEntityAt(
        tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
        glPushMatrix()
        glTranslated(x,y,z)
         tile as? T ?: throw Exception("can't cast to Renderer's bound TileEntity class")
        render(tile, ticks)
        glPopMatrix()
    }
    abstract fun render(tile : T, partialTicks: Float)
}