package com.suslovila.kharium.research

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.kharium.common.item.ModItems
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import thaumcraft.api.ThaumcraftApi
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.crafting.InfusionRecipe
import thaumcraft.api.research.ResearchCategories
import thaumcraft.api.research.ResearchItem
import thaumcraft.api.research.ResearchPage
import thaumcraft.common.config.ConfigBlocks

object AntiCraftResearchRegistry {

    var recipes = HashMap<String, InfusionRecipe>()

    val researchItemChildren: HashMap<ResearchItem, ArrayList<ResearchItem>> by lazy {
        val childrenToParents = hashMapOf<ResearchItem, ArrayList<ResearchItem>>()
        for (researchCategory in ResearchCategories.researchCategories.values) {
            for (researchItem in researchCategory.research.values) {
                childrenToParents[researchItem] = arrayListOf()
            }
        }

        for (researchCategory in ResearchCategories.researchCategories.values) {
            for (researchItem in researchCategory.research.values) {
                if (researchItem.parents != null) {
                    for (parent in researchItem.parents) {
                        childrenToParents[ResearchCategories.getResearch(parent)]?.add(researchItem)
                    }
                }
                if (researchItem.parentsHidden != null) {
                    for (parent in researchItem.parentsHidden) {
                        childrenToParents[ResearchCategories.getResearch(parent)]?.add(researchItem)
                    }
                }
            }
        }

        childrenToParents
    }

    //ItemStacks
    var essentiaReservoirVoid = ItemStack(ModBlocks.BlockEssentiaReservoirVoid, 1, 0)
    fun integrateInfusion() {
        recipes["IessentiaReservoirVoid"] = ThaumcraftApi.addInfusionCraftingRecipe(
            "EssentiaReservoirVoid",
            essentiaReservoirVoid,
            0,
            AspectList().add(Aspect.FIRE, 8).add(Aspect.VOID, 8).add(Aspect.DARKNESS, 8),
            ItemStack(ConfigBlocks.blockEssentiaReservoir),
            arrayOf(ItemStack(Blocks.obsidian), ItemStack(Items.lava_bucket))
        )
    }

    val khariumCategory = "kharium"
    fun integrateResearch() {

        ResearchCategories.registerCategory(
            khariumCategory,
            ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png"),
            ResourceLocation(Kharium.MOD_ID, "textures/misc/backTexture.png")
        )

        AntiCraftResearchItem(
            "ESSENTIARESERVOIRVOID",
            khariumCategory,
            AspectList().add(Aspect.VOID, 8).add(Aspect.WATER, 4).add(Aspect.MAGIC, 6),
            0,
            0,
            1,
            ItemStack(ModBlocks.BlockEssentiaReservoirVoid)
        ).setPages(ResearchPage("1"), ResearchPage(recipes["IessentiaReservoirVoid"])).setParents("ESSENTIARESERVOIR")
            .registerResearchItem()

        AntiCraftResearchItem("DIARY", "ANTICRAFT", AspectList(), 0, 4, 0, ItemStack(ModItems.diary)).setPages(
            ResearchPage("1"),
            ResearchPage("2")
        ).setStub().setHidden().setRound().setSpecial().registerResearchItem()

        AntiCraftResearchItem(
            "CRYSTALLIZED_KHARU",
            khariumCategory,
            AspectList(),
            0,
            7,
            0,
            ItemStack(ModItems.crystallizedKharu)
        ).setPages(ResearchPage("1"), ResearchPage("2")).setStub().setHidden().setRound().setSpecial()
            .registerResearchItem()

        AntiCraftResearchItem(
            "ANTI_NODE_HIDDEN",
            khariumCategory,
            AspectList(),
            4,
            5,
            0,
            ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png")
        ).setPages(ResearchPage("1"), ResearchPage("2")).setVirtual().registerResearchItem()

        AntiCraftResearchItem(
            "ANTI_NODE",
            khariumCategory,
            AspectList(),
            4,
            7,
            0,
            ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png")
        ).setPages(ResearchPage("1"), ResearchPage("2")).setStub().setHidden().setRound().setSpecial()
            .setParents("CRYSTALLIZED_KHARU").registerResearchItem()

        AntiCraftResearchItem(
            "KHARU_SNARE",
            khariumCategory,
            AspectList().add(Aspect.ELDRITCH, 8).add(KhariumAspect.HUMILITAS, 4).add(Aspect.MAGIC, 6)
                .add(Aspect.MECHANISM, 1).add(Aspect.ENERGY, 1).add(Aspect.TRAP, 1).add(Aspect.METAL, 1),
            4,
            12,
            0,
            ResourceLocation(Kharium.MOD_ID, "textures/aspects/humilitas.png")
        ).setPages(ResearchPage("1"), ResearchPage("2")).setSpecial()
            .setParents("ANTI_NODE").registerResearchItem()
//        AntiCraftResearchItem(
//            "ANTI_NODE",
//            khariumCategory,
//            AspectList(),
//            4,
//            7,
//            0,
//            ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png")
//        ).setPages(ResearchPage("1"), ResearchPage("2")).setStub().setHidden().setRound().setSpecial()
//            .setParents("CRYSTALLIZED_KHARU").registerResearchItem()
//
//        ThaumcraftApi.addWarpToResearch("DIARY", 10)
//        ThaumcraftApi.addWarpToResearch("CRYSTALLIZED_KHARU", 20)
    }

}
