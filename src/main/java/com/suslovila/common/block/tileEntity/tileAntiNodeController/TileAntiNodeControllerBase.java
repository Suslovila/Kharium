package com.suslovila.common.block.tileEntity.tileAntiNodeController;

import com.suslovila.utils.SUSVec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;

public class TileAntiNodeControllerBase extends TileEntity {
    public int timer = 0;
    SUSVec3 pos;
    public static ArrayList<SUSVec3> tiles = new ArrayList<>();
    public void updateEntity() {
        timer += 1;
        pos = new SUSVec3(xCoord, yCoord, zCoord);
        if (!tiles.contains(pos)) tiles.add(pos);
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
