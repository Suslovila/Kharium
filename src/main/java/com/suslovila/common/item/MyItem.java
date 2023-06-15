package com.suslovila.common.item;

import com.suslovila.common.MyItemInventory;
import com.suslovila.examplemod.ExampleMod;
import com.suslovila.utils.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import static com.suslovila.examplemod.ExampleMod.*;
import static org.lwjgl.opengl.GL11.*;

public class MyItem extends Item
{
    public MyItem()
    {
        super();
        // ItemStacks that store an NBT Tag Compound are limited to stack size of 1
        setMaxStackSize(1);
        // you'll want to set a creative tab as well, so you can get your item
        setCreativeTab(CreativeTabs.tabMisc);
    }

    // Without this method, your inventory will NOT work!!!
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1; // return any value greater than zero
    }
@Override
public void onUpdate(ItemStack itemStack, World world, Entity player, int variable, boolean bool) {

}

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            // If player not sneaking, open the inventory gui
            if (!player.isSneaking()) {
                player.openGui(ExampleMod.instance, ExampleMod.GUI_ITEM_INV, world, 0, 0, 0);
            }

            // Otherwise, stealthily place some diamonds in there for a nice surprise next time you open it up :)
            else {
                new MyItemInventory(itemstack).setInventorySlotContents(0, new ItemStack(Items.diamond,4));
            }
        }

        return itemstack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("inventoryitemmod:" + this.getUnlocalizedName().substring(5));
    }
    public static MovingObjectPosition raytraceBlocks(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3 playerLook = player.getLookVec();
        Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
        return world.func_147447_a(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false);
    }
}