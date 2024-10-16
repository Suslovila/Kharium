package com.suslovila.kharium.api.managment

import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.SusVec3
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent

// implemented by tile entities that must have logic with Configurator

// shift + right click - tries to bound an IConfigurable. When bound, will not be unbounded until "shift + right click" again on another block
interface IConfigurable {
    fun render(configurator: ItemStack, event: RenderWorldLastEvent)

    // (no shift + right click) on any block
    fun onBlockClick(
        stack: ItemStack,
        player: EntityPlayer,
        world: World,
        clickedPos: Position,
        side: Int,
        clickPos: SusVec3
    )

    fun onRightClick(
        itemStackIn: ItemStack, worldIn: World, player: EntityPlayer
    )
}