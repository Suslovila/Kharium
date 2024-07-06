package com.suslovila.kharium.common.block.container

import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerKharuSnare(val inventoryPlayer: InventoryPlayer, val snare: TileKharuSnare) : DefaultContainer() {

    init {
        for (playerInvRow in 0..2) {
            for (playerInvCol in 0..8) {
                addSlotToContainer(
                    Slot(
                        inventoryPlayer,
                        playerInvCol + playerInvRow * 9 + 9,
                        44 + 18 * playerInvCol,
                        140 + playerInvRow * 18
                    )
                )
            }
        }
        for (hotbarSlot in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, hotbarSlot, 44 + 18 * hotbarSlot, 140 + 18 * 3 + 4))
        }
    }

    override fun canInteractWith(player: EntityPlayer): Boolean {
        return snare.isUseableByPlayer(player)
    }
}
