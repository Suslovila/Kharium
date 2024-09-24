package com.suslovila.kharium.common.multiStructure.javaExample;

import com.suslovila.kharium.common.block.ModBlocks;
import com.suslovila.sus_multi_blocked.api.multiblock.AdditionalData;
import net.minecraft.block.Block;

// because of some jsopn problems, any values in SynthesizerElement that are not added by json will be set to null. So if you want (for some reason) store additional data for each SynthesizerElement, ou should add it as field of
// this class
public class SynthesizerAdditionalData extends AdditionalData {
    @Override
    public Block getFillingBlock() {
        return ModBlocks.SYNTHESIZER;
    }
}