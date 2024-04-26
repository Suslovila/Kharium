package com.suslovila.kharium.client.render


import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.client.particles.FXBase
import com.suslovila.kharium.client.particles.ParticleRenderDispatcher
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.TileAntiNodeStabilizerRenderer
import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import com.suslovila.kharium.utils.RotatableHandler
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import java.util.ArrayDeque
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

object ClientEventHandler {

    val postRenders = ConcurrentLinkedQueue<PostRendered>()

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderWorldLastLowest(event: RenderWorldLastEvent) {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    fun onRenderWorldLastLow(event: RenderWorldLastEvent) {


    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    fun onRenderWorldLastNormal(event: RenderWorldLastEvent) {
        dispatchQueuedRenders(event)


    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRenderWorldLastHigh(event: RenderWorldLastEvent) {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRenderWorldLastHighest(event: RenderWorldLastEvent) {
        handleParticles()

    }

    private fun handleParticles() {
        val profiler = Minecraft.getMinecraft().mcProfiler
        profiler.startSection("particles")
        ParticleRenderDispatcher.dispatch()
        profiler.endSection()
    }

    private fun dispatchQueuedRenders(
        event: RenderWorldLastEvent
    ) {
        glColor4f(1.0f, 1.0f, 1.0f, 1f)
        if (postRenders.isNotEmpty()) {
            for (postRendered in postRenders) postRendered.postRender(event)
            postRenders.clear()

        }
    }
}