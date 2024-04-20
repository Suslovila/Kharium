/*     */
package com.suslovila.kharium.common.crafting;

import com.suslovila.kharium.common.block.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;




public class RecipeRegistry {
    public static void init() {
        initCrafting();
        initSmelting();
        initCrucible();
        initInfusion();
        initArcane();
    }


    public static void initCrafting() {
        GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.blockVoidMetal)), new Object[]{"AAA", "AAA", "AAA", Character.valueOf('A'), new ItemStack(ConfigItems.itemResource, 1, 16)});
    }


    public static void initSmelting() {
//    GameRegistry.addSmelting(BlockRegistry.BlockShadowOre, new ItemStack(ItemRegistry.ItemMaterial), 2.0F);
    }


    public static void initInfusion() {
    }


    public static void initArcane() {
    }


    public static void initCrucible() {
    }
}