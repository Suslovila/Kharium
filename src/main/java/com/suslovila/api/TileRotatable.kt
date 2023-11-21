package com.suslovila.api

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import thaumcraft.api.TileThaumcraft

open class TileRotatable : TileThaumcraft() {
     var facing : ForgeDirection = ForgeDirection.DOWN
    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        facing = ForgeDirection.getOrientation(nbttagcompound.getByte("face").toInt())
    }

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        nbttagcompound.setByte("face", facing.ordinal.toByte())
    }

    //copied fun from thaumcraft for comfort rotation
    fun rotateFromOrientation() {
        when(facing.ordinal) {

            0 -> GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f)
            1 -> GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
            //nothing to rotate for 2
            3 -> GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f)
            4 -> GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f)
            5 -> GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)

        }
    }
}
