package com.suslovila.common.block.tileEntity.tileAntiNodeController;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileAntiNodeControllerBase extends TileEntity {
    public int timer = 0;
    public void updateEntity() {
        timer += 1;
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
