package com.suslovila.kharium.mixinUtils;

import com.suslovila.kharium.common.block.ModBlocks;
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode;
import com.suslovila.kharium.utils.SusNBTHelper;
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.tiles.TileNode;

public class MixinStaticMethods {
    public static void startNodeTransformation(TileNode node) {
        ((IMixinTileNodeProvider) (Object) node).addTime(1);
        ((IMixinTileNodeProvider) (Object) node).setTransformationAspectSize(node.getAspects().visSize());

    }

    public static void startNodeTransformation(TileNode node, ItemStack stack) {
        startNodeTransformation(node);
        ((IMixinTileNodeProvider) (Object) node).setOwnerName(SusNBTHelper.INSTANCE.getOrCreateTag(stack).getString(ItemCrystallizedAntiMatter.Companion.getGlobalOwnerName()));
    }

    public static boolean isNodeBeingTransformed(TileNode node) {
        return ((IMixinTileNodeProvider) (Object) node).getTransformationTimer() != -1;
    }
    public static void createAntiNode(World world, int x ,int y, int z, int maxEnergy){
        world.setBlock(x, y, z, ModBlocks.ANTI_NODE);
        TileAntiNode antiNode = (TileAntiNode) world.getTileEntity(x, y, z);
        antiNode.setMaxEnergy(maxEnergy);


    }
}
