package com.suslovila.common.block.tileEntity;

import com.suslovila.utils.SUSVec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileAntiNodeWatcher extends TileEntity {
 SUSVec3 antiNodePos;
@Override
    public void markDirty() {
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        super.markDirty();
    }


    public void handleInputStack(EntityPlayer player, ItemStack stack) {
//        if (stack != null && !hasStack()){
//            ItemStack copyStack = stack.copy();
//            copyStack.stackSize = 1;
//            this.stack = copyStack;
//            --stack.stackSize;
//            markDirty();
//        }
    }


}
