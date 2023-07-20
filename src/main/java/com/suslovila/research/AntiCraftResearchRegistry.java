package com.suslovila.research;

import com.suslovila.common.block.ModBlocks;
import com.suslovila.ExampleMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchPage;

public class AntiCraftResearchRegistry {
    public static void integrateResearch() {
        ResearchCategories.registerCategory("AntiCraft", new ResourceLocation(ExampleMod.MOD_ID, "textures/misc/antinode.png"), new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));
        (new AntiCraftResearchItem("GenericTheory", "AntiCraft", (new AspectList()).add(Aspect.VOID, 8).add(Aspect.WATER, 4).add(Aspect.MAGIC, 6), 0, 0, 1, new ItemStack(ModBlocks.BlockEssentiaReservoirVoid))).setSpecial().setRound().setPages(new ResearchPage[] { new ResearchPage("1"), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4"), new ResearchPage("5")
        }).setParents(new String[] {"ESSENTIARESERVOIR"
       }).registerResearchItem();

    }
}
