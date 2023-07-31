package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.utils.KotlinUtils
import com.suslovila.utils.SUSUtils
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX

class TileAntiNodeStabilizerRenderer : TileEntitySpecialRenderer() {
    val glassModel : IModelCustom;
    lateinit var baseModel : IModelCustom;
    companion object{
        val glassTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerGlasses.png")
        //val baseTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/watcher_base.png");
    }
    init{
        glassModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        //baseModel = AdvancedModelLoader.loadModel( ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher.obj"))

    }
    override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
        //SUSUtils.renderComplexTileEntity(x,y,z,tile as TileAntiNodeStabilizer,ticks, render(tile, ticks))
        glPushMatrix()
        glTranslated(x,y,z)
        UtilsFX.bindTexture(glassTexture)
//        glDepthMask(false)
//        glDisable(GL_CULL_FACE)
//        glDisable(GL_ALPHA_TEST)
//        glEnable(GL_BLEND)
//        glDisable(GL_LIGHTING)
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
//        glColor4f(1f, 1f, 1f, 1f)
//        glScalef(4f, 4f, 4f)
//        glassModel.renderAll()
//
//
//
//        glDisable(GL_CULL_FACE)
//        glDisable(GL_ALPHA_TEST)
//        glEnable(GL_BLEND)
//        glDisable(GL_LIGHTING)
//        glDepthMask(true)
        glPopMatrix()
    }
    fun render(tile : TileAntiNodeStabilizer, partialTicks : Float) {

        UtilsFX.bindTexture(glassTexture)
        glassModel.renderAll()
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }
}