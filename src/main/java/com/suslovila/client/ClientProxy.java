package com.suslovila.client;

import com.suslovila.client.particles.antiNodeBolt.AntiNodeBolt;
import com.suslovila.client.render.ClientEventHandler;
import com.suslovila.client.render.block.BlockEssentiaReservoirVoidRenderer;
import com.suslovila.client.render.item.ItemAntiNodeRenderer;
import com.suslovila.client.render.tile.TileAntiNodeRenderer;
import com.suslovila.client.render.tile.TileEssentiaReservoirVoidRenderer;
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeControllerBaseRenderer;
import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeWatcherRenderer;
import com.suslovila.common.CommonProxy;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy implements IGuiHandler
{
    public void nodeAntiBolt(World worldObj, float x, float y, float z, float x2, float y2, float z2) {
        AntiNodeBolt bolt = new AntiNodeBolt(worldObj, (double)x, (double)y, (double)z, (double)x2, (double)y2, (double)z2, worldObj.rand.nextLong(), 10, 3.0F, 5);
        bolt.defaultFractal();
        bolt.setType(0);
        bolt.finalizeBolt();
    }
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModBlocks.registerRender();
        setupItemRenderers();
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNode.class, new TileAntiNodeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaReservoirVoid.class, new TileEssentiaReservoirVoidRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNodeControllerBase.class, new TileAntiNodeControllerBaseRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntiNodeWatcher.class, new TileAntiNodeWatcherRenderer());


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

    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
}