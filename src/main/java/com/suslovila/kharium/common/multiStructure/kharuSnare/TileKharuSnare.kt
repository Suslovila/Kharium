package com.suslovila.kharium.common.multiStructure.kharuSnare

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
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.AspectSourceHelper
import thaumcraft.common.lib.events.EssentiaHandler
import kotlin.math.sqrt

class TileKharuSnare() : TileDefaultMultiStructureElement(), PostRendered, IInventory {
    val inventory: IInventory = SimpleInventory(0,0, "inv", 64)
    override val packetId: Int = 0
    val maxLowerAmount = 10
    var timeCheck = 20

    // a rare situation when I need timer for both sides
    companion object {
        val activationTime = 60
    }


    var aspects = AspectList().add(ACAspect.HUMILITAS, 1)
    var enabled = false
    var finalisedLayerAmount = 0
    val isPrepared: Boolean
        get() {
            return activationTimer == activationTime
        }
    var activationTimer = 0
    var tick = 0

    val preparationPercent: Double
        get() {
            return activationTimer.toDouble() / activationTime.toDouble()
        }

    fun getClientPreparationPercent(partialTicks: Float) =
        if (activationTimer != 0) ((activationTimer + partialTicks * (if (enabled) 1 else -1)).toDouble() / activationTime.toDouble()).coerceIn(
            0.0,
            1.0
        ) else 0.0

    override fun updateEntity() {

        if (world.isRemote) {
            return
        }
        activationTimer = (activationTimer + if (enabled) 1 else -1).coerceIn(0, activationTime)
//        EssentiaHandler.refreshSources(this)

        tick = (tick + 1) % Int.MAX_VALUE
        if (tick % timeCheck == 0) {
            if (tick % timeCheck == 0) {
                world.customData.kharuHotbeds.clear()
                world.customData.addKharuHotbed(
                    KharuHotbed(
                        AxisAlignedBB.getBoundingBox(
                            (xCoord - 7 - 3).toDouble(),
                            (yCoord - 7 - 3).toDouble(),
                            (zCoord - 7 - 3).toDouble(),
                            (xCoord - 7 + 3).toDouble(),
                            (yCoord - 7 + 3).toDouble(),
                            (zCoord - 7 + 3).toDouble()
                        ),
                        100_000
                    ) ,
                    world = world
                )
                world.customData.syncAllHotbeds(world)
//                world.customData.addKharuHotbed(world)
            }
            val hasAntiNode = world.getTile(this.getPosition() + Position(0, -8, 0)) is TileAntiNode
            if (!hasAntiNode) {
                enabled = false
            } else {
                val hasEssentia = tryTakeEssentia()
                enabled = hasEssentia
            }
        }
        markForSaveAndSync()

    }

    fun tryTakeEssentia(): Boolean {
        for (aspectType in this.aspects.getAspects()) {
            for (i in 1..aspects.getAmount(aspectType)) {
                val haveFound = EssentiaHandler.findEssentia(this, aspectType, ForgeDirection.UNKNOWN, 12)
                if (!haveFound) return false
                val successDraining = AspectSourceHelper.drainEssentia(this, aspectType, ForgeDirection.UNKNOWN, 12)
                if (!successDraining) return false

            }
        }
        return true
    }

    override fun postRender(event: RenderWorldLastEvent) {
        AntiNodeStabilizersRenderer.postRender(this, event)
        TileKharuSnareRenderer.postRender(this, event)
        DischargeFlaskRenderer.postRender(this, event)
    }

    override fun getRenderBoundingBox(): AxisAlignedBB = INFINITE_EXTENT_AABB


    val ENABLED_NBT = Kharium.prefixAppender.doAndGet("enabled")
    val TICK_NBT = Kharium.prefixAppender.doAndGet("tick")
    val ACTIVATION_NBT = Kharium.prefixAppender.doAndGet("activation_timer")
    val LAYER_AMOUNT_NBT = Kharium.prefixAppender.doAndGet("layer_amount")

