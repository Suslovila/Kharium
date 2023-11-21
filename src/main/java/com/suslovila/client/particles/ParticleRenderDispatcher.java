/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 2, 2014, 12:12:45 AM (GMT)]
 */
package com.suslovila.client.particles;
import com.mojang.realmsclient.util.Pair;

import kotlin.reflect.KClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;

public final class ParticleRenderDispatcher {


	// Called from LightningHandler.onRenderWorldLast since that was
	// already registered. /shrug
	public static void dispatch() {
		Tessellator tessellator = Tessellator.instance;

		Profiler profiler = Minecraft.getMinecraft().mcProfiler;

		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		GL11.glDisable(GL11.GL_LIGHTING);

		profiler.startSection("particles");
		dispatchQueuedRenders(tessellator, FXAntiNode.queuedRenders, FXAntiNode.queuedDepthIgnoringRenders, FXAntiNode.FXTexture);
		profiler.endSection();

		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopAttrib();
	}


	public static void dispatchQueuedRenders(Tessellator tessellator, ArrayDeque<FXSusBase> queuedRenders, ArrayDeque<FXSusBase> queuedDepthIgnoringRenders, ResourceLocation texture) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		if (!queuedRenders.isEmpty()) {

			tessellator.startDrawingQuads();
			for (FXSusBase wisp : queuedRenders)
				wisp.renderQueued(tessellator);
			tessellator.draw();
		}

		if (!queuedDepthIgnoringRenders.isEmpty()) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			tessellator.startDrawingQuads();
			for (FXSusBase wisp : queuedDepthIgnoringRenders)
				wisp.renderQueued(tessellator);
			tessellator.draw();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		queuedRenders.clear();
		queuedDepthIgnoringRenders.clear();
	}
}