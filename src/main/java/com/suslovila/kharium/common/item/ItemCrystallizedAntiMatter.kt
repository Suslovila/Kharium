package com.suslovila.kharium.common.item

import com.suslovila.kharium.utils.config.Config
import com.suslovila.kharium.Kharium
import com.suslovila.kharium.mixinUtils.MixinStaticMethods.isNodeBeingTransformed
import com.suslovila.kharium.mixinUtils.MixinStaticMethods.startNodeTransformation
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.common.event.PrimordialExplosionHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import thaumcraft.api.nodes.NodeType
import thaumcraft.common.tiles.TileNode


class ItemCrystallizedAntiMatter : Item() {
    companion object {
        val globalOwnerName = "ownerName"
    }

    init {
        unlocalizedName = "anti_matter";
        setTextureName(Kharium.MOD_ID + ":anti_matter");
        setMaxStackSize(64);
        creativeTab = Kharium.tab

    }

    override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, p_77663_4_: Int, p_77663_5_: Boolean) {
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_)
        stack?.getOrCreateTag()?.removeTag(globalOwnerName) ?: println("why is itemStack null?")
    }

    override fun onItemUse(
        stack: ItemStack,
        player: EntityPlayer,
        world: World,
        x: Int,
        y: Int,
        z: Int,
        side: Int,
        p_77648_8_: Float,
        p_77648_9_: Float,
        p_77648_10_: Float
    ): Boolean {
        //test feature
        if (!world.isRemote) PrimordialExplosionHandler.castPrimordialExplosion(world, SusVec3(x, y, z), 40.0)


        val tile = world.getTileEntity(x, y, z) as? TileNode ?: return false
        if (tile.nodeType == NodeType.HUNGRY && !isNodeBeingTransformed(tile)) {
            startNodeTransformation(tile, stack)
            if (Config.consumeEldritchDiaryAfterUse) --stack.stackSize
            return true
        } else return false
    }

    override fun onDroppedByPlayer(stack: ItemStack, player: EntityPlayer): Boolean {
        stack.getOrCreateTag().setString(globalOwnerName, player.commandSenderName)
        return true
    }

    override fun addInformation(
        p_77624_1_: ItemStack?,
        p_77624_2_: EntityPlayer?,
        p_77624_3_: MutableList<Any?>?,
        p_77624_4_: Boolean
    ) {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_)
    }
}
