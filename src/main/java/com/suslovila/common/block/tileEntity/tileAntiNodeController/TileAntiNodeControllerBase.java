package com.suslovila.common.block.tileEntity.tileAntiNodeController;

import com.suslovila.utils.SusVec3;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileAntiNodeControllerBase extends TileEntity {
    public int timer = 0;
    SusVec3 pos;
    public static ArrayList<SusVec3> tiles = new ArrayList<>();
    public void updateEntity() {
        timer += 1;
        pos = new SusVec3(xCoord, yCoord, zCoord);
        if (!tiles.contains(pos)) tiles.add(pos);
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
    }
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
}
