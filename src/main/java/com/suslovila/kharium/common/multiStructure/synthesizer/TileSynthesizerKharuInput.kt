package com.suslovila.kharium.common.multiStructure.synthesizer

import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.utils.ThaumcraftIntegrator
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuContainer
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.getTile
import scala.actors.threadpool.AtomicInteger
import thaumcraft.api.aspects.Aspect
import kotlin.math.min

class TileSynthesizerKharuInput() : TileDefaultMultiStructureElement(), IKharuContainer {
    override val packetId: Int = 0
    @JvmField
    var capacity: Int = ConfigKharuContainer.basicCapacity
    var currentAmount: Int = 0
        set(value) {
            field = value.coerceIn(0, capacity)
        }

    val connectedSynthesizerCore: TileSynthesizerCore
        get() {
            return world.getTile(this.getMasterPos()) as TileSynthesizerCore
        }

    val requiredKharuAmount: Int
        get() {
            connectedSynthesizerCore.currentProducingAspect?.let {
                return ThaumcraftIntegrator.compositionAmountToAspect[it]!!
            }
            return 0
        }

    override fun getStoredKharuAmount(): Int = currentAmount

    override fun getCapacity(): Int = capacity

    override fun setKharuAmount(amount: Int) {
        currentAmount = amount.coerceIn(0, capacity)
        markDirty()
        markForSaveAndSync()
    }

    override fun takeKharu(amount: Int): Int {
        val toTake = min(amount, currentAmount)
        currentAmount -= toTake

        markForSaveAndSync()
        markDirty()
        return toTake
    }

    override fun putKharu(amount: Int): Int {
        val toPut = min(capacity - currentAmount, amount).coerceAtLeast(0)
        currentAmount += toPut

        markForSaveAndSync()
        markDirty()
        return amount - toPut
    }

    override fun getRequiredAmount(): Int = requiredKharuAmount

    override fun getConduction(): Int = 5
}