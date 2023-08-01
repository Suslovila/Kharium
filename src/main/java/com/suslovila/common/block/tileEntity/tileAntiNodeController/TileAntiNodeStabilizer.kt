package com.suslovila.common.block.tileEntity.tileAntiNodeController

import net.minecraft.util.AxisAlignedBB
import thaumcraft.api.TileThaumcraft


class TileAntiNodeStabilizer : TileThaumcraft() {
    override fun getRenderBoundingBox() : AxisAlignedBB = INFINITE_EXTENT_AABB

}
