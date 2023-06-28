package com.suslovila.common.block.tileEntity;

import com.suslovila.utils.SUSVec3;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TileAntiNode extends TileEntity {
    public ConcurrentHashMap<SUSVec3, ArrayList<Object>> cordsForShadows = new ConcurrentHashMap<>();

}
