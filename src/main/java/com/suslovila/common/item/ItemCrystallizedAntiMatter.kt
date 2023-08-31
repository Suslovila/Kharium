package com.suslovila.common.item

import com.suslovila.Config
import com.suslovila.ExampleMod
import com.suslovila.mixinUtils.MixinStaticMethods.isNodeBeingTransformed
import com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation
import com.suslovila.api.utils.SUSUtils.getOrCreateTag
import com.suslovila.api.utils.SchemaUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.model.AdvancedModelLoader.loadModel
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.nodes.NodeType
import thaumcraft.client.lib.UtilsFX
import thaumcraft.common.tiles.TileNode


class ItemCrystallizedAntiMatter : Item() {
        companion object {
            val globalOwnerName = "ownerName"
        }
    init {
            unlocalizedName = "anti_matter";
            setTextureName(ExampleMod.MOD_ID + ":anti_matter");
            setMaxStackSize(64);
            creativeTab = ExampleMod.tab

    }

    override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, p_77663_4_: Int, p_77663_5_: Boolean) {
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_)
        stack?.getOrCreateTag()?.removeTag(globalOwnerName) ?: println("why is itemStack null?")
    }
    override fun onItemUse(stack : ItemStack, player: EntityPlayer, world : World, x: Int, y: Int, z: Int, side : Int, p_77648_8_: Float, p_77648_9_: Float, p_77648_10_: Float): Boolean {
        SchemaUtils.generate(world, x,y,z, "${ExampleMod.MOD_ID}/schematics/lab")
        val tile = world.getTileEntity(x,y,z) as? TileNode ?: return false
        if(tile.nodeType == NodeType.HUNGRY && !isNodeBeingTransformed(tile)) {
            startNodeTransformation(tile, stack)
            if(Config.consumeEldritchDiaryAfterUse) --stack.stackSize
            return true
        }
        else return false
    }

    override fun onDroppedByPlayer(stack: ItemStack, player: EntityPlayer): Boolean {
        stack.getOrCreateTag().setString(globalOwnerName, player.commandSenderName)
        return true
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
        if(type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            glTranslatef(0.5f, 0.5f, 0.5f)

            //glRotated(-90.0, 1.0, 0.0, 0.0)
            //glRotated(-90.0, 0.0, 0.0, 1.0)
        }
        if(type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) glTranslatef(0f,0.15f, 0f)
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