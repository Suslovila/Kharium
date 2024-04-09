package com.suslovila.kharium.common.block.container;

import com.suslovila.kharium.common.block.tileEntity.TileAntiNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.tiles.TileNitor;
import thaumcraft.common.tiles.TileNode;
import thaumcraft.common.tiles.TileNodeEnergized;
import thaumcraft.common.tiles.TileWardingStoneFence;

public class BlockAntiNode extends BlockAiry {
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileAntiNode();
    }
}
