package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.managment.IConfigurable
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.TileNetHandler
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.getTileCheckChunkLoad
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.SusVec3
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World


object ItemConfigurator : Item() {
    const val name = "configurator"

    val CURRENT_CONFIGURABLE_NBT = Kharium.prefixAppender.doAndGet("configurable")
    val CURRENT_PRIORITY_NBT = Kharium.prefixAppender.doAndGet("current_priority")
    val DIMENSION_NBT = Kharium.prefixAppender.doAndGet("dimension")

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":" + name)
        setMaxStackSize(1)
        creativeTab = Kharium.tab

    }


    override fun onItemUseFirst(
        stack: ItemStack,
        player: EntityPlayer,
        world: World,
        x: Int,
        y: Int,
        z: Int,
        side: Int,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {

        if (world.isRemote) return false
        val clickedPos = Position(x, y, z)

        val tile = world.getTile(clickedPos)
        if (player.isSneaking && tile is IConfigurable) {
            setCurrentConfigurable(world, clickedPos, stack)
        }

        (getCurrentConfigurable(world, stack))?.onBlockClick(
            stack,
            player,
            world,
            Position(x, y, z),
            side,
            SusVec3(hitX, hitY, hitZ)
        )

        return true
    }

    override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?): ItemStack? {
        if (stack == null || world == null || player == null) return stack
        (getCurrentConfigurable(world, stack))?.onRightClick(stack, world, player)
        return stack
    }


    fun setCurrentConfigurable(world: World, pos: Position, configurator: ItemStack) {
        val tag = configurator.getOrCreateTag()
        val netHandlerNbt = NBTTagCompound()
        pos.writeTo(netHandlerNbt)

        tag.setTag(CURRENT_CONFIGURABLE_NBT, netHandlerNbt)
        tag.setInteger(DIMENSION_NBT, world.provider.dimensionId)

    }

    fun getCurrentConfigurable(world: World, configurator: ItemStack): IConfigurable? {
        val tag = configurator.getOrCreateTag()
        if (!tag.hasKey(DIMENSION_NBT)) return null
        if (!tag.hasKey(CURRENT_CONFIGURABLE_NBT)) return null
        if (tag.getInteger(DIMENSION_NBT) != world.provider.dimensionId) return null
        val pos = Position.readFrom(tag.getCompoundTag(CURRENT_CONFIGURABLE_NBT))

        return world.getTileCheckChunkLoad(pos) as? IConfigurable
    }
}
