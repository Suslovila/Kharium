package com.suslovila.kharium.common.block.tileEntity.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.render.tile.TileKharuSnareRenderer
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.TileKharium
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusNBTHelper.writeTo
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.cos
import com.suslovila.kharium.utils.getPosDouble
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import kotlin.math.abs

abstract class TileRune : TileKharium() {
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
        val firstLayerCenter = zOffset == 0 && xOffset == 0
        if (firstLayerCenter) {
            UtilsFX.bindTexture(this.cubeCoreTexture)
            SusGraphicHelper.cubeModel.renderAll()
            if (enabled) {
                UtilsFX.bindTexture(this.cubeGlowingTexture)
                glowingPreparations()
                SusGraphicHelper.cubeModel.renderAll()
                SusGraphicHelper.popLight()
            }
            glPopMatrix()
            return
        }
        if (!isBorder && !isCorner) {
            UtilsFX.bindTexture(this.cubeCoreTexture)
            SusGraphicHelper.cubeModel.renderAll()
            if (enabled) {
                UtilsFX.bindTexture(this.cubeGlowingTexture)
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

            renderRuneCore()

            if (snare.enabled) {
                var scaleMovementFactor = 1 + cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f
                val delta = 0.7f * abs(scaleMovementFactor)
                glTranslatef(delta, delta, -delta)
                glPushMatrix()
//                val correction = 0.25
//                glTranslated(correction, -0.1, -correction)
//                SusGraphicHelper.drawFloatyLine(
//                    -delta.toDouble() - correction,
//                    -delta.toDouble() + correction + 0.01,
//                    delta.toDouble() - correction + 0.4,
//                    waveColor,
//                    TileKharuSnareRenderer.wavesTexture,
//                    speed = 0.05f,
//                    Math.min(time + 2, 10.0f) / 10.0f,
//                    width = 1F,
//                    time = time,
//                    true,
//                    1.0
//                ) {
//                    glEnable(GL_BLEND)
//                    glBlendFunc(GL_SRC_ALPHA, GL_ONE)
//                }

                glPopMatrix()

                glPushMatrix()
                glScaled(0.5, 0.5, 0.5)
                glowingPreparations()
                glColor4d(1.0, 1.0, 1.0, 1.0)
                UtilsFX.bindTexture(this.cornerGlowingTexture)
                cornerModel.renderOnly("plane_Cube.002")
                SusGraphicHelper.popLight()
                glPopMatrix()
            }
            glColor4d(1.0, 1.0, 1.0, 1.0)
            UtilsFX.bindTexture(this.cornerCoreTexture)
            glScaled(0.5, 0.5, 0.5)
            disableAllGlowing()
            cornerModel.renderOnly("plane_Cube.002")
        } else {
            glPushMatrix()
            glScaled(0.5, 0.5, 0.5)
            glColor4d(1.0, 1.0, 1.0, 1.0)
            UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
            disableAllGlowing()
            straightModel.renderPart("plane_Cube")
            glPopMatrix()
            glColor4d(1.0, 0.0, 0.0, 1.0)

            if (snare.enabled) {
                val scaleMovementFactor = 1 + cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f

                val delta = 0.7f * scaleMovementFactor
                var additionalDelta = 0f
                if (layerIndex > 1) {
                    if (abs(xOffset) == layerIndex) {
                        additionalDelta = 0.7f / (layerIndex - 1) * zOffset * (if (xOffset < 0) -1 else 1)
                        glTranslatef(additionalDelta, 0f, 0f)
                    }
                    if (abs(zOffset) == layerIndex) {
                        additionalDelta = 0.7f / (layerIndex - 1) * xOffset * (if (zOffset < 0) 1 else -1)
                        glTranslatef(additionalDelta, 0f, 0f)
                    }
                }
                glTranslatef(0.0f, delta, -delta)
//                glPushMatrix()
//                val correction = 0.3
//                glTranslated(0.0, -correction, correction)
//                SusGraphicHelper.drawFloatyLine(
//                    -additionalDelta.toDouble(),
//                    -delta.toDouble() + correction + 0.01,
//                    delta.toDouble() - correction + 0.4,
//                    waveColor,
//                    TileKharuSnareRenderer.wavesTexture,
//                    speed = 0.1f,
//                    Math.min(time + 2, 10.0f) / 10.0f,
//                    width = 1F,
//                    time = time,
//                    true,
//                    1.0
//                ) {
//                    glEnable(GL_BLEND)
//                    glBlendFunc(GL_SRC_ALPHA, GL_ONE)
//                }
//                glPopMatrix()


                glPushMatrix()
                glScaled(0.5, 0.5, 0.5)
                glowingPreparations()
                glColor4d(1.0, 1.0, 1.0, 1.0)
                UtilsFX.bindTexture(this.straightGlowingTexture)
                straightModel.renderOnly("floating")
                SusGraphicHelper.popLight()
                glPopMatrix()
            }
            glPushMatrix()
            glScaled(0.5, 0.5, 0.5)
            glowingPreparations()
            glColor4d(1.0, 1.0, 1.0, 1.0)
            glScaled(0.5, 0.5, 0.5)
            UtilsFX.bindTexture(this.straightCoreTexture)
            straightModel.renderOnly("floating")
            glPopMatrix()
        }
        glPopMatrix()
    }

    private fun renderRuneCore() {
        glPushMatrix()
        glScaled(0.5, 0.5, 0.5)
        glColor4d(1.0, 1.0, 1.0, 1.0)
        disableAllGlowing()
        UtilsFX.bindTexture(this.cornerCoreTexture)
        cornerModel.renderOnly("plane_Cube.001")
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
    }

    private fun disableAllGlowing() {
        glEnable(GL_LIGHTING)
        glDisable(GL_BLEND)
        glDisable(GL_ALPHA_TEST)
    }
}