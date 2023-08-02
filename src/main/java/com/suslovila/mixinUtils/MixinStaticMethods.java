package com.suslovila.mixinUtils;

import thaumcraft.common.tiles.TileNode;

public class MixinStaticMethods {
    public static void startNodeTransformation(TileNode node){
        ((IMixinTileNodeProvider)(Object)node).addTime(1);
        ((IMixinTileNodeProvider)(Object)node).setTransformationAspectSize(node.getAspects().visSize());
    }
    public static boolean isNodeBeingTransformed(TileNode node){
        return ((IMixinTileNodeProvider)(Object)node).getTransformationTimer() != -1;
    }
}
