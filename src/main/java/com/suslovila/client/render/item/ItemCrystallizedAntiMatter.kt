package com.suslovila.client.render.item

import com.suslovila.ExampleMod
import com.suslovila.mixinUtils.MixinStaticMethods.isNodeBeingTransformed
import com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import thaumcraft.api.nodes.NodeType
import thaumcraft.common.tiles.TileNode

class ItemCrystallizedAntiMatter : Item() {
    init {
        unlocalizedName = "anti_matter";
            setTextureName(ExampleMod.MOD_ID + ":anti_matter");
            setMaxStackSize(1);
    }

    override fun onItemUse(stack : ItemStack, player: EntityPlayer, world : World, x: Int, y: Int, z: Int, side : Int, p_77648_8_: Float, p_77648_9_: Float, p_77648_10_: Float): Boolean {
        val tile = world.getTileEntity(x,y,z) as? TileNode ?: return false
        if(tile.nodeType == NodeType.HUNGRY && !isNodeBeingTransformed(tile)) {
            startNodeTransformation(tile)
            --stack.stackSize
            return true
        }
        else return false
    }

}