package com.suslovila.kharium.client.render.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.item.ItemSpaceDivider
import com.suslovila.kharium.utils.ModelWrapperDisplayList
import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.obj.WavefrontObject
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

object ItemSpaceDividerRenderer : IItemRenderer {
    val swordModel: IModelCustom
    val runeModel: IModelCustom

    val textures =
        Array<ResourceLocation>(66) { i -> ResourceLocation(Kharium.MOD_ID, "textures/animated/flame/flame$i.png") }

    init {
        swordModel = ModelWrapperDisplayList(
            AdvancedModelLoader.loadModel(
                ResourceLocation(
                    Kharium.MOD_ID,
                    "models/items/space_divider.obj"
                )
            ) as WavefrontObject
        )
        runeModel = ModelWrapperDisplayList(
            AdvancedModelLoader.loadModel(
                ResourceLocation(
                    Kharium.MOD_ID,
                    "models/items/sword_rune.obj"
                )
            ) as WavefrontObject
        )
    }

    override fun handleRenderType(item: ItemStack?, type: IItemRenderer.ItemRenderType?) = true

    override fun shouldUseRenderHelper(
        type: IItemRenderer.ItemRenderType?,
        item: ItemStack?,
        helper: IItemRenderer.ItemRendererHelper?
    ) = true

    override fun renderItem(type: ItemRenderType, stack: ItemStack?, vararg data: Any?) {
        stack ?: return
        if (stack.item !is ItemSpaceDivider) return

//        SusGraphicHelper.drawGuideArrows()
        glPushMatrix()
        UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            val entity = data[1] as? EntityLivingBase

            val isActivated = (entity is EntityPlayer && entity.itemInUse != null)
            glDisable(GL_CULL_FACE)

            if (type == ItemRenderType.EQUIPPED && !isActivated) {
                glRotated(-45.0, 0.0, 1.0, 0.0)
                glRotated(-20.0, 0.0, 0.0, 1.0)
                glTranslated(-1.4, 0.8, 0.0)
                swordModel.renderAll()
                glTranslated(1.4, 0.2, 0.0)
                renderBladeRunes(stack, 0f, 6, 60.0)
            }
            if (type == ItemRenderType.EQUIPPED && isActivated) {
                glRotated(-45.0, 0.0, 1.0, 0.0)
                glRotated(-20.0, 0.0, 0.0, 1.0)
                glTranslated(-1.4, 0.8, 0.0)

                glRotated(60.0, 0.0, 1.0, 0.0)
                SusGraphicHelper.drawGuideArrows()

                glTranslated(-1.4, 0.0, 2.0)

                swordModel.renderAll()
                val minecraft = Minecraft.getMinecraft() ?: return

                val partialTicks = UtilsFX.getTimer(minecraft).renderPartialTicks
                val time = ((entity as? EntityPlayer)?.itemInUseDuration?.toFloat() ?: return) + partialTicks
                val bladeWidth = 0.1
                glTranslated(1.4, 0.2, 0.0)
                renderBladeRunes(stack, time, 6, 60.0)
            }

            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && !isActivated) {
                glTranslated(0.0, 0.8, 0.0)
                glRotated(-90.0, 0.0, 1.0, 0.0)
                glRotated(-90.0, 0.0, 0.0, 1.0)

                glTranslated(-1.3, 0.0, 0.0)
                glTranslated(0.0, -0.5, -1.5)
                glRotated(20.0, 0.0, 0.0, 1.0)
                glRotated(20.0, 1.0, 0.0, 0.0)
                glRotated(-20.0, 0.0, 1.0, 0.0)

                swordModel.renderAll()
                glTranslated(1.5, 0.15, -0.0)

                renderBladeRunes(stack, 0f, 6, 60.0)
            }

            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && isActivated) {
                glTranslated(0.0, 0.8, -1.0)
                glRotated(-60.0, 0.0, 1.0, 0.0)
                glRotated(110.0, 1.0, 0.0, 0.0)

                swordModel.renderAll()

                val bladeWidth = 0.1
                val runeAmount = 7
                glTranslated(1.5, 0.15, -0.0)
                val minecraft = Minecraft.getMinecraft()

                val partialTicks = UtilsFX.getTimer(minecraft).renderPartialTicks
                val time = ((entity as? EntityPlayer)?.itemInUseDuration?.toFloat() ?: return) + partialTicks

                renderBladeRunes(stack, time, 6, 60.0)

//                SusGraphicHelper.drawGuideArrows()
            }
//            renderActivatedBlade(type, stack, data)
        }
//        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) glTranslatef(0f, 0.15f, 0f)
//        SusGraphicHelper.drawGuideArrows()
        glPopMatrix()
    }

