package com.suslovila.kharium.utils

import com.suslovila.sus_multi_blocked.utils.Position
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
    fun TileEntity.getPosDouble() = SusVec3(this.xCoord, this.yCoord, this.zCoord)
    fun TileEntity.getPosition() = Position(this.xCoord, this.yCoord, this.zCoord)
fun Entity.getPosition() = SusVec3(this.posX, this.posY, this.posZ)

    fun World.getBlockFromPos(position: Position) = getBlock(position.x, position.y, position.z)


