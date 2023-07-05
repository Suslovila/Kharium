package com.suslovila.common.block.tileEntity;

import com.suslovila.utils.SUSVec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TileAntiNode extends TileEntity {
    public ConcurrentHashMap<SUSVec3, ArrayList<Object>> cordsForShadows = new ConcurrentHashMap<>();
}
