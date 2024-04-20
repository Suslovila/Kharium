package com.suslovila.kharium.common.ore_dicitonary;

import com.suslovila.kharium.common.block.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryRegistry {
    public static void init() {

        OreDictionary.registerOre("blockVoidMetal", new ItemStack(ModBlocks.blockVoidMetal));
    }
}
