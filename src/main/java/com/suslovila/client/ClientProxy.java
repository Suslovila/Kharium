package com.suslovila.client;

import com.suslovila.client.render.ClientEventHandler;
import com.suslovila.client.render.MyTileEntityRenderer;
import com.suslovila.client.render.block.BlockEssentiaReservoirVoidRenderer;
import com.suslovila.client.render.item.ItemAntiNodeRenderer;
import com.suslovila.client.render.tile.TileAntiNodeRenderer;
import com.suslovila.client.render.tile.TileEssentiaReservoirVoidRenderer;
import com.suslovila.common.CommonProxy;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.block.tileEntity.StorageTile;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModBlocks.registerRender();
        setupItemRenderers();
        ClientRegistry.bindTileEntitySpecialRenderer(StorageTile.class, new MyTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNode.class, new TileAntiNodeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaReservoirVoid.class, new TileEssentiaReservoirVoidRenderer());
        RenderingRegistry.registerBlockHandler(new BlockEssentiaReservoirVoidRenderer());

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    @Override
    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    private void setupItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.ANTI_NODE), new ItemAntiNodeRenderer());
    }
}