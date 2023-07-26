package com.suslovila.common.block.tileEntity;

import com.suslovila.utils.SUSVec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.items.ItemCompassStone;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TileAntiNode extends TileEntity {
    public ConcurrentHashMap<SUSVec3, ArrayList<Object>> cordsForShadows = new ConcurrentHashMap<>();
    public int tickExisted;
    public int energy;
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.readCustomNBT(nbttagcompound);
    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        tickExisted = nbttagcompound.getInteger("timer");
        energy = nbttagcompound.getInteger("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.writeCustomNBT(nbttagcompound);
    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("timer", tickExisted);
        nbttagcompound.setInteger("energy", energy);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readCustomNBT(pkt.func_148857_g());
    }
    public void updateEntity() {
        super.updateEntity();
        tickExisted = (tickExisted + 1) % Integer.MAX_VALUE;

    }
}
