package com.suslovila.kharium.common.block.tileEntity

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.kharu.IKharuSupplier
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import io.netty.util.internal.ConcurrentSet
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.TileThaumcraft
import kotlin.math.abs

class TileAntiNode : TileThaumcraft(), IKharuSupplier {
    companion object {
        val TAG_ACTUAL_ENERGY_NBT = Kharium.prefixAppender.doAndGet("actualEnergy")
        val MAX_ENERGY_NBT = Kharium.prefixAppender.doAndGet("maxEnergy")
        val STABILISATION_NBT = Kharium.prefixAppender.doAndGet("stabilization")
        val CONTAINMENT_NBT = Kharium.prefixAppender.doAndGet("stabilization")
        val INSTABILITY_NBT = Kharium.prefixAppender.doAndGet("instability")
        val CONTAINMENT_DECREMENT_NBT = Kharium.prefixAppender.doAndGet("instability")
        val tracker = object : TimeTracker() {
            override val maxValue: Int = 20
        }
    }


    val ownCheckTime = tracker.getNext()

    var kharuTails = ConcurrentSet<KharuTail>()
    var maxEnergy = 0
    var actualEnergy = 0
        set(value) {
            field = value.coerceIn(0, maxEnergy)
        }
    var stabilisation = 100.0
        set(value) {
            field = value.coerceIn(0.0, 100.0)
        }
    var containmentFactor = 50.0
        set(value) {
            field = value.coerceIn(0.0, 100.0)
        }

    var instabilityDecrement = 0.0
        set(value) {
            field = value.coerceIn(0.0, 100.0)
        }
    var containmentDecrement = 0.0
        set(value) {
            field = value.coerceIn(0.0, 100.0)
        }

    // all values are set by snare. To validate actuality, we check the snare
    val isStabilised: Boolean
        get() {
            return snare != null
        }
    val snare: TileKharuSnare?
        get() {
            return (this.world?.getTile(this.getPosition() + Position(0, 8, 0)) as? TileKharuSnare)
        }

    override fun writeCustomNBT(rootNbt: NBTTagCompound) {
        rootNbt.setInteger(TAG_ACTUAL_ENERGY_NBT, actualEnergy)
        rootNbt.setInteger(MAX_ENERGY_NBT, maxEnergy)
        rootNbt.setDouble(STABILISATION_NBT, stabilisation)
        rootNbt.setDouble(CONTAINMENT_NBT, containmentFactor)
        rootNbt.setDouble(INSTABILITY_NBT, instabilityDecrement)
        rootNbt.setDouble(CONTAINMENT_DECREMENT_NBT, containmentDecrement)

    }

    override fun readCustomNBT(rootNbt: NBTTagCompound) {
        actualEnergy = rootNbt.getInteger(TAG_ACTUAL_ENERGY_NBT)
        maxEnergy = rootNbt.getInteger(MAX_ENERGY_NBT)
        stabilisation = rootNbt.getDouble(STABILISATION_NBT)
        containmentFactor = rootNbt.getDouble(CONTAINMENT_NBT)
        instabilityDecrement = rootNbt.getDouble(INSTABILITY_NBT)
        containmentFactor = rootNbt.getDouble(CONTAINMENT_DECREMENT_NBT)
    }


    override fun updateEntity() {
        super.updateEntity()
        if (world.isRemote) return
        if (isStabilised && ((world.worldTime + ownCheckTime) % tracker.maxValue) == 0L) {
            snare!!.affectAntiNode(this)
            stabilisation -= instabilityDecrement
            containmentFactor -= containmentDecrement
            markForSaveAndSync()
        }

    }

    override fun takeFromItself(amount: Int): Int {
        TODO("Not yet implemented")
    }


    open fun markForSaveAndSync() {
        markForSave()
        markForSync()
    }

    open fun markForSave() {
        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this)
    }

    open fun markForSync() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
}

class KharuTail(
    val homePos: SusVec3,
    val tailSpeed: Int,
    val maxRadius: Double,
    val radiusChangePerFrame: Double,
    var timer: Int = 0,
    val aimVec3: SusVec3
) {
    val actualRadius
        get() = (timer * radiusChangePerFrame).coerceAtMost(maxRadius)
}

abstract class TimeTracker {
    abstract val maxValue: Int
    private var time = 0
    fun getNext() = (++time) % maxValue
}
