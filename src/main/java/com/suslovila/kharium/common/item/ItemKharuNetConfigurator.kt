package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.client.gui.KhariumGui
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.KharuNetMember
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.TileNetHandler
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.sus_multi_blocked.utils.PlayerInteractionHelper.sendChatMessage
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World


object ItemKharuNetConfigurator : Item() {
    const val name = "kharu_net_configurator"

    val CURRENT_NET_HANDLER_NBT = Kharium.prefixAppender.doAndGet("net_handler")
    val CURRENT_PRIORITY_NBT = Kharium.prefixAppender.doAndGet("current_priority")

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":" + name)
        setMaxStackSize(1)
        creativeTab = Kharium.tab

    }


    override fun onItemUse(
        stack: ItemStack?,
        player: EntityPlayer?,
        world: World?,
        x: Int,
        y: Int,
        z: Int,
        side: Int,
        p_77648_8_: Float,
        p_77648_9_: Float,
        p_77648_10_: Float
    ): Boolean {
        if (stack == null || player == null || world == null || world.isRemote) return false
        val stackTag = stack.getOrCreateTag()
        val clickedPos = Position(x, y, z)

        val tile = world.getTile(clickedPos)
        if (tile is TileNetHandler) {
            val netHandlerNbt = NBTTagCompound()
            clickedPos.writeTo(netHandlerNbt)

            stackTag.setTag(CURRENT_NET_HANDLER_NBT, netHandlerNbt)

            player.sendChatMessage("current net handler now is on: $clickedPos")
        }

        if (tile is IKharuContainer) {
            if (stackTag.hasKey(CURRENT_NET_HANDLER_NBT)) {
                val handlerNbt = stackTag.getCompoundTag(CURRENT_NET_HANDLER_NBT)
                val pos = Position.readFrom(handlerNbt)
                val netHandler = world.getTile(pos) as? TileNetHandler ?: return false

                // supplier
                if (!player.isSneaking) {
                    val foundMember = netHandler.netSuppliers.firstOrNull { it.position == clickedPos }?.apply {
                        this.priority = stack.getOrCreateTag().getOrCreateInteger(CURRENT_PRIORITY_NBT, 0)
                        netHandler.markForSaveAndSync()
                        player.sendChatMessage("changed supplier priority")
                    }
                    if (foundMember == null) {
                        val success = netHandler.netSuppliers.add(
                            KharuNetMember(
                                clickedPos,
                                stack.getOrCreateTag().getOrCreateInteger(CURRENT_PRIORITY_NBT, 0)
                            )
                        )
                        if(success) {
                            netHandler.markForSaveAndSync()
                            player.sendChatMessage("added new supplier to net")

                        }
                        else {
                            player.sendChatMessage("error adding supplier to net. Maybe there is already such priority?")
                        }
                    }
                }
                // consumer
                else {
                    val foundMember = netHandler.netConsumers.firstOrNull { it.position == clickedPos }?.apply {
                        this.priority = stack.getOrCreateTag().getOrCreateInteger(CURRENT_PRIORITY_NBT, 0)
                        netHandler.markForSaveAndSync()
                        player.sendChatMessage("changed consumer priority")
                    }
                    if (foundMember == null) {
                        val success = netHandler.netConsumers.add(
                            KharuNetMember(
                                clickedPos,
                                stack.getOrCreateTag().getOrCreateInteger(CURRENT_PRIORITY_NBT, 0)
                            )
                        )
                        if(success) {
                            netHandler.markForSaveAndSync()
                            player.sendChatMessage("added new consumer to net")

                        }
                        else {
                            player.sendChatMessage("error adding consumer to net. Maybe there is already such priority?")
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onItemRightClick(itemStackIn: ItemStack?, worldIn: World?, player: EntityPlayer?): ItemStack? {
        if (itemStackIn == null || worldIn == null || player == null) return itemStackIn
            player.openGui(
                Kharium.MOD_ID,
                KhariumGui.ITEM_KHARU_NET_HANDLER.ordinal,
                worldIn,
                player.posX.toInt(),
                player.posY.toInt(),
                player.posZ.toInt()
            )
            return itemStackIn
    }


    fun getCurrentNetHandler(world: World, configurator: ItemStack): TileNetHandler? {
        val tag = configurator.getOrCreateTag()
        if(!tag.hasKey(CURRENT_NET_HANDLER_NBT)) return null
        val pos = Position.readFrom(tag.getCompoundTag(CURRENT_NET_HANDLER_NBT))

        return world.getTile(pos) as? TileNetHandler
    }
}
