package com.suslovila.mixinUtils;

import com.suslovila.utils.SusNBTHelper;
import com.suslovila.common.item.ItemCrystallizedAntiMatter;
import net.minecraft.item.ItemStack;
import thaumcraft.common.tiles.TileNode;

public class MixinStaticMethods {
    public static void startNodeTransformation(TileNode node){
        ((IMixinTileNodeProvider)(Object)node).addTime(1);
        ((IMixinTileNodeProvider)(Object)node).setTransformationAspectSize(node.getAspects().visSize());

    }
    public static void startNodeTransformation(TileNode node, ItemStack stack){
        startNodeTransformation(node);
        ((IMixinTileNodeProvider)(Object)node).setOwnerName(SusNBTHelper.INSTANCE.getOrCreateTag(stack).getString(ItemCrystallizedAntiMatter.Companion.getGlobalOwnerName()));
    }

    public static boolean isNodeBeingTransformed(TileNode node){
        return ((IMixinTileNodeProvider)(Object)node).getTransformationTimer() != -1;
    }
}
