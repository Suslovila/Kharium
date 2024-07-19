package com.suslovila.kharium.common.multiStructure.runeInstaller

import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.TileKharium
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

class TileRuneInstaller(
    val inventory: SimpleInventory = object : SimpleInventory(
        size = 1,
        firstOutPutSlotIndex = 1,
        name = "Rune Installer",
        stackLimit = 64
    ) {
        override fun isItemValidForSlot(index: Int, itemstack: ItemStack): Boolean =
            itemstack.item is RuneUsingItem
    }
) : TileKharium() {

    init {
        inventory.addListener(this)
    }

    override fun updateEntity() {

    }

    override fun writeCustomNBT(rootTag: NBTTagCompound) {
        super.writeCustomNBT(rootTag)
        inventory.writeToNBT(rootTag)
    }

    override fun readCustomNBT(rootTag: NBTTagCompound) {
        super.readCustomNBT(rootTag)
        inventory.readFromNBT(rootTag)
    }
}