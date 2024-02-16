package com.suslovila.utils

import com.suslovila.ExampleMod
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11

object RotatableHandler {
    val TAG_FACING = ExampleMod.MOD_ID + "_facing"
    fun readRotation(tag: NBTTagCompound): ForgeDirection =
        ForgeDirection.getOrientation(tag.getByte(TAG_FACING).toInt())


    fun writeRotation(tag: NBTTagCompound, facing: ForgeDirection) {
        tag.setByte(TAG_FACING, facing.ordinal.toByte())
    }
    fun rotateFromOrientation(facing: ForgeDirection) {
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

    fun getFacingVector(facing: ForgeDirection): SusVec3 = SusVec3.getVec3FromForgeDirection(facing)
}