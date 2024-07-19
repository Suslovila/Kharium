package com.suslovila.kharium.common.container

import com.suslovila.kharium.common.block.container.DefaultContainer
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerRuneInstaller(val tile: TileRuneInstaller, val player: EntityPlayer) : DefaultContainer() {

    init {
        addSlotToContainer(
            SlotRuneUsingItem(
                tile.inventory,
                0,
                232,
                80
            )
        )
        addPlayerInventory()
    }

    private fun addPlayerInventory() {
        for (playerInvRow in 0..2) {
            for (playerInvCol in 0..8) {
                addSlotToContainer(
                    Slot(
                        player.inventory,
                        playerInvCol + playerInvRow * 9 + 9,
                        161 + 18 * playerInvCol,
                        179 + playerInvRow * 18
                    )
                )
            }
        }
        for (hotbarSlot in 0..8) {
            addSlotToContainer(Slot(player.inventory, hotbarSlot, 161 + 18 * hotbarSlot, 235))
        }
    }

    override fun canInteractWith(player: EntityPlayer): Boolean {
        return true
    }
}

    class SlotRuneUsingItem(inventory: IInventory, slotIndex: Int, x: Int, y: Int) :
        Slot(inventory, slotIndex, x, y) {
        override fun isItemValid(stack: ItemStack): Boolean {
            return inventory.isItemValidForSlot(slotIndex, stack)
        }
    }

