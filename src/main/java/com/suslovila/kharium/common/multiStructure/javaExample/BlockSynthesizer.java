package com.suslovila.kharium.common.multiStructure.javaExample;

import com.suslovila.kharium.Kharium;
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure;
import com.suslovila.sus_multi_blocked.api.multiblock.block.MultiStructureBlock;
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSynthesizer extends MultiStructureBlock<SynthesizerAdditionalData, SynthesizerElement> {
    private final MultiStructure<SynthesizerAdditionalData, SynthesizerElement> multiStructure;

    public BlockSynthesizer(String name) {
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setCreativeTab(Kharium.tab);
        GameRegistry.registerBlock(this, name);
        this.multiStructure = MultiStructureSynthesizer.INSTANCE;
    }

    @Override
    public MultiStructure<SynthesizerAdditionalData, SynthesizerElement> getMultiStructure() {
        return multiStructure;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        if (world.isRemote || player == null) return false;
        TileDefaultMultiStructureElement tile = (TileDefaultMultiStructureElement) world.getTileEntity(x, y, z);
        if (tile == null) return false;
        // example of using

        //        TileSynthesizerCore synthesizerCore = (TileSynthesizerCore) .getTile(world, tile.getStructureMasterPos());
//
//        if (synthesizerCore == null) return false;
//        if (!player.isSneaking()) {
//            player.openGui(Kharium.instance, GuiIds.KHARU_SNARE, world, synthesizerCore.xCoord, synthesizerCore.yCoord, synthesizerCore.zCoord);
//            return true;
//        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return MultiStructureSynthesizer.INSTANCE.possibleTilesByMeta.get(meta).get();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }
}

