package com.suslovila.common.block.tileEntity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class SmelterTile extends TileEntity {
    private ItemStack stack;
    public ItemStack getStack(){
        return stack;
    }

    private static final String INV_TAG = "Inventory";
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (stack != null) {
            NBTTagCompound inventoryTag = new NBTTagCompound();
            stack.writeToNBT(inventoryTag);
            nbt.setTag(INV_TAG, inventoryTag);
        }
    }

    /**
     * Данный метод вызывается при чтении данных Tile Entity из чанка. Мы не рекомендуем удалять вызов родительского метода,
     * так как это может привести к потере информации о Tile Entity во время загрузки Tile Entity.
     *
     * @param nbt данные NBT которые содержат информацию о Tile Entity.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

            NBTTagCompound inventoryTag = nbt.getCompoundTag(INV_TAG);
            stack = ItemStack.loadItemStackFromNBT(inventoryTag);
            
    }

    /**
     * Данный метод вызывается каждый игровой тик. 20 тиков = 1 секунда
     */
    @Override
    public void updateEntity() {
        if (worldObj.isRemote)System.out.println("On client: "+ getStack());
        else System.out.println("On server: "+ getStack());
        /*
         * Обязательно проверяем, что действия производятся на серверной стороне, затем проверяем, что имеется стек предмета.
         * Если игровое время в результате деления 100(5 сек) с остатком возвращает 0, то выполняется проверка на стек внутри плавильни.
         */
        if (!worldObj.isRemote && hasStack() && worldObj.getTotalWorldTime() % 100 == 0) {
            // Если предмет является блоком угольной руды, то создаём сущность предмета, которая содержит в себе стек с предметом "уголь"
            if (stack.getItem() == Item.getItemFromBlock(Blocks.coal_ore)) {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, new ItemStack(Items.coal)));
                // Не забываем удалить стек
                stack = null;
                // А также не забывает сохранить данные
                markDirty();
            }
            // Если предмет является блоком железной руды, то создаём сущность предмета, которая содержит в себе стек с предметом "железный слиток"
            else if (stack.getItem() == Item.getItemFromBlock(Blocks.iron_ore)) {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, new ItemStack(Items.iron_ingot)));
                stack = null;
                markDirty();
            }
        }
    }

    public boolean hasStack() {
        return stack != null;
    }

    public void handleInputStack(EntityPlayer player, ItemStack stack) {
        if (stack != null && !hasStack()){
            ItemStack copyStack = stack.copy();
            copyStack.stackSize = 1;
            this.stack = copyStack;
            --stack.stackSize;
            markDirty();
        }
    }
    public void markDirty() {
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        super.markDirty();
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTag);
    }
@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        this.readFromNBT(packet.func_148857_g());
        super.worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }

}