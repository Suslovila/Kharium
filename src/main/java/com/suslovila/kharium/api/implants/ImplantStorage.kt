package com.suslovila.kharium.api.implants

import baubles.common.Baubles
import baubles.common.network.PacketHandler
import baubles.common.network.PacketSyncBauble
import com.suslovila.kharium.api.implants.ImplantType.Companion.getFirstSlotIndexOf
import com.suslovila.kharium.api.implants.ImplantType.Companion.slotAmount
import com.suslovila.kharium.common.sync.implant.PacketImplantSync
import com.suslovila.kharium.utils.Constants.NBT.TAG_COMPOUND
import cpw.mods.fml.common.network.ByteBufUtils
import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList


class ImplantStorage(
) :
    IInventory {
    val implantAmount: Int = ImplantType.slotAmount
    private val name: String = "implant_storage"
    private val stackLimit: Int = 1

    fun forEachImplant(lambda: (Int, ItemStack?) -> Unit) {
        implantsByType.forEach { implantTypeHolder ->
            implantTypeHolder.implants.forEachIndexed { indexInType, implant ->
                lambda(
                    getFirstSlotIndexOf(implantTypeHolder.implantType) + indexInType,
                    implant
                )
            }
        }
    }

    class ImplantTypeHolder(
        val implantType: ImplantType
    ) {

        val implants: Array<ItemStack?> = arrayOfNulls(implantType.slotAmount)

        companion object {
            const val TYPE_INDEX_NBT = "typeIndex"
            const val IMPLANTS_NBT = "implants"
            fun readFrom(tag: NBTTagCompound): ImplantTypeHolder {
                val type = ImplantType.values()[tag.getInteger(TYPE_INDEX_NBT)]
                val holder = ImplantTypeHolder(type)
                val implants = tag.getTagList(IMPLANTS_NBT, TAG_COMPOUND)
                for (implantIndex in 0 until implants.tagCount()) {
                    val slotNbt = implants.getCompoundTagAt(implantIndex)
                    val indexInArray = slotNbt.getInteger(SLOT_INDEX_NBT)
                    if (indexInArray >= type.slotAmount) continue
                    val implant = ItemStack.loadItemStackFromNBT(slotNbt)
                    holder.implants[indexInArray] = implant
                }
                return holder
            }

            fun readFrom(buf: ByteBuf): ImplantTypeHolder {
                val type = ImplantType.values()[buf.readInt()]
                val holder = ImplantTypeHolder(type)
                val amount = buf.readInt()
                for (implantIndex in 0 until amount) {
                    val indexInArray = buf.readInt()
                    if (indexInArray >= type.slotAmount) continue
                    val implant = ByteBufUtils.readItemStack(buf)
                    holder.implants[indexInArray] = implant
                }
                return holder
            }
        }

        fun writeTo(tag: NBTTagCompound) {
            val implantsNbt = NBTTagList()
            for (index in implants.indices) {
                val implantStack = implants[index]
                if (implantStack != null && implantStack.stackSize > 0) {
                    val slotContentNbt = NBTTagCompound()
                    slotContentNbt.setInteger(
                        SLOT_INDEX_NBT,
                        index
                    )
                    implantStack.writeToNBT(slotContentNbt)
                    implantsNbt.appendTag(slotContentNbt)
                }
            }
            tag.setInteger(TYPE_INDEX_NBT, implantType.ordinal)
            tag.setTag(IMPLANTS_NBT, implantsNbt)
        }

        fun writeTo(buf: ByteBuf) {
            buf.writeInt(implantType.ordinal)
            fun isValid(stack: ItemStack?) = stack != null && stack.stackSize > 0
            buf.writeInt(implants.count { isValid(it) })
            for (index in implants.indices) {
                val implantStack = implants[index]
                if (isValid(implantStack)) {
                    buf.writeInt(index)
                    ByteBufUtils.writeItemStack(buf, implantStack)
                }
            }
        }


    }

    val implantsByType: Array<ImplantTypeHolder> = Array(ImplantType.values().size) { typeIndex ->
        ImplantTypeHolder(ImplantType.values()[typeIndex])
    }


    override fun getSizeInventory(): Int {
        return implantAmount
    }

    override fun getStackInSlot(slotId: Int): ItemStack? {
        val possibleType = ImplantType.getTypeForSlotWithIndex(slotId) ?: return null
        val firstSlot = getFirstSlotIndexOf(possibleType)
        return implantsByType[possibleType.ordinal].implants[slotId - firstSlot]
    }

    override fun decrStackSize(slotId: Int, count: Int): ItemStack? {
        if (slotId < implantsByType.size) {
            val implant = getStackInSlot(slotId) ?: return null
            if (implant.stackSize > count) {
                val result = getStackInSlot(slotId)!!.splitStack(count)
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
        if (slotId >= implantsByType.size) {
            return
        }
        val possibleType = ImplantType.getTypeForSlotWithIndex(slotId) ?: return
        val firstSlot = getFirstSlotIndexOf(possibleType)
        implantsByType[possibleType.ordinal].implants[slotId - firstSlot] = itemstack
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
        val holders = NBTTagList()
        for (index in implantsByType.indices) {
            val holder = implantsByType[index]
            val nbtForHolder = NBTTagCompound()
            holder.writeTo(nbtForHolder)
            holders.appendTag(nbtForHolder)
        }
        rootTag.setTag(IMPLANTS_NBT, holders)
    }

    fun readFromNBT(data: NBTTagCompound) {
        if (!data.hasKey(IMPLANTS_NBT)) return
        val nbtTagList = data.getTagList(IMPLANTS_NBT, TAG_COMPOUND)
        for (j in 0 until nbtTagList.tagCount()) {
            val holderNbt = nbtTagList.getCompoundTagAt(j)
            val holder = ImplantTypeHolder.readFrom(holderNbt)
            implantsByType[holder.implantType.ordinal] = holder
        }
    }

    fun writeTo(
        buf: ByteBuf
    ) {
        for (holder in implantsByType) {
            holder.writeTo(buf)
        }
    }


    override fun getStackInSlotOnClosing(slotId: Int): ItemStack? {
        val implant = getStackInSlot(slotId) ?: return null
        setInventorySlotContents(slotId, null)
        return implant
    }

    override fun isItemValidForSlot(slotIndex: Int, itemstack: ItemStack): Boolean {
        if (slotIndex >= implantAmount) return false
        val itemType = itemstack.item
        val typeForSlot = ImplantType.getTypeForSlotWithIndex(slotIndex) ?: return false
        return typeForSlot == (itemType as? ItemImplant)?.implantType
    }

    override fun isCustomInventoryName(): Boolean {
        return false
    }

    override fun markDirty() {

    }


    companion object {
        fun readFrom(buf: ByteBuf): ImplantStorage {
            val storage = ImplantStorage()
            for (index in 0 until ImplantType.values().size) {
                val typeWithImplants = ImplantTypeHolder.readFrom(buf)
                storage.implantsByType[typeWithImplants.implantType.ordinal] = typeWithImplants
            }
            return storage
        }

        private const val IMPLANTS_NBT = "Items"
        private const val SLOT_INDEX_NBT = "index"
    }

    fun syncSlotToClients(slot: Int) {
//                PacketHandler.INSTANCE.sendToAll(PacketImplantSync(this.player.get(), slot) as IMessage)
    }
}
