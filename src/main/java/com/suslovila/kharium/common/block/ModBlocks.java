package com.suslovila.kharium.common.block;

import com.suslovila.kharium.common.block.blockAntiNodeController.BlockAntiNodeControllerBase;
import com.suslovila.kharium.common.block.container.BlockAntiNode;
import com.suslovila.kharium.common.block.container.BlockAntiNodeWatcher;
import com.suslovila.kharium.common.block.container.BlockEssentiaReservoirVoid;
import com.suslovila.kharium.common.block.container.BlockKharuExtractor;
import com.suslovila.kharium.common.block.multiblocks.BlockSynthesizer;
import com.suslovila.kharium.common.block.multiblocks.tile.TileSynthesizer;
import com.suslovila.kharium.common.block.tileEntity.*;
import com.suslovila.kharium.common.block.tileEntity.rune.TileStabiliserRune;
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileKharuExtractor;
import com.suslovila.kharium.common.item.BlockEssentiaReservoirVoidItem;
import com.suslovila.kharium.common.multiStructure.TileFilling;
import com.suslovila.kharium.common.multiStructure.implantInstaller.BlockImplantInstaller;
import com.suslovila.kharium.common.multiStructure.kharuContainer.BlockKharuContainer;
import com.suslovila.kharium.common.multiStructure.kharuContainer.TileKharuContainer;
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.BlockNetHandler;
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.TileNetHandler;
import com.suslovila.kharium.common.multiStructure.kharuSnare.BlockKharuSnare;
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare;
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnareContainer;
import com.suslovila.kharium.common.multiStructure.runeInstaller.BlockRuneInstaller;
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

import static com.suslovila.kharium.Kharium.MOD_ID;

public class ModBlocks {
    public static int EssentiaReservoirVoidRI = -1;

    public static BlockAntiNode ANTI_NODE = new BlockAntiNode();
    public static BlockAntiNodeWatcher ANTI_NODE_WATCHER = new BlockAntiNodeWatcher();
    public static BlockSynthesizer synthesizer = new BlockSynthesizer("synthesizer");
    public static BlockAntiNodeControllerBase BLOCK_ANTI_NODE_CONTROLLER = new BlockAntiNodeControllerBase();
    public static final BlockEssentiaReservoirVoid BlockEssentiaReservoirVoid = (new BlockEssentiaReservoirVoid());
    public static BlockKharuExtractor KHARU_EXTRACTOR = new BlockKharuExtractor();
    public static BlockRestrainedGlass glass = new BlockRestrainedGlass(MOD_ID + "_restrained_glass");

    public static BlockKharuSnare KHARU_SNARE = new BlockKharuSnare("block_snare");
    public static BlockKharuContainer KHARU_CONTAINER = new BlockKharuContainer("block_kharu_container");
    public static BlockNetHandler KHARU_NET_HANDLER = new BlockNetHandler("block_kharu_net_handler");

    public static Block blockVoidMetal = new BlockVoidMetal();

    public static Block blockImplantInstaller = new BlockImplantInstaller("implant_installer");
    public static Block blockRuneInstaller = new BlockRuneInstaller("rune_installer");

    public static BlockRune rune = new BlockRune(MOD_ID + "_block_rune");

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
        GameRegistry.registerTileEntity(TileKharuSnare.class, MOD_ID + "TileKharuSnare");

        GameRegistry.registerTileEntity(TileKharuExtractor.class, MOD_ID + "TileKharuExtractor");
        GameRegistry.registerTileEntity(TileRestrainedGlass.class, MOD_ID + "TileRestrainedGlass");

        GameRegistry.registerTileEntity(TileStabiliserRune.class, MOD_ID + "TileRuneStabilisation");
        GameRegistry.registerTileEntity(TileRuneInstaller.class, MOD_ID + "TileRuneInstaller");

        GameRegistry.registerTileEntity(TileKharuContainer.class, MOD_ID + "TileKharuContainer");

        GameRegistry.registerTileEntity(TileNetHandler.class, MOD_ID + "TileNetHandler");

        GameRegistry.registerTileEntity(TileFilling.class, MOD_ID + "TileFilling");
        GameRegistry.registerTileEntity(TileKharuSnareContainer.class, MOD_ID + "TileKharuSnareContainer");
    }

    public static void registerRender() {
        EssentiaReservoirVoidRI = RenderingRegistry.getNextAvailableRenderId();


    }
}