//    private fun renderActivatedBlade(type: IItemRenderer.ItemRenderType, stack: ItemStack, vararg data: Any) {
//        val minecraft = Minecraft.getMinecraft() ?: return
//        try {
//
//
//            val partialTicks = UtilsFX.getTimer(minecraft).renderPartialTicks
//            val entity = (data[0] as Array<*>)[1] as? EntityLivingBase
//
//            if (entity is EntityPlayer && entity.itemInUse != null) {
//                val time = entity.itemInUseDuration.toFloat() + partialTicks
////                glTranslated(0.0, -1.0, -0.0)
////                glRotated(-20.0, 1.0, 0.0, 0.0)
////
////                glRotated(20.0, 0.0, 1.0, 0.0)
//                glRotated(90.0, 0.0, 1.0, 0.0)
////                SusGraphicHelper.drawGuideArrows()
//
////                glRotated(-45.0, 1.0, 0.0, 0.0)
//
//            }
//            swordModel.renderAll()
//
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//        }
//    }

    private fun renderBladeRunes(
        item: ItemStack,
        preparationTime: Float,
        runeAmount: Int,
        maxAngle: Double
    ) {
        val runeLength = 0.65
        val maxPrepareTime = ItemSpaceDivider.getMaxPreparationTime(item).toFloat()
        val angle = when (preparationTime) {
            in 0f..(maxPrepareTime / 2) -> 0.0
            in (maxPrepareTime / 2)..maxPrepareTime -> maxAngle / 2
            else -> maxAngle
        }

        val tessellator = Tessellator.instance
        SusGraphicHelper.pushLight()
        SusGraphicHelper.setMaxBrightness()

        for (xAngle in arrayOf(0.0, 180.0)) {
            glPushMatrix()
            glRotated(xAngle, 1.0, 0.0, 0.0)
            val zCorrectionFffset = (0.07 + (runeLength / 2 * (angle / maxAngle) * (maxAngle / 90.0)))
            val xCorrectionOffset = (runeLength / 2 * (angle / maxAngle) * (maxAngle / 90.0))
            for (i in 0 until runeAmount) {
                glTranslated(-xCorrectionOffset, 0.0, -zCorrectionFffset)
                glRotated(angle, 0.0, 1.0, 0.0)
                UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                glColor4d(1.0, 1.0, 1.0, 1.0)
                runeModel.renderAll()
                if (angle > 0.0) {
                    glTranslated(0.0, 0.0, 0.03)
                    renderFlames()
//                    SusGraphicHelper.drawGuideArrows()
                    glTranslated(0.0, 0.0, -0.03)

                }
                glRotated(-angle, 0.0, 1.0, 0.0)
                glTranslated(xCorrectionOffset, 0.0, zCorrectionFffset)

                glTranslated(-(0.5 + 0.05), 0.0, 0.0)
            }
            glPopMatrix()
        }
        SusGraphicHelper.popLight()
//        glTranslated(0.0, 0.0, bladeWidth * 2)
//        glRotated(180.0, 1.0, 0.0, 0.0)
//
//        for (i in 0 until runeAmount) {
//            glTranslated(0.0, 0.0, -runeLength / 2 * (angle / maxAngle))
//            glRotated(angle, 0.0, 1.0, 0.0)
//            runeModel.renderAll()
//            glRotated(-angle, 0.0, 1.0, 0.0)
//            glTranslated(0.0, 0.0, runeLength / 2 * (angle / maxAngle))
//
//            glTranslated((runeLength + 0.1), 0.0, 0.0)
//        }
//        SusGraphicHelper.drawGuideArrows()

    }

    private fun renderFlames() {
        val nt = System.nanoTime()
        val frames = 66
        val frame: Int = ((nt / 10000000L) % frames.toLong()).toInt()
        val strip = 0

        val v1 = 0.0
        val v2 = 1.0
        val u1 = 0.0
        val u2 = 1.0
        val tessellator = Tessellator.instance

        SusGraphicHelper.setDefaultBlend()

        UtilsFX.bindTexture(textures[frame])
        val scaleFactor = 0.6
        val offset = -0.5
        tessellator.startDrawingQuads()
        SusGraphicHelper.bindColor(Color.red.rgb, 0.7f, 1.0f)
        glScaled(scaleFactor, scaleFactor, scaleFactor)
        tessellator.addVertexWithUV(-1.0 - offset, 0.5, 0.0, u2, v2)
        tessellator.addVertexWithUV(1.0 - offset, 0.5, 0.0, u2, v1)
        tessellator.addVertexWithUV(1.0 - offset, -0.5, 0.0, u1, v1)
        tessellator.addVertexWithUV(-1.0 - offset, -0.5, 0.0, u1, v2)
        tessellator.draw()

        SusGraphicHelper.disableBlend()
        glScaled(1 / scaleFactor, 1 / scaleFactor, 1 / scaleFactor)
    }
}