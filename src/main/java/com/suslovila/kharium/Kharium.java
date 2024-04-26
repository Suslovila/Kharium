package com.suslovila.kharium;

import com.suslovila.kharium.client.ClientProxy;
import com.suslovila.kharium.common.CommonProxy;
import com.suslovila.kharium.common.crafting.RecipeRegistry;
import com.suslovila.kharium.common.ore_dicitonary.OreDictionaryRegistry;
import com.suslovila.sus_multi_blocked.utils.NbtKeyNameHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

@Mod(name = Kharium.NAME, modid = Kharium.MOD_ID, version = Kharium.VERSION, dependencies = "required-after:Thaumcraft")
public class Kharium {
    public static final String NAME = "kharium";
    public static final String MOD_ID = "kharium";
    public static final String VERSION = "1.0";

    public static final NbtKeyNameHelper prefixAppender = new NbtKeyNameHelper(MOD_ID);

    public static final CreativeTabs tab = new CreativeTabs(NAME) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.water);
        }
    };

    @Mod.Instance(MOD_ID)
    public static Kharium instance;

    @SidedProxy(clientSide = "com.suslovila.kharium.client.ClientProxy", serverSide = "com.suslovila.kharium.common.CommonProxy")
    public static CommonProxy proxy;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        OreDictionaryRegistry.init();
        RecipeRegistry.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ClientProxy());


    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

    }

}
