package com.suslovila.kharium.api.implants

import com.suslovila.kharium.utils.Constants
import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import java.util.*


class ImplantStorage(
) :
    IInventory {
    val size: Int = IMPLANT_TYPE.values().size
    private val name: String = "implant_storage"
    private val stackLimit: Int = 1


    val implants: Array<ItemStack?> = arrayOfNulls(size)


    override fun getSizeInventory(): Int {
        return implants.size
    }

    override fun getStackInSlot(slotId: Int): ItemStack? {
        return implants[slotId]
    }

    override fun decrStackSize(slotId: Int, count: Int): ItemStack? {
        if (slotId < implants.size) {
            val implant = implants[slotId] ?: return null
            if (implant.stackSize > count) {
                val result = implants[slotId]!!.splitStack(count)
                markDirty()
                return result
            }
            if (implant.stackSize < count) {
                return null
            }
            setInventorySlotContents(slotId, null)
            return implant
        }
        return null
    }

    override fun setInventorySlotContents(slotId: Int, itemstack: ItemStack?) {
        if (slotId >= implants.size) {
            return
        }
        implants[slotId] = itemstack
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

    fun writeToNBT(
        rootTag: NBTTagCompound
    ) {
        val slots = NBTTagList()
        for (index in implants.indices) {
            val implantStack = implants[index]
            if (implantStack != null && implantStack.stackSize > 0) {
                val slot = NBTTagCompound()
                slots.appendTag(slot)
                slot.setInteger(
                    SLOT_INDEX_NBT,
                    index
                )
                implantStack.writeToNBT(slot)
            }
        }
        rootTag.setTag(IMPLANTS_NBT, slots)
    }

    fun readFromNBT(data: NBTTagCompound) {
        if (!data.hasKey(IMPLANTS_NBT)) return
        val nbtTagList = data.getTagList(IMPLANTS_NBT, Constants.NBT.TAG_COMPOUND)
        for (j in 0 until nbtTagList.tagCount()) {
            val slotNbt = nbtTagList.getCompoundTagAt(j)
            var index = slotNbt.getInteger(SLOT_INDEX_NBT)
            if (index >= 0 && index < implants.size) {
                setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slotNbt))
            }
        }
    }

    fun writeTo(
        buf: ByteBuf
    ) {
        val realAmount = implants.count { it != null }
        buf.writeInt(realAmount)
        for (index in implants.indices) {
            val implantStack = implants[index]
            if (implantStack != null && implantStack.stackSize > 0) {
                buf.writeInt(index)
                ByteBufUtils.writeItemStack(buf, implantStack);
            }
        }
    }

    fun readFrom(buf: ByteBuf) {
        val amount = buf.readInt()
        for (j in 0 until amount) {
            var index = buf.readInt()
            setInventorySlotContents(index, ByteBufUtils.readItemStack(buf))
        }
    }

    override fun getStackInSlotOnClosing(slotId: Int): ItemStack? {
        if (implants[slotId] == null) {
            return null
        }
        val stackToTake = implants[slotId]
        setInventorySlotContents(slotId, null)
        return stackToTake
    }

    override fun isItemValidForSlot(index: Int, itemstack: ItemStack): Boolean {
        if (index > IMPLANT_TYPE.values().size) return false
        val itemType = itemstack.item
        return (itemType as? ItemImplant)?.implantType?.any { supportedType -> IMPLANT_TYPE.values()[index] == supportedType }
            ?: false
    }

    override fun isCustomInventoryName(): Boolean {
        return false
    }

    override fun markDirty() {

    }


    companion object {
        //        private const val
        private const val IMPLANTS_NBT = "Items"
        private const val SLOT_INDEX_NBT = "index"
    }
}
