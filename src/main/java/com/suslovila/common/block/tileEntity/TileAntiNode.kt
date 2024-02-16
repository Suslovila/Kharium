package com.suslovila.common.block.tileEntity

import com.suslovila.api.kharu.IKharuSupplier
import com.suslovila.utils.SusVec3
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.TileThaumcraft
import java.util.concurrent.ConcurrentHashMap

class TileAntiNode : TileThaumcraft(), IKharuSupplier {
    companion object{
        const val TAG_TIMER = "timer"
        const val TAG_ACTUAL_ENERGY = "actualEnergy"
        const val MAX_ENERGY = "maxEnergy"

    }

    @SideOnly(Side.CLIENT)
    var cordsForShadows = ConcurrentHashMap<SusVec3, ArrayList<Any>>()

    var tickExisted = 0

    var  maxEnergy = 0

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