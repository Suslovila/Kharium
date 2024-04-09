package com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController

import com.suslovila.kharium.utils.RotatableHandler
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import io.netty.util.internal.ConcurrentSet
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.TileThaumcraft
import java.util.concurrent.ConcurrentLinkedDeque


class TileAntiNodeStabilizer : TileThaumcraft() {
    var facing: ForgeDirection = ForgeDirection.NORTH

    companion object {
        @SideOnly(Side.CLIENT)
        val tiles = ConcurrentSet<SusVec3>()
    }

    override fun updateEntity() {
        super.updateEntity()
        facing = ForgeDirection.NORTH
        if (worldObj.isRemote) {
            tiles.add(SusVec3(xCoord, yCoord, zCoord))
        }
    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        facing = RotatableHandler.readRotation(nbttagcompound)
    }

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        RotatableHandler.writeRotation(nbttagcompound, facing)
    }

    override fun getRenderBoundingBox(): AxisAlignedBB = INFINITE_EXTENT_AABB

}
