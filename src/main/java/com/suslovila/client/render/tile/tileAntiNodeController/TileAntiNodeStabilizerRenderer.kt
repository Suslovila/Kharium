package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.client.render.tile.SusTileRenderer
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX

class TileAntiNodeStabilizerRenderer : SusTileRenderer<TileAntiNodeStabilizer>() {
    var glassModel : IModelCustom;
    var coreModel : IModelCustom;
    companion object{
        val glassTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerGlasses.png")
        val coreTexture =  ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/stabilizerCore.png");
    }
    init{
        glassModel = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerGlasses.obj"))
        coreModel = AdvancedModelLoader.loadModel( ResourceLocation(ExampleMod.MOD_ID, "models/blocks/stabilizerCore.obj"))

    }

    override fun render(tile : TileAntiNodeStabilizer, partialTicks : Float) {
        glScaled(0.3, 0.3, 0.3)
        renderCore()
        renderGlasses()
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    private fun renderGlasses(){
        UtilsFX.bindTexture(glassTexture)
        glDepthMask(false)
        glDisable(GL_CULL_FACE)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glColor4f(1f, 1f, 1f, 1f)
        glScaled(0.999, 0.999, 0.999)
        glassModel.renderAll()

        glEnable(GL_CULL_FACE)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)
        glDepthMask(true)
    }
    private fun renderCore(){
        UtilsFX.bindTexture(coreTexture)
        glDisable(GL_CULL_FACE)
        coreModel.renderAll()
        glEnable(GL_CULL_FACE)

    }
}