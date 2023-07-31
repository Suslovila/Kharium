package com.suslovila.common.item

import com.suslovila.Config
import com.suslovila.ExampleMod
import com.suslovila.mixinUtils.MixinStaticMethods.isNodeBeingTransformed
import com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.AdvancedModelLoader.loadModel
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.obj.WavefrontObject
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.nodes.NodeType
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.tiles.TileNode


class ItemCrystallizedAntiMatter : Item() {

    init {
        unlocalizedName = "anti_matter";
            setTextureName(ExampleMod.MOD_ID + ":anti_matter");
            setMaxStackSize(64);
    }

    override fun onItemUse(stack : ItemStack, player: EntityPlayer, world : World, x: Int, y: Int, z: Int, side : Int, p_77648_8_: Float, p_77648_9_: Float, p_77648_10_: Float): Boolean {
        val tile = world.getTileEntity(x,y,z) as? TileNode ?: return false
        if(tile.nodeType == NodeType.HUNGRY && !isNodeBeingTransformed(tile)) {
            startNodeTransformation(tile)
            if(Config.consumeEldritchDiaryAfterUse) --stack.stackSize
            return true
        }
        else return false
    }
}


object ItemCrystallizedAntiMatterRenderer : IItemRenderer{
    val model : IModelCustom
    init {
        model = loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/items/anti_matter.obj"));
    }
    override fun handleRenderType(item: ItemStack?, type: IItemRenderer.ItemRenderType?) = true

    override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType?, item: ItemStack?, helper: IItemRenderer.ItemRendererHelper?) = true

    override fun renderItem(type: IItemRenderer.ItemRenderType, item: ItemStack?, vararg data: Any?) {
        glPushMatrix()
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/items/anti_matter.png")
        if(type == IItemRenderer.ItemRenderType.EQUIPPED) glTranslatef(0.5f, 0.5f,0.5f)
        glScalef(2f,2f,2f)
        model.renderPart("inner_inner")
        renderOuterCristal()
        glPopMatrix()
    }
    private fun renderOuterCristal(){
        glDepthMask(false)
        glDisable(GL_CULL_FACE)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glDisable(GL_LIGHTING)

        glColor4f(1f, 1f, 1f, 0.3f)
        model.renderOnly("outer_outer")
        glEnable(GL_CULL_FACE)
        glEnable(GL_ALPHA_TEST)
        glDisable( GL_BLEND)
        glEnable( GL_LIGHTING)
        glDepthMask(true)
    }
}