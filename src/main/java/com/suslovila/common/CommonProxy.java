package com.suslovila.common;

import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.event.FMLEventListener;
import com.suslovila.common.event.SweetMixinListener;
import com.suslovila.ExampleMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import static com.suslovila.research.ACAspect.initAspects;


public class CommonProxy implements IGuiHandler
{
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new FMLEventListener());
        MinecraftForge.EVENT_BUS.register(new SweetMixinListener());
        ModBlocks.register();
        initAspects();
    }
    public void nodeAntiBolt(World worldObj, float x, float y, float z, float x2, float y2, float z2) {

    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
    public void registerRenderers() {}




    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        // Hooray, no 'magic' numbers - we know exactly which Gui this refers to
        if (guiId == ExampleMod.GUI_ITEM_INV)
        {
            // Use the player's held item to create the inventory
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        if (guiId == ExampleMod.GUI_ITEM_INV)
        {
            // We have to cast the new container as our custom class
            // and pass in currently held item for the inventory
        }
        return null;
    }
}