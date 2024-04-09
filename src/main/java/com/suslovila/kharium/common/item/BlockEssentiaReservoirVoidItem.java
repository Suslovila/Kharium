package com.suslovila.kharium.common.item;

import com.suslovila.kharium.common.block.tileEntity.TileEssentiaReservoirVoid;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.blocks.BlockEssentiaReservoirItem;
import thaumcraft.common.tiles.TileEssentiaReservoir;

public class BlockEssentiaReservoirVoidItem extends BlockEssentiaReservoirItem {
    public BlockEssentiaReservoirVoidItem(Block par1) {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int par1) {
        return par1;
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (placed) {
            TileEssentiaReservoirVoid ts = (TileEssentiaReservoirVoid) world.getTileEntity(x, y, z);
            ts.facing = ForgeDirection.getOrientation(side).getOpposite();
            ts.markDirty();
            world.markBlockForUpdate(x, y, z);
        }

        return placed;
    }
}
