package com.suslovila.common.block.tileEntity.tileAntiNodeController

import com.suslovila.api.TileRotatable
import com.suslovila.api.utils.SusVec3
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import io.netty.util.internal.ConcurrentSet
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.common.util.ForgeDirection


class TileAntiNodeStabilizer : TileRotatable() {
    override fun getRenderBoundingBox() : AxisAlignedBB = INFINITE_EXTENT_AABB

    //shit code for proper render in world
    companion object{
        @SideOnly(Side.CLIENT)
        val tiles = ConcurrentSet<SusVec3>()
    }

    override fun updateEntity() {
        super.updateEntity()
        facing = ForgeDirection.NORTH
        if(worldObj.isRemote)tiles.add(SusVec3(xCoord, yCoord, zCoord))
    }
}
