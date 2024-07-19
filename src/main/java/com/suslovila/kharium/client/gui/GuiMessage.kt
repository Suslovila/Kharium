package com.suslovila.kharium.client.gui

import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import org.lwjgl.opengl.GL11
import thaumcraft.api.aspects.Aspect
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

abstract class GuiMessage(
    val totalTime: Int,
    val x: Int,
    val y: Int
) {
    var timeLeft: Int = totalTime
    fun isExpired() =
        timeLeft <= 0

    fun tick() {
        timeLeft -= 1
    }

    abstract fun draw(event: RenderGameOverlayEvent.Post)
}

open class GuiMessageNotEnoughFuel(
    val totalTime: Int,
    val text: String,
    val textXPos: Int,
    val image: ResourceLocation?,
) {
    var timeLeft: Int = totalTime
    fun isExpired() =
        timeLeft <= 0

    fun tick() {
        timeLeft -= 1
    }


    open fun draw(event: RenderGameOverlayEvent.Post, yPos: Int) {
        val alpha = (1.0f * timeLeft.toFloat() / totalTime).coerceAtLeast(0.0f)
        val color = Color(1.0f, 1.0f, 1.0f, alpha).rgb
        Minecraft.getMinecraft().fontRendererObj.drawString(text, textXPos, yPos, color)
        val stringLengthFactor = 0.1
        UtilsFX.bindTexture(image)
        GL11.glPushMatrix()
//        GL11.glTranslated(
//            event.resolution.scaleFactor * (text.length * stringLengthFactor + textXPos.toDouble()),
//            event.resolution.scaleFactor * (yPos.toDouble()),
//            0.0
//        )
//        val v1 = 0.0
//        val v2 = 1.0
//        val u1 = 0.0
//        val u2 = 1.0
//        val tessellator = Tessellator.instance
        SusGraphicHelper.bindColor(Aspect.FIRE.color, alpha, 1.0f)
        UtilsFX.drawTexturedQuad(1, 1, 152, 0, 20, 76, -90.0)

//        tessellator.startDrawingQuads()
        val pictureRadius = 4.0
//        UtilsFX.drawTexturedQuad(1, 1, 152, 0, 20, 76, -90.0)
//        tessellator.addVertexWithUV(-pictureRadius, pictureRadius, -299.0, u2, v2)
//        tessellator.addVertexWithUV(pictureRadius, pictureRadius, -299.0, u2, v1)
//        tessellator.addVertexWithUV(pictureRadius, -pictureRadius, -299.0, u1, v1)
//        tessellator.addVertexWithUV(-pictureRadius, -pictureRadius, -299.0, u1, v2)
//        tessellator.draw()
        GL11.glPopMatrix()
    }
}


class GuiPosition(val x: Int, val y: Int)