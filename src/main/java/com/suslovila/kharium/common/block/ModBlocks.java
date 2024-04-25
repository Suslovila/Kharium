package com.suslovila.kharium.common.block;

import com.suslovila.kharium.common.block.blockAntiNodeController.BlockAntiNodeControllerBase;
import com.suslovila.kharium.common.block.blockAntiNodeController.BlockAntiNodeStabilizer;
import com.suslovila.kharium.common.block.container.BlockAntiNode;
import com.suslovila.kharium.common.block.container.BlockAntiNodeWatcher;
import com.suslovila.kharium.common.block.container.BlockEssentiaReservoirVoid;
import com.suslovila.kharium.common.block.container.BlockKharuExtractor;
import com.suslovila.kharium.common.block.multiblocks.BlockSynthesizer;
import com.suslovila.kharium.common.block.multiblocks.tile.TileSynthesizer;
import com.suslovila.kharium.common.block.tileEntity.*;
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer;
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileKharuExtractor;
import com.suslovila.kharium.common.item.BlockEssentiaReservoirVoidItem;
import com.suslovila.kharium.common.multiStructure.kharuSnare.BlockKharuSnare;
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare;
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnareFilling;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

import static com.suslovila.kharium.Kharium.MOD_ID;

public class ModBlocks {
    public static int EssentiaReservoirVoidRI = -1;

    public static BlockAntiNode ANTI_NODE = new BlockAntiNode();
    public static BlockAntiNodeWatcher ANTI_NODE_WATCHER = new BlockAntiNodeWatcher();
    public static BlockSynthesizer synthesizer = new BlockSynthesizer("synthesizer");
    public static BlockAntiNodeStabilizer stabilizer = new BlockAntiNodeStabilizer("stabilizer");
    public static BlockAntiNodeControllerBase BLOCK_ANTI_NODE_CONTROLLER = new BlockAntiNodeControllerBase();
    public static final BlockEssentiaReservoirVoid BlockEssentiaReservoirVoid = (new BlockEssentiaReservoirVoid());
    public static BlockKharuExtractor KHARU_EXTRACTOR = new BlockKharuExtractor();
    public static BlockRestrainedGlass glass = new BlockRestrainedGlass(MOD_ID + "_restrained_glass");

    public static BlockKharuSnare KHARU_SNARE = new BlockKharuSnare("block_snare");

    public static Block blockVoidMetal = new BlockVoidMetal();

    public static void register() {

        GameRegistry.registerBlock(BlockEssentiaReservoirVoid, BlockEssentiaReservoirVoidItem.class, "EssentiaReservoirVoid");
        GameRegistry.registerBlock(ANTI_NODE, "AntiNode");
        GameRegistry.registerBlock(BLOCK_ANTI_NODE_CONTROLLER, "BlockAntiNodeControllerBase");
        GameRegistry.registerBlock(ANTI_NODE_WATCHER, "BlockAntiNodeWatcher");
//        GameRegistry.registerBlock(KHARU_SNARE, "BlockKharuExtractor");
        GameRegistry.registerBlock(blockVoidMetal,"blockVoidMetal");
//        GameRegistry.registerBlock(KHARU_SNARE,"blockKharuSnare");

        GameRegistry.registerTileEntity(TileEssentiaReservoirVoid.class, MOD_ID + "TileEssentiaReservoirVoid");
        GameRegistry.registerTileEntity(TileAntiNode.class, MOD_ID + "TileAntiNode");
        GameRegistry.registerTileEntity(TileAntiNodeControllerBase.class, MOD_ID + "TileControllerBase");
        GameRegistry.registerTileEntity(TileAntiNodeWatcher.class, MOD_ID + "TileAntiNodeWatcher");
        GameRegistry.registerTileEntity(TileSynthesizer.class, MOD_ID + "TileSynthesizer");
        GameRegistry.registerTileEntity(TileAntiNodeStabilizer.class, MOD_ID + "TileAntiNodeStabilizer");
        GameRegistry.registerTileEntity(TileKharuSnare.class, MOD_ID + "TileKharuSnare");

        GameRegistry.registerTileEntity(TileKharuExtractor.class, MOD_ID + "TileKharuExtractor");
        GameRegistry.registerTileEntity(TileRestrainedGlass.class, MOD_ID + "TileRestrainedGlass");
        GameRegistry.registerTileEntity(TileKharuSnareFilling.class, MOD_ID + "TileKharuSnareFilling");


    }

    public static void registerRender() {
        EssentiaReservoirVoidRI = RenderingRegistry.getNextAvailableRenderId();


    }
}
