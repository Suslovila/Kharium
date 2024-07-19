package com.suslovila.kharium.common.container

import com.suslovila.kharium.api.implants.ImplantStorage
import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.api.implants.ImplantType.Companion.icon
import com.suslovila.kharium.api.implants.ImplantType.Companion.slotAmount
import com.suslovila.kharium.common.block.container.DefaultContainer
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

class ContainerImplantHolder(val player: EntityPlayer) : DefaultContainer() {

    init {
        KhariumPlayerExtendedData.get(player)?.implantStorage?.let {storage ->

            addImplantSlots(storage)
        }
        addPlayerInventory()
    }

    private fun addImplantSlots(storage: ImplantStorage) {
        val implantTypes = ImplantType.values()
        var index = 0

        var yOffset = 10
        for (implantTypeIndex in implantTypes.indices) {
            val type = implantTypes[implantTypeIndex]
            val isLeftSide = (implantTypeIndex % 2 == 0)
            val offsetForTypeSlots = oneSlotStep * type.slotAmount * (if (isLeftSide) -1 else 0)
            for (typeIndex in 0 until type.slotAmount) {
                addSlotToContainer(
                    ImplantSlot(
                        storage,
                        index++,
                        140 + (if (!isLeftSide) 200 else 0) + typeIndex * oneSlotStep + offsetForTypeSlots,
                        yOffset
                    )
                )
            }
            if (implantTypeIndex % 2 != 0) yOffset += 26
        }
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


class ImplantSlot(inventory: IInventory, slotIndex: Int, x: Int, y: Int) :
    Slot(inventory, slotIndex, x, y) {
    override fun isItemValid(stack: ItemStack): Boolean {
        return inventory.isItemValidForSlot(slotIndex, stack)
    }

    override fun getBackgroundIconIndex(): IIcon? {
        return ImplantType.getTypeForSlotWithIndex(slotIndex)?.icon
    }
}
