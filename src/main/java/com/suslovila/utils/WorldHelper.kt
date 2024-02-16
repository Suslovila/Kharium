package com.suslovila.utils

import net.minecraft.tileentity.TileEntity

object WorldHelper {
    fun TileEntity.getPos() = SusVec3(this.xCoord, this.yCoord, this.zCoord)
}