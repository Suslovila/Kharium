package com.suslovila.common;

import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.event.FMLEventListener;
import com.suslovila.common.event.SweetMixinListener;
import com.suslovila.common.item.ModItems;
import com.suslovila.research.AntiCraftResearchRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;

import static com.suslovila.research.ACAspect.*;


public class CommonProxy
{

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new FMLEventListener());
        MinecraftForge.EVENT_BUS.register(new FMLEventListener());
        MinecraftForge.EVENT_BUS.register(new SweetMixinListener());
        ModBlocks.register();
        ModItems.register();
        initAspects();
    }
    public void nodeAntiBolt(World worldObj, float x, float y, float z, float x2, float y2, float z2) {

    }

    public void init(FMLInitializationEvent event) {

    }


    public void postInit(FMLPostInitializationEvent event) {
        initItemsAspects();
        AntiCraftResearchRegistry.integrateResearch();

    }
    public void registerRenderers() {}


}