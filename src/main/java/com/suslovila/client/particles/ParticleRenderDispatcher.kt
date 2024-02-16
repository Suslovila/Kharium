package com.suslovila.client.particles

import com.suslovila.Kharium
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.util.*

object ParticleRenderDispatcher {


    // Called from onRenderWorldLast since that was already registered.
    fun dispatch() {
        val tessellator = Tessellator.instance
        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDepthMask(false)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        //rendering smoke-spiral particles
        dispatchQueuedRenders(
            tessellator,
            FXSmokeSpiral.queuedRenders,
            FXSmokeSpiral.queuedDepthIgnoringRenders,
            ResourceLocation(Kharium.MOD_ID, "textures/misc/p_large.png")
        )

        GL11.glDepthMask(true)

        //rendering black particles
        dispatchQueuedRenders(
            tessellator,
            FXKharu.queuedRenders,
            FXKharu.queuedDepthIgnoringRenders,
            FXKharu.FXTexture
        )

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
    }


    fun dispatchQueuedRenders(
        tessellator: Tessellator,
        queuedRenders: ArrayDeque<FXBase>,
        queuedDepthIgnoringRenders: ArrayDeque<FXBase>,
        texture: ResourceLocation?
    ) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f)
        Minecraft.getMinecraft().renderEngine.bindTexture(texture)
        if (!queuedRenders.isEmpty()) {
            tessellator.startDrawingQuads()
            for (particle in queuedRenders) particle.renderQueued(tessellator)

            tessellator.draw()
        }
        if (!queuedDepthIgnoringRenders.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            tessellator.startDrawingQuads()
            for (particle in queuedDepthIgnoringRenders) particle.renderQueued(tessellator)

            tessellator.draw()
            GL11.glEnable(GL11.GL_DEPTH_TEST)
        }
        queuedRenders.clear()
        queuedDepthIgnoringRenders.clear()
    }
}