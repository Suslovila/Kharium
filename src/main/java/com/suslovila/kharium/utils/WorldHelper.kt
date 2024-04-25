package com.suslovila.kharium.utils

import com.suslovila.sus_multi_blocked.utils.Position
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
    fun TileEntity.getPosDouble() = SusVec3(this.xCoord, this.yCoord, this.zCoord)
    fun TileEntity.getPosition() = Position(this.xCoord, this.yCoord, this.zCoord)

    fun World.getBlockFromPos(position: Position) = getBlock(position.x, position.y, position.z)


