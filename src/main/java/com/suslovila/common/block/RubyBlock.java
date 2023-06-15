package com.suslovila.common.block;

import com.suslovila.examplemod.ExampleMod;
import com.suslovila.examplemod.ModTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class RubyBlock extends Block {
    public RubyBlock(){
        super(Material.rock);
        setBlockName("ruby");
        setCreativeTab(ModTab.INSTANCE);
        setBlockTextureName(ExampleMod.MOD_ID + ":ruby");
    }
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ModBlocks.rubyRenderId;
    }
}
