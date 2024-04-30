package com.suslovila.kharium.client.render.tile

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusUtils.humilitasColor
import com.suslovila.kharium.client.render.ClientEventHandler
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.DischargeFlaskRenderer
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.utils.*
import com.suslovila.kharium.utils.SusUtils.humilitasColorObj
import com.suslovila.kharium.utils.SusUtils.random
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import java.awt.Color
import kotlin.math.abs

object TileKharuSnareRenderer : SusTileRenderer<TileKharuSnare>() {

    val runesSwingPeriod = 40

    var coreModel: IModelCustom
    var fieldModel: IModelCustom
    val runesOverStabilizer = "textures/blocks/kharu_snare_over.png"
    val coreTexture = ResourceLocation(Kharium.MOD_ID, "textures/blocks/kharu_snare.png")
    val correctionOffset = -2.0
    val antiNodeOffset = -5.0
    val wavesTexture = ResourceLocation(Kharium.MOD_ID, "textures/misc/rune_active.png")

    init {
        coreModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/blocks/kharu_snare.obj"))
        fieldModel =
            AdvancedModelLoader.loadModel(ResourceLocation(Kharium.MOD_ID, "models/field.obj"))

    }

    override fun renderTileEntityAt(tile: TileEntity?, x: Double, y: Double, z: Double, ticks: Float) {
        super.renderTileEntityAt(tile, x, y, z, ticks)
        renderUpgradeRunes(tile as TileKharuSnare, ticks)
    }

