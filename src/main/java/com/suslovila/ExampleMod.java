package com.suslovila;

import com.suslovila.client.ClientProxy;
import com.suslovila.common.CommonProxy;
import com.suslovila.research.AntiCraftResearchRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Mod(modid = ExampleMod.MOD_ID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(ExampleMod.MOD_ID);

    public static final String MOD_ID = "examplemod";
    public static final String VERSION = "1.0";
    

    @Mod.Instance(MOD_ID)
    public static ExampleMod instance;

    @SidedProxy(clientSide = "com.suslovila.client.ClientProxy", serverSide = "com.suslovila.common.CommonProxy")
    public static CommonProxy proxy;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        proxy.registerRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ClientProxy());


    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        AntiCraftResearchRegistry.integrateResearch();
        proxy.postInit(event);

    }
    public static NBTTagCompound getOrCreateTag(ItemStack itemStack){
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound();
    }
}
