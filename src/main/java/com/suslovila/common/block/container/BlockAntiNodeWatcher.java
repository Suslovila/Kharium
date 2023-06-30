package com.suslovila.common.block.container;

import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntiNodeWatcher extends BlockContainer {
    protected BlockAntiNodeWatcher() {
        super(Material.rock);
    }
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer activator, int side, float hitX, float hitY, float hitZ) {
        // Все логические операции должны выполняться на серверной стороне, иначе могут возникнуть ошибки с обработкой и хранением данных.
        if (!world.isRemote) {
            // Получаем Tile Entity по координатам блока
            TileEntity tile = world.getTileEntity(x, y, z);

            // Проверяем, что полученный Tile Entity является StorageTile
//            if (tile instanceof TileAntiNodeWatcher && activator.getHeldItem() instanceof ) {
//                TileAntiNodeWatcher storage = (TileAntiNodeWatcher) tile;
//                // Передаём объект игрока и предмет из его руки в метод обработки стека.
//                storage.handleInputStack(activator, activator.getHeldItem());
//
//
//
//            }
        }
        return true;
    }
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }
}