    override fun render(tile: TileKharuSnare, partialTicks: Float) {
        ClientEventHandler.postRenders.add(tile)
        val time = Minecraft.getMinecraft().renderViewEntity.ticksExisted.toFloat() + partialTicks
        glPushMatrix()
        glTranslated(0.0, correctionOffset, 0.0)
        renderCore()
        if (tile.isPrepared) {
            renderTranslatingKharu(tile, partialTicks, time)
        }
        glPopMatrix()
        AntiNodeStabilizersRenderer.render(tile, partialTicks)
        DischargeFlaskRenderer.render(tile, partialTicks)
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture)
    }

    private fun renderTranslatingKharu(tile: TileKharuSnare, partialTicks: Float, time: Float) {
        glDisable(GL_LIGHTING)
        glTranslated(0.0, -1.0 + correctionOffset, 0.0)
        for (i in 0..4) {
            glPushMatrix()
            glRotated((45 + i * 90).toDouble(), 0.0, 1.0, 0.0)
            glTranslated(2.7, 0.0, 0.0)
            val scaleFactor = 3.0
            glScaled(scaleFactor, scaleFactor, scaleFactor)
            SusGraphicHelper.drawFloatyLine(
                -3.0 / scaleFactor,
                -6.3 / scaleFactor,
                0.0,
                1,
                ResourceLocation(Kharium.MOD_ID, "textures/misc/bubble.png"),
                speed = 0.1f,
                Math.min(time, 10.0f) / 10.0f,
                width = 0.3F,
                time = time,
                false,
                tile.getClientPreparationPercent(partialTicks)
            ) { glDisable(GL_BLEND) }
            SusGraphicHelper.drawFloatyLine(
                -3.0 / scaleFactor,
                -6.3 / scaleFactor,
                0.0,
                Color.red.rgb,
                ResourceLocation(Kharium.MOD_ID, "textures/misc/bubble.png"),
                speed = 0.05f,
                Math.min(time + 2, 10.0f) / 10.0f,
                width = 0.3F,
                time = time + 2,
                false,
                tile.getClientPreparationPercent(partialTicks),
            ) { glDisable(GL_BLEND) }
            glPopMatrix()
        }
    }


    fun renderCore() {
        glPushMatrix()

        glScaled(0.5, 0.5, 0.5)
        UtilsFX.bindTexture(coreTexture)

        glColor4f(1f, 1f, 1f, 1f)
        coreModel.renderAll()
//        renderGlowingElements()

        glPopMatrix()
    }


    private fun renderGlowingElements() {
        glPushMatrix()
        glAlphaFunc(516, 0.003921569f)
        glEnable(3042)
        glDisable(GL_LIGHTING)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        UtilsFX.bindTexture(Kharium.MOD_ID, runesOverStabilizer)
        val j = 15728880
        val k = j % 65536
        val l = j / 65536
        SusGraphicHelper.pushLight()
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k.toFloat() / 1.0f, l.toFloat() / 1.0f)
        SusGraphicHelper.popLight()
        val co = Color(humilitasColor)
        glColor4f(
            co.red / 255f,
            co.green / 255f,
            co.blue / 255f,
            1f
        )
        coreModel.renderAll()
        glDisable(GL_BLEND)
        glAlphaFunc(516, 0.1f)
        glPopMatrix()
    }

    fun postRender(tile: TileKharuSnare, event: RenderWorldLastEvent) {
        glPushMatrix()
        SusGraphicHelper.translateFromPlayerTo(
            tile.getPosDouble().add(SusVec3(0.5, antiNodeOffset + correctionOffset - 1 + 0.5, 0.5)),
            event.partialTicks
        )
        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)
        glDisable(GL_ALPHA_TEST)
        glDepthMask(false)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE)
        glDisable(GL_CULL_FACE)
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/whiteBlank.png")
        glColor4d(
            humilitasColorObj.red / 255.0,
            humilitasColorObj.green / 255.0,
            humilitasColorObj.blue / 255.0,
            0.07 * tile.getClientPreparationPercent(event.partialTicks)
        )
        val scaleFactor = 4.0
        glScaled(scaleFactor, scaleFactor, scaleFactor)
        fieldModel.renderAll()
        glPopMatrix()
        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glDisable(GL_ALPHA_TEST)
        glDepthMask(true)
    }

    fun renderUpgradeRunes(tile: TileKharuSnare, partialTicks: Float) {
//        if (!tile.enabled) return
        val startPosition = tile.getPosition() + Position(0, -13, 0)

        for (layerIndex in 0 until tile.finalisedLayerAmount) {
            val layerPos = startPosition + Position(0, -layerIndex, 0)
            renderLayer(tile, layerPos, layerIndex, tile.finalisedLayerAmount, partialTicks)
        }
    }

    fun renderLayer(tile: TileKharuSnare, layerPos: Position, layerIndex: Int, layerAmount: Int, partialTicks: Float) {
        UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
        val facings = arrayListOf(
            ForgeDirection.SOUTH,
            ForgeDirection.NORTH,
            ForgeDirection.EAST,
            ForgeDirection.WEST
        )
        for (xOffset in -layerIndex..layerIndex) {
            for (zOffset in -layerIndex..layerIndex) {
                val pos = layerPos + Position(xOffset, 0, zOffset)
                val foundTile = tile.world.getTile(layerPos + Position(xOffset, 0, zOffset)) ?: return
                if (foundTile !is TileRune) return
                glPushMatrix()
                val destination = pos.toSusVec3().add(0.5, 0.5, 0.5)
                SusGraphicHelper.translateFromPlayerTo(destination, partialTicks)
                val isCorner = abs(xOffset) == layerIndex && abs(zOffset) == layerIndex
                val isBorder = abs(xOffset) == layerIndex || abs(zOffset) == layerIndex
                val firstLayerCenter = zOffset == 0 && xOffset == 0
                if (firstLayerCenter) {
                    SusGraphicHelper.cubeModel.renderAll()
                    glPopMatrix()
                    continue
                }
                if (!isBorder && !isCorner) {
                    SusGraphicHelper.cubeModel.renderAll()
                    glPopMatrix()
                    continue
                }
                val time = SusGraphicHelper.getRenderGlobalTime(partialTicks)

                val facing = facings.minBy { forgeDirection ->
                    SusVec3.angleBetweenVec3(
                        // this is shit code a bit, but maybe the most easy variant to correctly rotate the corner elements
                        SusVec3(xOffset, 0, zOffset).yRot(if (isCorner) 20f else 0f),
                        SusVec3(forgeDirection.offsetX, forgeDirection.offsetY, forgeDirection.offsetZ)
                    )
                }
                rotateFromOrientation(facing)


                if (isCorner) {

                    glTranslated(-0.5, 0.0, 0.5)
//                    UtilsFX.bindTexture(foundTile.cornerActiveCoreTexture)
//                    SusGraphicHelper.drawGuideArrows()

                    glPushMatrix()
                    glScaled(0.5, 0.5, 0.5)
                    glColor4d(1.0, 0.0, 0.0, 1.0)
                    glDisable(GL_LIGHTING)
                    glDisable(GL_BLEND)
                    glDisable(GL_ALPHA_TEST)
                    UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                    foundTile.cornerModel.renderOnly("plane_Cube.001")
                    glPopMatrix()

                    if (tile.enabled) {
                        var scaleMovementFactor = 1.0f
                        val longlivity = 60
                        val period = 200
                        val angleDelta = Math.PI * (layerIndex + 1) / layerAmount

                        scaleMovementFactor = 1 + cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f

                        val delta = 0.7f * abs(scaleMovementFactor)
                        glTranslatef(delta, delta, -delta)
                        glPushMatrix()
                        val correction = 0.25
                        glTranslated(correction, -0.1, -correction)
                        SusGraphicHelper.drawFloatyLine(
                            -delta.toDouble() - correction,
                            -delta.toDouble() + correction + 0.01,
                            delta.toDouble() - correction + 0.4,
                            humilitasColor,
                            wavesTexture,
                            speed = 0.05f,
                            Math.min(time + 2, 10.0f) / 10.0f,
                            width = 1F,
                            time = time,
                            true,
                            1.0
                        ) {
                            glEnable(GL_BLEND)
                            glBlendFunc(GL_SRC_ALPHA, GL_ONE)
                        }
//                        SusGraphicHelper.drawGuideArrows()

                        glPopMatrix()
                        val rotationSwigling = 1.5
                        val translateSwigling = 0.005
//                        glRotated(random.nextGaussian() * rotationSwigling, 1.0, 0.0, 0.0)
//                        glRotated(random.nextGaussian() * rotationSwigling, 0.0, 0.0, 1.0)
//                        glRotated(random.nextGaussian() * rotationSwigling, 0.0, 1.0, 0.0)
//                        glTranslated(random.nextGaussian() * translateSwigling, 0.0, 0.0)
//                        glTranslated(0.0, random.nextGaussian() * translateSwigling, 0.0)
//                        glTranslated(0.0, 0.0, random.nextGaussian() * translateSwigling)
                    }
                    glScaled(0.5, 0.5, 0.5)
                    UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                    foundTile.cornerModel.renderOnly("plane_Cube.002")
                    SusGraphicHelper.pushLight()
                    SusGraphicHelper.setMaxBrightness()
//                    UtilsFX.bindTexture(foundTile.cornerActiveGlowingTexture)
//                    foundTile.cornerModel.renderOnly("plane_Cube.002")
                    SusGraphicHelper.popLight()
                } else {
//                    UtilsFX.bindTexture(foundTile.cornerActiveCoreTexture)


//                    val offset = calculateOffsetForStraightLayerElement(layerIndex, )
                    glPushMatrix()
                    glScaled(0.5, 0.5, 0.5)
                    glColor4d(0.0, 0.0, 1.0, 1.0)
                    glDisable(GL_LIGHTING)
                    glDisable(GL_BLEND)
                    glDisable(GL_ALPHA_TEST)
                    UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                    foundTile.straightModel.renderPart("plane_Cube")
                    glPopMatrix()
                    glColor4d(1.0, 0.0, 0.0, 1.0)

                    if (tile.enabled) {
                        val scaleMovementFactor = 1 + cos(time / 10 + Math.PI * (layerIndex) / layerAmount) * 0.2f

                        val delta = 0.7f * scaleMovementFactor
                        glTranslatef(0.0f, delta, -delta)
//                        SusGraphicHelper.drawGuideArrows()
                        glPushMatrix()
                        val correction = 0.3
                        glTranslated(0.0, -correction, correction)
                        SusGraphicHelper.drawFloatyLine(
                            0.0,
                            -delta.toDouble() + correction + 0.01,
                            delta.toDouble() - correction + 0.4,
                            humilitasColor,
                            wavesTexture,
                            speed = 0.1f,
                            Math.min(time + 2, 10.0f) / 10.0f,
                            width = 1F,
                            time = time,
                            true,
                            1.0
                        ) {
                            glEnable(GL_BLEND)
                            glBlendFunc(GL_SRC_ALPHA, GL_ONE)
                        }
                        glPopMatrix()
                        val rotationSwigling = 1.5
                        val translateSwigling = 0.005
//                        glRotated(random.nextGaussian() * rotationSwigling, 1.0, 0.0, 0.0)
//                        glRotated(random.nextGaussian() * rotationSwigling, 0.0, 0.0, 1.0)
//                        glRotated(random.nextGaussian() * rotationSwigling, 0.0, 1.0, 0.0)
//                        glTranslated(random.nextGaussian() * translateSwigling, 0.0, 0.0)
//                        glTranslated(0.0, random.nextGaussian() * translateSwigling, 0.0)
//                        glTranslated(0.0, 0.0, random.nextGaussian() * translateSwigling)
                    }
                    glScaled(0.5, 0.5, 0.5)
                    UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                    foundTile.straightModel.renderOnly("core_Cube.001")
                }
                glPopMatrix()

            }

        }
    }

    fun rotateFromOrientation(facing: ForgeDirection) {
        when (facing) {
            ForgeDirection.UP -> {}
            ForgeDirection.DOWN -> {
                GL11.glRotatef(180f, 1f, 0f, 0f)
            }

            ForgeDirection.NORTH -> {
            }

            ForgeDirection.SOUTH -> {
                GL11.glRotatef(180f, 0f, 1f, 0f)
            }

            ForgeDirection.WEST -> {
                GL11.glRotatef(90f, 0f, 1f, 0f)
            }

            ForgeDirection.EAST -> {
                GL11.glRotatef(-90f, 0f, 1f, 0f)
            }

            else -> {}
        }
    }
}

fun Position.toSusVec3() = SusVec3(x, y, z)
fun SusVec3.isCoDirected(vec3: SusVec3) = ((x / vec3.x) == (y / vec3.y)) && ((y / vec3.y) == (z / vec3.z))