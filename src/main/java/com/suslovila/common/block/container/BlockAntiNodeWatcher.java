package com.suslovila.common.block.container;

import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntiNodeWatcher extends BlockContainer {
    public BlockAntiNodeWatcher() {
        super(Material.rock);
    }

    public int getRenderType() {
        return -1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileAntiNodeWatcher();
    }
}
