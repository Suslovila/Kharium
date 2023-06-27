package com.suslovila.common.block;

import com.suslovila.client.render.RubyBlockRenderer;
import com.suslovila.client.render.block.RenderPoppetChest;
import com.suslovila.common.block.blockAntiNodeController.BlockAntiNodeControllerBase;
import com.suslovila.common.block.container.*;
import com.suslovila.common.block.tileEntity.SmelterTile;
import com.suslovila.common.block.tileEntity.StorageTile;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.common.item.BlockEssentiaReservoirVoidItem;
import com.suslovila.common.item.ColoredStoneBlockItem;
import com.suslovila.examplemod.ExampleMod;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
public class ModBlocks {
    public static int rubyRenderId = -1;
    public static int EssentiaReservoirVoidRI = -1;
    public static final RubyBlock RUBY = new RubyBlock();
    public static final ColoredStoneBlock COLORED_STONE = new ColoredStoneBlock();
    public static final StorageBlock CONTAINER = new StorageBlock();
    public static final SmelterBlock SMELTER_BLOCK = new SmelterBlock();

    public static BlockAntiNode ANTI_NODE = new BlockAntiNode();
    public static BlockAntiNodeControllerBase BLOCK_ANTI_NODE_CONTROLLER = new BlockAntiNodeControllerBase();
    public static final BlockEssentiaReservoirVoid BlockEssentiaReservoirVoid = (new BlockEssentiaReservoirVoid());

    public static void register(){
        GameRegistry.registerBlock(RUBY, "ruby");
        GameRegistry.registerBlock(COLORED_STONE, ColoredStoneBlockItem.class,"colored_stone");
        GameRegistry.registerBlock(CONTAINER,"storage");
        GameRegistry.registerBlock(SMELTER_BLOCK, "smelter");
        GameRegistry.registerBlock(BlockEssentiaReservoirVoid, BlockEssentiaReservoirVoidItem.class,"EssentiaReservoirVoid");
        GameRegistry.registerBlock(ANTI_NODE,"AntiNode");
        GameRegistry.registerBlock(BLOCK_ANTI_NODE_CONTROLLER,"BlockAntiNodeControllerBase");


        GameRegistry.registerTileEntity(StorageTile.class, ExampleMod.MOD_ID + ":storage");
        GameRegistry.registerTileEntity(SmelterTile.class, ExampleMod.MOD_ID + "smelter");
        GameRegistry.registerTileEntity(TileEssentiaReservoirVoid.class, ExampleMod.MOD_ID + "TileEssentiaReservoirVoid");
        GameRegistry.registerTileEntity(TileAntiNode.class, ExampleMod.MOD_ID + "TileAntiNode");
        GameRegistry.registerTileEntity(TileAntiNodeControllerBase.class, ExampleMod.MOD_ID + "TileControllerBase");

    }
    public static void registerRender(){
        rubyRenderId = RenderingRegistry.getNextAvailableRenderId();
        EssentiaReservoirVoidRI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RubyBlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SmelterTile.class, new RenderPoppetChest());


    }
}
