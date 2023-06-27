package com.suslovila.common.block.tileEntity.tileAntiNodeController;

import net.minecraft.tileentity.TileEntity;

public class TileAntiNodeControllerBase extends TileEntity {
    public int timer = 0;
    public void updateEntity() {
        timer += 1;
    }
}
