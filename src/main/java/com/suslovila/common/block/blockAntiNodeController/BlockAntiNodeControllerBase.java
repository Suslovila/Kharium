package com.suslovila.common.block.blockAntiNodeController;

import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntiNodeControllerBase extends BlockContainer {
    public BlockAntiNodeControllerBase() {
        super(Material.iron);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileAntiNodeControllerBase();
    }
}
