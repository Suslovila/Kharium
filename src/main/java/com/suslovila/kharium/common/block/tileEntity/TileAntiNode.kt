package com.suslovila.kharium.common.block.tileEntity

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.rune.IRuneUsingTile
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.*
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuSnare
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import io.netty.util.internal.ConcurrentSet
import net.minecraft.nbt.NBTTagCompound

class TileAntiNode(
) : TileKharium(), IRuneUsingTile {
    override val runeFactorSustains: Array<Percentage> = defaultPercentage()
    override val runeFactorChangePerCheck: Array<Percentage> =
        Array(RuneType.values().size) { _ -> Percentage(ConfigKharuSnare.antiNodeSustainDecreasePerCheck) }


    companion object {
        val TAG_ACTUAL_ENERGY_NBT = Kharium.prefixAppender.doAndGet("actual_energy")
        val MAX_ENERGY_NBT = Kharium.prefixAppender.doAndGet("max_energy")
        val tracker = object : TimeTracker() {
            override val maxValue: Int = 20
        }
    }


    val ownCheckTime = tracker.getNext()

    var kharuTails = ConcurrentSet<KharuTail>()
    var maxEnergy = 0
    var actualEnergy = 0
        // each sustain is in percent (from 0 % up to 100 % )
        set(value) {
            field = value.coerceIn(0, maxEnergy)
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
        writeRuneInfluenceStateTo(rootNbt)

    }

    override fun readCustomNBT(rootNbt: NBTTagCompound) {
        actualEnergy = rootNbt.getInteger(TAG_ACTUAL_ENERGY_NBT)
        maxEnergy = rootNbt.getInteger(MAX_ENERGY_NBT)
        readRuneInfluenceFrom(rootNbt)
    }


    override fun updateEntity() {
        super.updateEntity()
        if (world.isRemote) return
        for (runeType in RuneType.values()) takeInfluence(runeType)
        takeInfluenceFromSnare()

    }

    fun takeInfluenceFromSnare() {
        if (isStabilised && ((world.worldTime + ownCheckTime) % tracker.maxValue) == 0L) {
            snare!!.affectAntiNode(this)
            markForSaveAndSync()
        }
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

