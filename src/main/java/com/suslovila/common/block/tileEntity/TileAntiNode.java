package com.suslovila.common.block.tileEntity;

import com.suslovila.utils.SUSVec3;
import com.suslovila.utils.Vector3;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.HashMap;

public class TileAntiNode extends TileNode {
    public HashMap<SUSVec3, ArrayList<Object>> cordsForShadows = new HashMap<>();
    //public HashMap<SUSVec3, ArrayList<Object>> cordsForRings = new HashMap<>();

    public int timer = 0;


}
