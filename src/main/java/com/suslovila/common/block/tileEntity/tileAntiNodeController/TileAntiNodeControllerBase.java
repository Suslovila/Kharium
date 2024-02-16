package com.suslovila.common.block.tileEntity.tileAntiNodeController;

import com.suslovila.utils.SusVec3;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileAntiNodeControllerBase extends TileEntity {
    public int timer = 0;
    SusVec3 pos;
    public static ConcurrentSet<SusVec3> tiles = new ConcurrentSet<>();
    public void updateEntity() {
        timer += 1;
        pos = new SusVec3(xCoord, yCoord, zCoord);
        if(worldObj.isRemote) {
            tiles.add(pos);
        }
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
    }
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }
}
