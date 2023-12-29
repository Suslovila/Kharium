package com.suslovila.common.block.tileEntity

import com.suslovila.api.kharu.IKharuSupplier
import com.suslovila.api.utils.SusVec3
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.TileThaumcraft
import java.util.concurrent.ConcurrentHashMap

class TileAntiNode : TileThaumcraft(), IKharuSupplier {

    @SideOnly(Side.CLIENT)
    var cordsForShadows = ConcurrentHashMap<SusVec3, ArrayList<Any>>()

    var tickExisted = 0

    //I wish it would be val, but in this case it will be impossible to write it to nbt :(
    var  maxEnergy = 0

    var actualEnergy = 0
        set(value) {field = Math.min(value, maxEnergy)}


    fun calculateStabilisation(){
        //TODO: implement
    }


    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        tickExisted = nbttagcompound.getInteger("timer")
        actualEnergy = nbttagcompound.getInteger("actualEnergy")
        maxEnergy = nbttagcompound.getInteger("maxEnergy")
    }


    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        nbttagcompound.setInteger("timer", tickExisted)
        nbttagcompound.setInteger("actualEnergy", actualEnergy)
        nbttagcompound.setInteger("maxEnergy", maxEnergy)
    }


    override fun updateEntity() {
        super.updateEntity()
        tickExisted = (tickExisted + 1) % Int.MAX_VALUE
    }

    override fun takeFromItself(amount: Int): Int {
        TODO("Not yet implemented")
    }


}