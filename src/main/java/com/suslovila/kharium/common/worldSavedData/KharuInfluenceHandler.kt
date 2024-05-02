package com.suslovila.kharium.common.worldSavedData

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusVec3
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World

object KharuInfluenceHandler {
    fun getKharuLevel(world : World, position : SusVec3) {

    }
}

class KharuHotbed(
    val zone : AxisAlignedBB,
    var amount : Int
)


val ZONE_NBT = Kharium.prefixAppender.doAndGet("axisAlignedAABB")
fun AxisAlignedBB.writeTo(rootNbt : NBTTagCompound) {
    val tag = NBTTagCompound()
    tag.setDouble("minX", minX)
    tag.setDouble("minY", minX)
    tag.setDouble("minZ", minX)
    tag.setDouble("maxX", maxX)
    tag.setDouble("maxY", maxY)
    tag.setDouble("maxZ", maxZ)

    rootNbt.setTag("")
}