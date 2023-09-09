package com.suslovila.common.block.tileEntity

import com.suslovila.api.TileRotatable
import com.suslovila.api.kharu.IKharuTransport
import net.minecraftforge.common.util.ForgeDirection

class TileKharuTube() : TileRotatable(), IKharuTransport {
    // pos in array => side of tube (opened/closed is turned with wand)
    val sides = arrayListOf(false, false, false, false, false, false)
    override fun getKharuOutputAmount(): Int {
        TODO("Not yet implemented")
    }

    override fun requiredKharuAmount(): Int {
        TODO("Not yet implemented")
    }

    override fun getKharuAmount(): Int {
        TODO("Not yet implemented")
    }

    override fun getCapacity(): Int {
        TODO("Not yet implemented")
    }

    override fun takeFromItself(amount: Int): Int {
        TODO("Not yet implemented")
    }

    override fun putToItself(amount: Int): Int {
        TODO("Not yet implemented")
    }

    override fun isConnectable(face : ForgeDirection) = sides[face.ordinal]
    override fun canInputFrom(var1: ForgeDirection?): Boolean {
        TODO("Not yet implemented")
    }

    override fun canOutputTo(var1: ForgeDirection?): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateEntity() {
        super.updateEntity()
        for (side in sides.indices){
           val loc = ForgeDirection.getOrientation(side)

        }
    }
}
