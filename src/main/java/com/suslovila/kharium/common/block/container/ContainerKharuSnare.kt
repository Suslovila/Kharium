package com.suslovila.kharium.common.block.container

import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerKharuSnare(val inventoryPlayer: InventoryPlayer, val snare: TileKharuSnare) : Container() {

    init {
        for (i1 in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, i1, 8 + i1 * 18, 181))
        }
        for (yOffset in 0..2) {
            for (xOffset in 0..8) {
                addSlotToContainer(
                    Slot(
                        inventoryPlayer,
                        xOffset + yOffset * 9 + 9,
                        8 + xOffset * 18,
                        159 - yOffset * 18
                    )
                )
            }
        }
    }

    override fun canInteractWith(player: EntityPlayer): Boolean {
        return snare.isUseableByPlayer(player)
    }

//    protected fun shiftItemStack(stackToShift: ItemStack, start: Int, end: Int): Boolean {
//        var changed = false
//        if (stackToShift.isStackable) {
//            var slotIndex = start
//            while (stackToShift.stackSize > 0 && slotIndex < end) {
//                val slot = inventorySlots[slotIndex] as Slot
//                val stackInSlot = slot.stack
//                if (stackInSlot != null && StackHelper.canStacksMerge(stackInSlot, stackToShift)) {
//                    val resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize
//                    val max = Math.min(stackToShift.maxStackSize, slot.slotStackLimit)
//                    if (resultingStackSize <= max) {
//                        stackToShift.stackSize = 0
//                        stackInSlot.stackSize = resultingStackSize
//                        slot.onSlotChanged()
//                        changed = true
//                    } else if (stackInSlot.stackSize < max) {
//                        stackToShift.stackSize -= max - stackInSlot.stackSize
//                        stackInSlot.stackSize = max
//                        slot.onSlotChanged()
//                        changed = true
//                    }
//                }
//                slotIndex++
//            }
//        }
//        if (stackToShift.stackSize > 0) {
//            var slotIndex = start
//            while (stackToShift.stackSize > 0 && slotIndex < end) {
//                val slot = inventorySlots[slotIndex] as Slot
//                var stackInSlot = slot.stack
//                if (stackInSlot == null) {
//                    val max = Math.min(stackToShift.maxStackSize, slot.slotStackLimit)
//                    stackInSlot = stackToShift.copy()
//                    stackInSlot.stackSize = Math.min(stackToShift.stackSize, max)
//                    stackToShift.stackSize -= stackInSlot.stackSize
//                    slot.putStack(stackInSlot)
//                    slot.onSlotChanged()
//                    changed = true
//                }
//                ItemStack++
//            }
//        }
//        return changed
//    }
//
//    private fun tryShiftItem(stackToShift: ItemStack, numSlots: Int): Boolean {
//        for (machineIndex in 0 until numSlots - 9 * 4) {
//            val slot = inventorySlots[machineIndex] as Slot
//            if (!slot.isItemValid(stackToShift)) {
//                continue
//            }
//            if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun transferStackInSlot(player: EntityPlayer, slotIndex: Int): ItemStack? {
//        var originalStack: ItemStack? = null
//        val slot = inventorySlots[slotIndex] as Slot?
//        val numSlots = inventorySlots.size
//        if (slot != null && slot.hasStack) {
//            val stackInSlot = slot.stack
//            originalStack = stackInSlot.copy()
//            val isPlayerInventorySlot = slotIndex >= numSlots - 9 * 4
//            val isHotBar = slotIndex >= numSlots - 9
//            if (isPlayerInventorySlot && tryShiftItem(stackInSlot, numSlots)) {
//                // NOOP
//            } else if (isPlayerInventorySlot && !isHotBar) {
//                if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
//                    return null
//                }
//            } else if (isHotBar) {
//                if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
//                    return null
//                }
//            } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
//                return null
//            }
//            slot.onSlotChange(stackInSlot, originalStack)
//            if (stackInSlot.stackSize <= 0) {
//                slot.putStack(null)
//            } else {
//                slot.onSlotChanged()
//            }
//            if (stackInSlot.stackSize == originalStack.stackSize) {
//                return null
//            }
//            slot.onPickupFromSlot(player, stackInSlot)
//        }
//        return originalStack!!
//    }
}
