package com.suslovila.kharium.common.multiStructure.kharuSnare

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.KharuHotbed
import com.suslovila.kharium.research.KhariumAspect
import com.suslovila.kharium.utils.Percentage
import com.suslovila.kharium.utils.TimeTracker
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuContainer
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuSnare
import com.suslovila.kharium.utils.getPosition
import com.suslovila.kharium.utils.plusAssign
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.AspectSourceHelper
import thaumcraft.common.lib.events.EssentiaHandler
import kotlin.math.min

class TileKharuSnareContainer() : TileDefaultMultiStructureElement(), IKharuContainer {
    override val packetId: Int = 0

    val tracker = object : TimeTracker() {
        override val maxValue: Int = 20
    }

    val ownCheckTime = tracker.getNext()

    @JvmField
    var capacity: Int = ConfigKharuContainer.basicCapacity
    var currentAmount: Int = 0
        set(value) {
            field = value.coerceIn(0, capacity)
        }


    val CAPACITY_NBT = Kharium.prefixAppender.doAndGet("capacity")
    val KHARU_NBT = Kharium.prefixAppender.doAndGet("currentAmount")

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        nbttagcompound.setInteger(CAPACITY_NBT, capacity)
        nbttagcompound.setInteger(KHARU_NBT, currentAmount)
    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        capacity = nbttagcompound.getInteger(CAPACITY_NBT)
        currentAmount = nbttagcompound.getInteger(KHARU_NBT)

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
