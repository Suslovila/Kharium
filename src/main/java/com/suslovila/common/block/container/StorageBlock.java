package com.suslovila.common.block.container;

import com.suslovila.common.block.tileEntity.StorageTile;
import com.suslovila.examplemod.ExampleMod;
import com.suslovila.examplemod.ModTab;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class StorageBlock extends BlockContainer {
    public StorageBlock() {
        super(Material.wood);
        setBlockName("storage");
        setBlockTextureName(ExampleMod.MOD_ID + ":storage");
        setCreativeTab(ModTab.INSTANCE);
    }

    /**
     * Данный метод отвечает за создание Tile Entity.
     *
     * @param world     мир в котором расположен блок.
     * @param metadata  метаданные блока
     * @return Возвращает новый экземпляр Tile Entity
     */
    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new StorageTile();
    }
    /**
     * Вызывается когда игрок, нажимает по блоку правой кнопкой мыши.
     *
     * @param world         мир в котором установлен блок.
     * @param x             позиция блока по X координате.
     * @param y             позиция блока по Y координате.
     * @param z             позиция блока по Z координате.
     * @param activator     игрок, который взаимодействует с блоком.
     * @param side          сторона блока по которой было произведён клик.
     * @param hitX          позицию на блоке, на которой производилось нажатие по X координате.
     * @param hitY          позицию на блоке, на которой производилось нажатие по Y координате.
     * @param hitZ          позицию на блоке, на которой производилось нажатие по Z координате.
     * @return Возвращает true/false если блок был активирован. Если блок не был активирован(false), то если в руке находился блок, он будет установлен в мире.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer activator, int side, float hitX, float hitY, float hitZ) {
        // Все логические операции должны выполняться на серверной стороне, иначе могут возникнуть ошибки с обработкой и хранением данных.
        if (!world.isRemote) {
            // Получаем Tile Entity по координатам блока
            TileEntity tile = world.getTileEntity(x, y, z);

            // Проверяем, что полученный Tile Entity является StorageTile
            if (tile instanceof StorageTile) {
                StorageTile storage = (StorageTile) tile;
                // Передаём объект игрока и предмет из его руки в метод обработки стека.
                storage.handleInputStack(activator, activator.getHeldItem());
            }
        }
        return true;
    }
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

    @Override
    public int getRenderType() {
        return -1;
    }
}