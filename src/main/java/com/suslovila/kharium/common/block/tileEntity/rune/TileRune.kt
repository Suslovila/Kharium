package com.suslovila.kharium.common.block.tileEntity.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.common.block.tileEntity.TileKharium
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.ModelWrapperDisplayList
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.KhariumSusNBTHelper.writeTo
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.cos
import com.suslovila.kharium.utils.getPosDouble
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.obj.WavefrontObject
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import kotlin.math.abs
import kotlin.math.sqrt

sealed class TileRune : TileKharium() {
    abstract val runeType: RuneType
    abstract val disabled: ResourceLocation

    abstract val waveColor: Int
    abstract val cubeCoreTexture: ResourceLocation
    abstract val cubeGlowingTexture: ResourceLocation

    abstract val straightCoreTexture: ResourceLocation
    abstract val straightGlowingTexture: ResourceLocation

    abstract val cornerCoreTexture: ResourceLocation
    abstract val cornerGlowingTexture: ResourceLocation

    open val straightModel: IModelCustom
    open val cornerModel: IModelCustom

    init {
        straightModel =
            ModelWrapperDisplayList(
                AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/runes/rune_straight.obj"))
                        as WavefrontObject
            )
        cornerModel =
            ModelWrapperDisplayList(
                AdvancedModelLoader.loadModel(
                    ResourceLocation(
                        Kharium.MOD_ID,
                        "models/blocks/runes/rune_corner.obj"
                    )
                ) as WavefrontObject
            )

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

    val facings = arrayListOf(
        ForgeDirection.SOUTH,
        ForgeDirection.NORTH,
        ForgeDirection.EAST,
        ForgeDirection.WEST
    )

    open fun renderAsLayerPart(
        snare: TileKharuSnare,
        partialTicks: Float,
        xOffset: Int,
        zOffset: Int,
        layerIndex: Int,
        layerAmount: Int
    ) {
        glPushMatrix()
        val destination = this.getPosDouble().add(0.5, 0.5, 0.5)

        SusGraphicHelper.translateFromPlayerTo(destination, partialTicks)
        val isCorner = abs(xOffset) == layerIndex && abs(zOffset) == layerIndex
        val isBorder = abs(xOffset) == layerIndex || abs(zOffset) == layerIndex
        val firstLayerCenter = (zOffset == 0 && xOffset == 0)
        if (firstLayerCenter) {
            SusGraphicHelper.setStandartColors()
            UtilsFX.bindTexture(this.cubeCoreTexture)
            SusGraphicHelper.cubeModel.renderAll()
            if (enabled) {
                SusGraphicHelper.setStandartColors()
                UtilsFX.bindTexture(this.cubeGlowingTexture)
                SusGraphicHelper.bindColor(waveColor, 1.0f, 1.0f)
                glowingPreparations()
                SusGraphicHelper.cubeModel.renderAll()
                SusGraphicHelper.popLight()
            }
            glPopMatrix()
            return
        }
        if (!isBorder && !isCorner) {
            SusGraphicHelper.setStandartColors()
            UtilsFX.bindTexture(this.cubeCoreTexture)
            SusGraphicHelper.cubeModel.renderAll()
            if (enabled) {
                UtilsFX.bindTexture(this.cubeGlowingTexture)
                SusGraphicHelper.bindColor(waveColor, 1.0f, 1.0f)
                glowingPreparations()
                SusGraphicHelper.cubeModel.renderAll()
                SusGraphicHelper.popLight()
            }
            glPopMatrix()
            return
        }
        val time = SusGraphicHelper.getRenderGlobalTime(partialTicks)

        val facing = facings.minBy { forgeDirection ->
            SusVec3.angleBetweenVec3(
                // this is shit code a bit, but maybe the most easy variant to correctly rotate the corner elements
                SusVec3(xOffset, 0, zOffset).yRot(if (isCorner) 20f else 0f),
                SusVec3(forgeDirection.offsetX, forgeDirection.offsetY, forgeDirection.offsetZ)
            )
        }
        TileKharuSnareRenderer.rotateFromOrientation(facing)


        if (isCorner) {

            glTranslated(-0.5, 0.0, 0.5)

            // render core
            glPushMatrix()
            glColor4d(1.0, 1.0, 1.0, 1.0)
            disableAllGlowing()
            UtilsFX.bindTexture(this.cornerCoreTexture)
            cornerModel.renderOnly("core")
            glPopMatrix()

            var scaleMovementFactor = 1.0f
            if (snare.enabled) {
                scaleMovementFactor = cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f
                val delta = 0.7f * abs(scaleMovementFactor + 1)
                glTranslatef(delta, delta, -delta)

            }
            glColor4d(1.0, 1.0, 1.0, 1.0)
            UtilsFX.bindTexture(this.cornerCoreTexture)
            disableAllGlowing()
            cornerModel.renderOnly("floating")

            if (snare.enabled) {
                glPushMatrix()
                glowingPreparations()
                SusGraphicHelper.bindColor(this.waveColor, abs(sqrt((scaleMovementFactor * 5.0f + 1) / 2)), 1.0f)
                glScalef(1.02f, 1.02f, 1.02f)
                UtilsFX.bindTexture(this.cornerGlowingTexture)
                cornerModel.renderOnly("floating")
                SusGraphicHelper.popLight()
                glPopMatrix()
            }


        } else {
            // render core
            glPushMatrix()
            glColor4d(1.0, 1.0, 1.0, 1.0)
            UtilsFX.bindTexture(straightCoreTexture)
            disableAllGlowing()
            straightModel.renderPart("core")
            glPopMatrix()
            glColor4d(1.0, 0.0, 0.0, 1.0)
            var scaleMovementFactor = 1.0f
            if (snare.enabled) {
                scaleMovementFactor = cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f

                val delta = 0.7f * (scaleMovementFactor + 1)
                var additionalDelta = 0f
                if (layerIndex > 1) {
                    if (abs(xOffset) == layerIndex) {
                        additionalDelta = 0.7f / (layerIndex) * zOffset * (if (xOffset < 0) -1 else 1)
                        glTranslatef(additionalDelta, 0f, 0f)
                    }
                    if (abs(zOffset) == layerIndex) {
                        additionalDelta = 0.7f / (layerIndex) * xOffset * (if (zOffset < 0) 1 else -1)
                        glTranslatef(additionalDelta, 0f, 0f)
                    }
                }
                glTranslatef(0.0f, delta, -delta)
            }
            glPushMatrix()
            glowingPreparations()
            glColor4d(1.0, 1.0, 1.0, 1.0)
            UtilsFX.bindTexture(this.straightCoreTexture)
            straightModel.renderOnly("floating")
            glPopMatrix()

            if (snare.enabled) {
                glPushMatrix()
                glowingPreparations()
                SusGraphicHelper.bindColor(this.waveColor, abs(sqrt((scaleMovementFactor * 5.0f + 1) / 2)), 1.0f)
                UtilsFX.bindTexture(this.straightGlowingTexture)
                straightModel.renderOnly("floating")
                SusGraphicHelper.popLight()
                glPopMatrix()
            }
        }
        glPopMatrix()
    }

    private fun renderRuneCore() {
        glPushMatrix()
        glColor4d(1.0, 1.0, 1.0, 1.0)
        disableAllGlowing()
        UtilsFX.bindTexture(this.cornerCoreTexture)
        cornerModel.renderOnly("core")
        glPopMatrix()
    }

    private fun glowingPreparations() {
        SusGraphicHelper.pushLight()
        SusGraphicHelper.setMaxBrightness()
        glDisable(GL_LIGHTING)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_ALPHA_TEST)
        glAlphaFunc(GL_GREATER, 0.0f)
        SusGraphicHelper.popLight()
    }

    private fun disableAllGlowing() {
        glEnable(GL_LIGHTING)
        glDisable(GL_BLEND)
        glDisable(GL_ALPHA_TEST)
    }
}