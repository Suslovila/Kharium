package com.suslovila.kharium.common.multiStructure.kharuContainer

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.api.fuel.IKharuHolderItem
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuContainer
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import kotlin.math.min

class TileKharuContainer() : TileDefaultMultiStructureElement(), PostRendered, IKharuContainer {
    val inventory = object : SimpleInventory(
        size = 1,
        firstOutPutSlotIndex = 1,
        name = "Kharu Container",
        stackLimit = 1
    ) {
        override fun isItemValidForSlot(index: Int, itemstack: ItemStack): Boolean =
            itemstack.item is IKharuHolderItem
    }
    override val packetId: Int = 0
    val maxLowerAmount = 10
    var timeCheck = 20

    @JvmField
    var capacity: Int = ConfigKharuContainer.basicCapacity
    var currentAmount: Int = 0
        set(value) {
            field = value.coerceIn(0, capacity)
        }
    var tick: Int = 0

    var finalisedLayerAmount = 0

    override fun updateEntity() {

        if (world.isRemote) {
            return
        }

        tick = (tick + 1) % Int.MAX_VALUE
        if (tick % timeCheck == 0) {
            handleItemInside()
            markForSaveAndSync()
        }

    }


    override fun getRenderBoundingBox(): AxisAlignedBB = INFINITE_EXTENT_AABB
    val KHARU_AMOUNT_NBT = Kharium.prefixAppender.doAndGet("kharu_amount_stored")
    val CAPACITY_NBT = Kharium.prefixAppender.doAndGet("capacity")

    val LAYER_AMOUNT_NBT = Kharium.prefixAppender.doAndGet("layer_amount")

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        nbttagcompound.setInteger(LAYER_AMOUNT_NBT, finalisedLayerAmount)
        nbttagcompound.setInteger(KHARU_AMOUNT_NBT, currentAmount)
        nbttagcompound.setInteger(CAPACITY_NBT, capacity)
        inventory.writeToNBT(nbttagcompound)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        finalisedLayerAmount = nbttagcompound.getInteger(LAYER_AMOUNT_NBT)
        currentAmount = nbttagcompound.getInteger(KHARU_AMOUNT_NBT)
        capacity = nbttagcompound.getInteger(CAPACITY_NBT)
        inventory.readFromNBT(nbttagcompound)
    }


    fun getRunes(): Array<Int> {
        val startPosition = this.getPosition() + Position(0, 1, 0)
        val runesAmount = Array(RuneType.values().size) { _ -> 0 }
        finalisedLayerAmount = 0
        for (layerIndex in 0 until maxLowerAmount) {
            val layerRunesAmount =
                getLayerRunes(startPosition + Position(0, layerIndex, 0), layerIndex) ?: return runesAmount
            finalisedLayerAmount += 1
            for (runeType in RuneType.values()) {
                runesAmount[runeType.ordinal] += layerRunesAmount[runeType.ordinal]
            }
        }
        return runesAmount
    }

    fun getLayerRunes(layerPos: Position, layerIndex: Int): Array<Int>? {
        val foundRunes = Array(RuneType.values().size) { _ -> 0 }
        for (xOffset in -layerIndex..layerIndex) {
            for (zOffset in -layerIndex..layerIndex) {
                val foundTile = world.getTile(layerPos + Position(xOffset, 0, zOffset)) ?: return null
                if (foundTile !is TileRune) return null
                foundTile.snarePos = this.getPosition()

                foundTile.ownLayerLevel = layerIndex
                foundTile.markForSaveAndSync()
                foundRunes[foundTile.runeType.ordinal] += 1
            }
        }
        return foundRunes

    }

    fun handleItemInside() {
        val stack = inventory.getStackInSlot(0) ?: return
        val kharuContainerItemClass = stack.item as? IKharuHolderItem ?: return

        val currentlyStored = kharuContainerItemClass.getStoredKharu(stack)
        val toPut = arrayListOf(
            getConduction(),
            currentAmount,
            (kharuContainerItemClass.getMaxKharuAmount(stack) - currentlyStored)
        ).min().coerceAtLeast(0)
        kharuContainerItemClass.setStoredKharu(stack,currentlyStored + toPut)
        currentAmount -= toPut

    }

    override fun postRender(event: RenderWorldLastEvent) {

    }

    override fun getStoredKharuAmount(): Int = currentAmount

    override fun getCapacity(): Int = capacity
    override fun setKharuAmount(amount: Int) {
        currentAmount = amount.coerceIn(0, capacity)
        markForSaveAndSync()
    }

    override fun takeKharu(amount: Int): Int {
        val toTake = min(amount, currentAmount)
        currentAmount -= toTake
        markForSave()

        markForSaveAndSync()
        return toTake
    }

    override fun putKharu(amount: Int): Int {
        val toPut = min(capacity - currentAmount, amount).coerceAtLeast(0)
        currentAmount += toPut

        markForSaveAndSync()
        return amount - toPut
    }

    override fun getRequiredAmount(): Int {
        return getConduction()
    }

    override fun getConduction(): Int {
        return 5
    }

}
