package com.suslovila.kharium.common.multiStructure.implantInstaller

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.KharuHotbed
import com.suslovila.kharium.research.ACAspect
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.AspectSourceHelper
import thaumcraft.common.lib.events.EssentiaHandler
import kotlin.math.sqrt

class TileImplantInstaller() : TileDefaultMultiStructureElement() {
    override val packetId: Int = 0

    // a rare situation when I need timer for both sides
    companion object {

    }

    override fun updateEntity() {

    }

}

fun Boolean.toInt(): Int = if (this) 1 else 0