package com.suslovila.mixinUtils;

public interface IMixinTileNodeProvider {
     int getTransformationTimer();
     void addTime(int n);
     int getTransformationAspectSize();
     void setTransformationAspectSize(int size);
    boolean isNodeBeingTransformed();
}
