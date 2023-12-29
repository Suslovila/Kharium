package com.suslovila.api

import com.suslovila.api.utils.SusVec3
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import thaumcraft.api.TileThaumcraft

open class TileRotatable : TileThaumcraft() {
     var facing : ForgeDirection = ForgeDirection.EAST
    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        facing = ForgeDirection.getOrientation(nbttagcompound.getByte("facing").toInt())
    }

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        nbttagcompound.setByte("facing", facing.ordinal.toByte())
    }

    //copied fun from thaumcraft for comfort rotation
    fun rotateFromOrientation() {
        when(facing) {
            ForgeDirection.DOWN -> {}
            ForgeDirection.UP -> {
                GL11.glRotatef(180f, 1f, 0f, 0f)
            }
            ForgeDirection.NORTH -> {
                GL11.glRotatef(90f, 1f, 0f, 0f)
            }
            ForgeDirection.SOUTH -> {
                GL11.glRotatef(-90f, 1f, 0f, 0f)
            }
            ForgeDirection.WEST -> {
                GL11.glRotatef(-90f, 0f, 0f, 1f)
            }
            ForgeDirection.EAST -> {
                GL11.glRotatef(90f, 0f, 0f, 1f)
            }
            else -> {}
        }
    }


    fun getFacingVector() : SusVec3 = SusVec3.getVec3FromForgeDirection(facing)
}
