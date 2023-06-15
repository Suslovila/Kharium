package com.suslovila.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

import static org.lwjgl.opengl.GL11.*;

public class ParticleRenderDispatcher {

    public static int wispFxCount = 0;
    public static int depthIgnoringWispFxCount = 0;
    public static int sparkleFxCount = 0;
    public static int fakeSparkleFxCount = 0;
    public static int lightningCount = 0;

    // Called from LightningHandler.onRenderWorldLast since that was
    // already registered. /shrug
    public static void dispatch() {
        Tessellator tessellator = Tessellator.instance;

        Profiler profiler = Minecraft.getMinecraft().mcProfiler;

        glPushAttrib(GL_LIGHTING);
        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glAlphaFunc(GL_GREATER, 0.003921569F);
        glDisable(GL_LIGHTING);

//        profiler.startSection("sparkle");
//        FXSparkle.dispatchQueuedRenders(tessellator);
        profiler.startSection("antinodeparticle");
        FXAntiNode.dispatchQueuedRenders(tessellator);
        profiler.endSection();

        glAlphaFunc(GL_GREATER, 0.1F);
        glDisable(GL_BLEND);
        glDepthMask(true);
        glPopAttrib();
    }
}
