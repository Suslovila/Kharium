package com.suslovila.kharium.common.multiStructure.javaExample;

import com.suslovila.kharium.common.multiStructure.synthesizer.TileSynthesizerAspectInput;
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructureElement;
import com.suslovila.sus_multi_blocked.api.multiblock.block.ITileMultiStructureElement;
import com.suslovila.sus_multi_blocked.utils.Position;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;


// objects of this class represent the blocks in world
class SynthesizerElement extends MultiStructureElement<SynthesizerAdditionalData> {
    // these are values from json that you have added. Note that names should be the same as in json file. When initializing, they will be automatically readen from json and
    // set here
    private final int tileEntityByMeta;
    private final int correspondingComponentIndex;

    public SynthesizerElement(int x, int y, int z, String storedBlock, int meta, int tileEntityByMeta, int correspondingComponentIndex) {
        super(x, y, z, storedBlock, meta);
        this.tileEntityByMeta = tileEntityByMeta;
        this.correspondingComponentIndex = correspondingComponentIndex;
    }

    @Override
    public void putAdditionalData() {
        this.additionalData = new SynthesizerAdditionalData();
    }


    public void construct(
            World world,
            Position masterWorldPosition,
            ForgeDirection facing,
            int angle,
            int index,
            EntityPlayer player
    ) {
        Position realPos = masterWorldPosition.plus(getRealOffset(facing, angle));

        /*
                CHECK THIS OUT! NOW USING tileEntityByMeta that was set in json file I can set proper metadata for blocks of structure! Now check out block class!
         */
        world.setBlock(realPos.getX(), realPos.getY(), realPos.getZ(), additionalData.getFillingBlock(), tileEntityByMeta, 2);
        TileEntity tileEntity = world.getTileEntity(realPos.getX(), realPos.getZ(), realPos.getZ());
        if (!(tileEntity instanceof ITileMultiStructureElement)) {
            return;
        }
        ITileMultiStructureElement tile = (ITileMultiStructureElement) tileEntity;
        tile.setMasterPos(masterWorldPosition);
        tile.setFacing(facing);
        tile.setRotationAngle(angle);
        tile.setElementIndex(index);


        Thaumcraft.proxy.blockSparkle(world, realPos.getX(), realPos.getY(), realPos.getZ(), 16736256, 5);
    }
}