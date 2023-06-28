package com.suslovila;

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
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    @Mod.Instance(MOD_ID)
    public static ExampleMod instance;

    @SidedProxy(clientSide = "com.suslovila.client.ClientProxy", serverSide = "com.suslovila.common.CommonProxy")
    public static CommonProxy proxy;

    /** This is used to keep track of GUIs that we make*/
    private static int modGuiIndex = 0;

    /** Set our custom inventory Gui index to the next available Gui index */
    public static final int GUI_ITEM_INV = modGuiIndex++;

    // ITEMS ETC.
    public static Item itemstore;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);

    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        // no renderers or entities to register, but whatever
        proxy.registerRenderers();
        // register CommonProxy as our GuiHandler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CommonProxy());
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
