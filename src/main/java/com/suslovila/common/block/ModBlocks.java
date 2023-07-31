package com.suslovila.common.block;

import com.suslovila.ExampleMod;
import com.suslovila.common.block.blockAntiNodeController.BlockAntiNodeControllerBase;
import com.suslovila.common.block.container.BlockAntiNode;
import com.suslovila.common.block.container.BlockAntiNodeWatcher;
import com.suslovila.common.block.container.BlockEssentiaReservoirVoid;
import com.suslovila.common.block.multiblocks.BlockSynthesizer;
import com.suslovila.common.block.multiblocks.tile.TileSynthesizer;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.common.item.BlockEssentiaReservoirVoidItem;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import static com.suslovila.ExampleMod.MOD_ID;

public class ModBlocks {
    public static int EssentiaReservoirVoidRI = -1;
    public static BlockAntiNode ANTI_NODE = new BlockAntiNode();
    public static BlockAntiNodeWatcher ANTI_NODE_WATCHER = new BlockAntiNodeWatcher();
    public static BlockSynthesizer synthesizer = new BlockSynthesizer("pizda");
    public static BlockAntiNodeControllerBase BLOCK_ANTI_NODE_CONTROLLER = new BlockAntiNodeControllerBase();
    public static final BlockEssentiaReservoirVoid BlockEssentiaReservoirVoid = (new BlockEssentiaReservoirVoid());

    public static void register() {

        GameRegistry.registerBlock(BlockEssentiaReservoirVoid, BlockEssentiaReservoirVoidItem.class,"EssentiaReservoirVoid");
        GameRegistry.registerBlock(ANTI_NODE,"AntiNode");
        GameRegistry.registerBlock(BLOCK_ANTI_NODE_CONTROLLER,"BlockAntiNodeControllerBase");
        GameRegistry.registerBlock(ANTI_NODE_WATCHER, "BlockAntiNodeWatcher");
        

        GameRegistry.registerTileEntity(TileEssentiaReservoirVoid.class, MOD_ID + "TileEssentiaReservoirVoid");
        GameRegistry.registerTileEntity(TileAntiNode.class, MOD_ID + "TileAntiNode");
        GameRegistry.registerTileEntity(TileAntiNodeControllerBase.class, MOD_ID + "TileControllerBase");
        GameRegistry.registerTileEntity(TileAntiNodeWatcher.class, MOD_ID + "TileAntiNodeWatcher");
        GameRegistry.registerTileEntity(TileSynthesizer.class, MOD_ID + "MOD_ID");

    }

    public static void registerRender(){
        EssentiaReservoirVoidRI = RenderingRegistry.getNextAvailableRenderId();



    }
}
