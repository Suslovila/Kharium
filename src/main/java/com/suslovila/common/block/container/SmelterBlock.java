package com.suslovila.common.block.container;

import com.suslovila.common.block.tileEntity.SmelterTile;
import com.suslovila.common.block.tileEntity.StorageTile;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class SmelterBlock extends BlockContainer {
    public SmelterBlock(){
        super(Material.rock);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.51F, 1.0F);

    }
    /**
     * Указывает Minecraft, что блок является нормальным блоком для отрисовки(обычным кубом).
     *
     * @return Возвращает логическое значение.
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Указывает Minecraft, что блок является непрозрачным. Если указать true, то пространство внутри блока будет создавать
     * x-ray эффект.
     *
     * @return Возвращает логическое значение.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new SmelterTile();
    }
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer activator, int side, float hitX, float hitY, float hitZ) {
        // Все логические операции должны выполняться на серверной стороне, иначе могут возникнуть ошибки с обработкой и хранением данных.
        if (!world.isRemote) {
            // Получаем Tile Entity по координатам блока
            TileEntity tile = world.getTileEntity(x, y, z);

            // Проверяем, что полученный Tile Entity является StorageTile
            if (tile instanceof SmelterTile) {
                SmelterTile storage = (SmelterTile) tile;
                // Передаём объект игрока и предмет из его руки в метод обработки стека.
                storage.handleInputStack(activator, activator.getHeldItem());



            }
        }
        return true;
    }
}