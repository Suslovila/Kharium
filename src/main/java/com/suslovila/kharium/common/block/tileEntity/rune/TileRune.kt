package com.suslovila.kharium.common.block.tileEntity.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.TileKharium
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.SusNBTHelper.writeTo
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom

abstract class TileRune : TileKharium() {
    abstract val disabled: ResourceLocation

    abstract val straightSleepCoreTexture: ResourceLocation
    abstract val straightSleepGlowingTexture: ResourceLocation
    abstract val straightActiveCoreTexture: ResourceLocation
    abstract val straightActiveGlowingTexture: ResourceLocation

    abstract val cornerSleepCoreTexture: ResourceLocation
    abstract val cornerSleepGlowingTexture: ResourceLocation
    abstract val cornerActiveCoreTexture: ResourceLocation
    abstract val cornerActiveGlowingTexture: ResourceLocation

    open val straightModel: IModelCustom
    open val cornerModel: IModelCustom

    init {
        straightModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/runes/rune_straight.obj"))
        cornerModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/runes/rune_corner.obj"))
    }

    var snarePos: Position? = null
    val SNARE_POS_NBT = Kharium.prefixAppender.doAndGet("master_pos")
    var ownLayerLevel: Int? = null

    val enabled: Boolean
        get() {
            val snare = (world.getTile(snarePos) as? TileKharuSnare) ?: return false
            return (snare.enabled && snare.finalisedLayerAmount > (ownLayerLevel ?: snare.maxLowerAmount))
        }

    val snare: TileKharuSnare?
        get() {
            return world.getTile(snarePos) as? TileKharuSnare
        }
    val isOwnLevelFinalised: Boolean
        get() {
            return (snare?.finalisedLayerAmount ?: -2) > (ownLayerLevel ?: -1)
        }
    fun getClientPreparationPercent(partialTicks: Float): Double =
        snare?.getClientPreparationPercent(partialTicks) ?: 0.0

    abstract fun onRegularSnareCheck(snare: TileKharuSnare, antiNode: TileAntiNode)


    val LAYER_NBT = Kharium.prefixAppender.doAndGet("layer_level")
    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        val posNbt = NBTTagCompound()
        snarePos?.writeTo(posNbt)
        nbttagcompound.setTag(SNARE_POS_NBT, posNbt)
        ownLayerLevel?.writeTo(nbttagcompound, LAYER_NBT)
    }

    override fun readCustomNBT(rootNbt: NBTTagCompound) {
        super.readCustomNBT(rootNbt)
        val posNbt = rootNbt.getCompoundTag(SNARE_POS_NBT)
        snarePos = if (posNbt.hasKey(Position.X_NBT) &&
            posNbt.hasKey(Position.Y_NBT) &&
            posNbt.hasKey(Position.Z_NBT)
        ) {
            Position.readFrom(posNbt)
        } else {
            null
        }
        ownLayerLevel = if (rootNbt.hasKey(LAYER_NBT)) rootNbt.getInteger(LAYER_NBT) else null
    }
}