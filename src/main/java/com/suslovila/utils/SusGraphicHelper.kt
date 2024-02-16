package com.suslovila.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11.*
import java.awt.Color

object SusGraphicHelper {

    @JvmStatic
    fun drawGuideArrows() {
        with(Tessellator.instance) {
            glDisable(GL_TEXTURE_2D)
            glLineWidth(2F)

            startDrawing(GL_LINES)
            setColorRGBA_F(0F, 0F, 1F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(0.0, 0.0, 1.0)
            draw()

            startDrawing(GL_LINES)
            setColorRGBA_F(0F, 1F, 0F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(0.0, 1.0, 0.0)
            draw()

            startDrawing(GL_LINES)
            setColorRGBA_F(1F, 0F, 0F, 1F)
            addVertex(0.0, 0.0, 0.0)
            addVertex(1.0, 0.0, 0.0)
            draw()

            glLineWidth(1F)
            glEnable(GL_TEXTURE_2D)
        }
    }
    fun bindColor(tessellator: Tessellator, color : Int, alpha : Float, fadeFactor : Float) {
        val co = Color(color)
        val r = co.red / 255.0f
        val g = co.green/ 255.0f
        val b = co.blue / 255.0f
        tessellator.setColorRGBA_F(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha)
    }

    fun translateFromPlayerTo(pos : SusVec3, partialTicks : Float){
        val player = Minecraft.getMinecraft().thePlayer
        val destX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
        val destY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
        val destZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
        glTranslated(pos.x - destX, pos.y - destY, pos.z - destZ)
    }
    enum class BasicDirection(val vec3: SusVec3) {
        NORTH(SusVec3(0, 0, -1)),
        SOUTH(SusVec3(0, 0, 1)),
        EAST(SusVec3(1, 0, 0)),
        WEST(SusVec3(-1, 0, 0)),
        UP(SusVec3(0, 1, 0)),
        DOWN(SusVec3(0, -1, 0))
    }
}
