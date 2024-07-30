package com.suslovila.kharium.common.multiStructure.kharuSnare

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.KharuHotbed
import com.suslovila.kharium.research.KhariumAspect
import com.suslovila.kharium.utils.Percentage
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuContainer
import com.suslovila.kharium.utils.config.multistructures.ConfigKharuSnare
import com.suslovila.kharium.utils.getPosition
import com.suslovila.kharium.utils.plusAssign
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.AspectSourceHelper
import thaumcraft.common.lib.events.EssentiaHandler
import kotlin.math.min

class TileKharuSnare() : TileDefaultMultiStructureElement(), PostRendered {
    val inventory: IInventory = SimpleInventory(0, 0, "inv", 64)
    override val packetId: Int = 0
    val maxLowerAmount = 10
    var timeCheck = 20

    // a rare situation when I need timer for both sides
    companion object {
        val activationTime = 60
    }

    var aspects = AspectList().add(KhariumAspect.HUMILITAS, 1)
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
//            if (tick % timeCheck == 0) {
//                world.customData.kharuHotbeds.clear()
//                world.customData.addKharuHotbed(
//                    KharuHotbed(
//                        AxisAlignedBB.getBoundingBox(
//                            (xCoord - 7 - 3).toDouble(),
//                            (yCoord - 7 - 3).toDouble(),
//                            (zCoord - 7 - 3).toDouble(),
//                            (xCoord - 7 + 3).toDouble(),
//                            (yCoord - 7 + 3).toDouble(),
//                            (zCoord - 7 + 3).toDouble()
//                        ),
//                        100_000
//                    ),
//                    world = world
//                )
//                world.customData.syncAllHotbeds(world)
////                world.customData.addKharuHotbed(world)
//            }

            val antiNode = world.getTile(this.getPosition() + Position(0, -8, 0)) as? TileAntiNode
            if (antiNode == null) {
                enabled = false
            } else {
                val hasEssentia = tryTakeEssentia()
                enabled = hasEssentia

                val container = (world.getTile(
                    this.getPosition() + Position(
                        0,
                        1,
                        0
                    )
                ) as? TileKharuSnareContainer)
                container?.putKharu(antiNode.actualEnergy)
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

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        aspects.writeToNBT(nbttagcompound)
        nbttagcompound.setBoolean(ENABLED_NBT, enabled)
        nbttagcompound.setInteger(TICK_NBT, tick)
        nbttagcompound.setInteger(ACTIVATION_NBT, activationTimer)
        nbttagcompound.setInteger(LAYER_AMOUNT_NBT, finalisedLayerAmount)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        aspects.readFromNBT(nbttagcompound)
        enabled = nbttagcompound.getBoolean(ENABLED_NBT)
        tick = nbttagcompound.getInteger(TICK_NBT)
        activationTimer = nbttagcompound.getInteger(ACTIVATION_NBT)
        finalisedLayerAmount = nbttagcompound.getInteger(LAYER_AMOUNT_NBT)
    }

    fun affectAntiNode(tileAntiNode: TileAntiNode) {
        aspects.aspects.clear()
        aspects.add(KhariumAspect.HUMILITAS, 1)
        val runeAmount = getRunes()
        for (runeType in RuneType.values()) {
            tileAntiNode.runeFactorSustains[runeType.ordinal] +=
                Percentage(
                    runeAmount[runeType.ordinal] * ConfigKharuSnare.runeInfluence[runeType.ordinal]
                )
        }

        markForSaveAndSync()
    }


    fun getRunes(): Array<Int> {
        val startPosition = this.getPosition() + Position(0, -13, 0)
        val runesAmount = Array(RuneType.values().size) { _ -> 0 }
        finalisedLayerAmount = 0
        for (layerIndex in 0 until maxLowerAmount) {
            val layerRunesAmount =
                getLayerRunes(startPosition + Position(0, -layerIndex, 0), layerIndex) ?: return runesAmount
            finalisedLayerAmount += 1
            for (runeType in RuneType.values()) {
                runesAmount[runeType.ordinal] += layerRunesAmount[runeType.ordinal]
            }
        }
        return runesAmount
    }

    fun getLayerRunes(layerPos: Position, layerIndex: Int): Array<Int>? {
        val foundRunes = Array(RuneType.values().size) { _ -> 0 }
        for (xOffset in -layerIndex..layerIndex) {
            for (zOffset in -layerIndex..layerIndex) {
                val foundTile = world.getTile(layerPos + Position(xOffset, 0, zOffset)) ?: return null
                if (foundTile !is TileRune) return null
                foundTile.snarePos = this.getPosition()

                foundTile.ownLayerLevel = layerIndex
                foundTile.markForSaveAndSync()
                foundRunes[foundTile.runeType.ordinal] += 1
            }
        }
        return foundRunes
    }
}