    override fun writeCustomNBT(rootNbt: NBTTagCompound) {
        super.writeCustomNBT(rootNbt)
        aspects.writeToNBT(rootNbt)
        rootNbt.setBoolean(ENABLED_NBT, enabled)
        rootNbt.setInteger(TICK_NBT, tick)
        rootNbt.setInteger(ACTIVATION_NBT, activationTimer)
        rootNbt.setInteger(LAYER_AMOUNT_NBT, finalisedLayerAmount)

    }

    override fun readCustomNBT(rootNbt: NBTTagCompound) {
        super.readCustomNBT(rootNbt)
        aspects.readFromNBT(rootNbt)
        enabled = rootNbt.getBoolean(ENABLED_NBT)
        tick = rootNbt.getInteger(TICK_NBT)
        activationTimer = rootNbt.getInteger(ACTIVATION_NBT)
        finalisedLayerAmount = rootNbt.getInteger(LAYER_AMOUNT_NBT)
    }

    fun affectAntiNode(tileAntiNode: TileAntiNode) {
        aspects.aspects.clear()
        aspects.add(ACAspect.HUMILITAS, 1)
        getRunes().forEach { rune ->
            rune.onRegularSnareCheck(this, tileAntiNode)
        }
        tileAntiNode.markForSaveAndSync()
        markForSaveAndSync()
    }

    fun getRunes(): ArrayList<TileRune> {
        val startPosition = this.getPosition() + Position(0, -13, 0)
        val runes = arrayListOf<TileRune>()
        finalisedLayerAmount = 0
        for (layerIndex in 0 until maxLowerAmount) {
            val layerRunes = getLayerRunes(startPosition + Position(0, -layerIndex, 0), layerIndex) ?: return runes
            finalisedLayerAmount += 1
            runes.addAll(layerRunes)
        }
        return runes
    }

    fun getLayerRunes(layerPos: Position, layerIndex: Int): ArrayList<TileRune>? {
        val foundRunes = arrayListOf<TileRune>()
        for (xOffset in -layerIndex..layerIndex) {
            for (zOffset in -layerIndex..layerIndex) {
                val foundTile = world.getTile(layerPos + Position(xOffset, 0, zOffset)) ?: return null
                if (foundTile !is TileRune) return null
                foundTile.snarePos = this.getPosition()

                foundTile.ownLayerLevel = layerIndex
                foundTile.markForSaveAndSync()
                foundRunes.add(foundTile)
            }
        }
        return foundRunes
    }

    override fun markDirty() {
        inventory.markDirty()
    }





    override fun getSizeInventory() : Int = 0

    override fun setInventorySlotContents(slot: kotlin.Int, stack: ItemStack?) {
        inventory.setInventorySlotContents(slot, stack)
    }

    override fun getInventoryName(): kotlin.String? {
        return ("tile.assemblyTableBlock.name")
    }

    override fun getStackInSlot(slot: kotlin.Int): ItemStack? {
        return inventory.getStackInSlot(slot)
    }

    override fun decrStackSize(slot: kotlin.Int, amount: kotlin.Int): ItemStack? {
        return inventory.decrStackSize(slot, amount)
    }

    override fun getStackInSlotOnClosing(slot: kotlin.Int): ItemStack? {
        return inventory.getStackInSlotOnClosing(slot)
    }


    override fun getInventoryStackLimit(): kotlin.Int {
        return inventory.inventoryStackLimit
    }

    override fun isUseableByPlayer(player: EntityPlayer): kotlin.Boolean {
        return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) === this) && !isInvalid() &&
                (sqrt(player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5)) <= 24))
    }

    override fun openChest() {}

    override fun closeChest() {}

    override fun isCustomInventoryName(): kotlin.Boolean {
        return false
    }

    override fun isItemValidForSlot(slot: kotlin.Int, stack: ItemStack?): kotlin.Boolean {
        return inventory.isItemValidForSlot(slot, stack)
    }

    open fun getInputSlotAmount(): Int = 0

    open fun getOutputSlotsAmount(): kotlin.Int {
        return getSizeInventory() - getInputSlotAmount()
    }




}

fun Boolean.toInt(): Int = if (this) 1 else 0