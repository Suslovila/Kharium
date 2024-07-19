/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package com.suslovila.kharium.common.block.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.Constants
import java.util.*

open class SimpleInventory(
    size: Int,
    private val firstOutPutSlotIndex: Int,
    private val name: String,
    private val stackLimit: Int
) :
    IInventory {
    val itemStacks: Array<ItemStack?>
    private val listeners = LinkedList<TileEntity>()

    init {
        itemStacks = arrayOfNulls(size)
    }

    override fun getSizeInventory(): Int {
        return itemStacks.size
    }

    override fun getStackInSlot(slotId: Int): ItemStack? {
        return itemStacks[slotId]
    }

    override fun decrStackSize(slotId: Int, count: Int): ItemStack? {
        if (slotId < itemStacks.size && itemStacks[slotId] != null) {
            if (itemStacks[slotId]!!.stackSize > count) {
                val result = itemStacks[slotId]!!.splitStack(count)
                markDirty()
                return result
            }
            if (itemStacks[slotId]!!.stackSize < count) {
                return null
            }
            val stack = itemStacks[slotId]
            setInventorySlotContents(slotId, null)
            return stack
        }
        return null
    }

    override fun setInventorySlotContents(slotId: Int, itemstack: ItemStack?) {
        if (slotId >= itemStacks.size) {
            return
        }
        itemStacks[slotId] = itemstack
        if (itemstack != null && itemstack.stackSize > this.inventoryStackLimit) {
            itemstack.stackSize = this.inventoryStackLimit
        }
        markDirty()
    }

    override fun getInventoryName(): String {
        return name
    }

    override fun getInventoryStackLimit(): Int {
        return stackLimit
    }

    override fun isUseableByPlayer(entityplayer: EntityPlayer): Boolean {
        return true
    }

    override fun openChest() {}
    override fun closeChest() {}

    fun writeToNBT(data: NBTTagCompound, tag: String? = ITEMS_NBT) {
        val slots = NBTTagList()
        for (index in itemStacks.indices) {
            if (itemStacks[index] != null && itemStacks[index]!!.stackSize > 0) {
                val slot = NBTTagCompound()
                slots.appendTag(slot)
                slot.setByte(SLOT_NBT, index.toByte())
                itemStacks[index]?.writeToNBT(slot)
            }
        }
        data.setTag(tag, slots)
    }

    fun readFromNBT(data: NBTTagCompound) {
        if (data.hasKey(ITEMS_NBT)) {
            readFromNBT(data, ITEMS_NBT)
        }
    }

    fun readFromNBT(data: NBTTagCompound, tag: String?) {
        val nbttaglist = data.getTagList(tag, Constants.NBT.TAG_COMPOUND)
        for (j in 0 until nbttaglist.tagCount()) {
            val slotNbt = nbttaglist.getCompoundTagAt(j)
            var index: Int
            index = if (slotNbt.hasKey(SLOT_INDEX_NBT)) {
                slotNbt.getInteger(SLOT_INDEX_NBT)
            } else {
                slotNbt.getByte(SLOT_NBT).toInt()
            }
            if (index >= 0 && index < itemStacks.size) {
                setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slotNbt))
            }
        }
    }

    fun addListener(listener: TileEntity) {
        listeners.add(listener)
    }

    override fun getStackInSlotOnClosing(slotId: Int): ItemStack? {
        if (itemStacks[slotId] == null) {
            return null
        }
        val stackToTake = itemStacks[slotId]
        setInventorySlotContents(slotId, null)
        return stackToTake
    }

    override fun isItemValidForSlot(index: Int, itemstack: ItemStack): Boolean {
        return index < firstOutPutSlotIndex
    }

    override fun isCustomInventoryName(): Boolean {
        return false
    }

    override fun markDirty() {
        for (handler in listeners) {
                handler.world?.markTileEntityChunkModified(handler.xCoord, handler.yCoord, handler.zCoord, handler)
            }
        }

    companion object {
        private const val ITEMS_NBT = "Items"
        private const val SLOT_NBT = "Slot"
        private const val SLOT_INDEX_NBT = "index"
    }
}
