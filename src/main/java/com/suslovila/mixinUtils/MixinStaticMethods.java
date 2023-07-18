package com.suslovila.mixinUtils;

import thaumcraft.common.tiles.TileNode;

public class MixinStaticMethods {
    public static void startNodeTransformation(TileNode node){
        ((MixinTileNodeProvider)(Object)node).addTime(1);
        ((MixinTileNodeProvider)(Object)node).setTransformationAspectSize(node.getAspects().visSize());
    }
    public static boolean isNodeBeingTransformed(TileNode node){
        return ((MixinTileNodeProvider)(Object)node).getTransformationTimer() != -1;
    }
}
