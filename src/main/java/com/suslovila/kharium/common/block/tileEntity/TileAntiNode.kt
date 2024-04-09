package com.suslovila.kharium.common.block.tileEntity

import com.suslovila.kharium.api.kharu.IKharuSupplier
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import io.netty.util.internal.ConcurrentSet
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.TileThaumcraft

class TileAntiNode : TileThaumcraft(), IKharuSupplier {
    companion object {
        const val TAG_TIMER = "timer"
        const val TAG_ACTUAL_ENERGY = "actualEnergy"
        const val MAX_ENERGY = "maxEnergy"
    }


    @SideOnly(Side.CLIENT)
    var kharuTails = ConcurrentSet<KharuTail>()
    var tickExisted = 0
    var maxEnergy = 0
    var actualEnergy = 0
        set(value) {
            field = Math.min(value, maxEnergy)
        }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        tickExisted = nbttagcompound.getInteger(TAG_TIMER)
        actualEnergy = nbttagcompound.getInteger(TAG_ACTUAL_ENERGY)
        maxEnergy = nbttagcompound.getInteger(MAX_ENERGY)
    }


    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        nbttagcompound.setInteger(TAG_TIMER, tickExisted)
        nbttagcompound.setInteger(TAG_ACTUAL_ENERGY, actualEnergy)
        nbttagcompound.setInteger(MAX_ENERGY, maxEnergy)
    }


    override fun updateEntity() {
        super.updateEntity()
        tickExisted = (tickExisted + 1) % Int.MAX_VALUE
    }

    override fun takeFromItself(amount: Int): Int {
        TODO("Not yet implemented")
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
        get() = Math.min(timer * radiusChangePerFrame, maxRadius)
}
