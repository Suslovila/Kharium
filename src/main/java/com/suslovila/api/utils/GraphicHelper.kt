package com.suslovila.api.utils

import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11.*

object GraphicHelper {
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
}