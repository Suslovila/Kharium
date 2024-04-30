package com.suslovila.kharium.common.block.tileEntity

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import thaumcraft.api.TileThaumcraft

open class TileKharium : TileThaumcraft() {

    override fun getDescriptionPacket(): Packet {
        val nbttagcompound = NBTTagCompound()
        writeCustomNBT(nbttagcompound)
        return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound)
    }
    open fun markForSaveAndSync() {
        markForSave()
        markForSync()
    }

    open fun markForSave() {
        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this)
    }

    open fun markForSync() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
}