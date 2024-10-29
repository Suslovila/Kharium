package com.suslovila.kharium.client.render


import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.api.managment.IConfigurable
import com.suslovila.kharium.client.particles.ParticleRenderDispatcher
import com.suslovila.kharium.common.item.ItemConfigurator
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.getTileCheckChunkLoad
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11.*
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun renderConfigurable(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft().thePlayer ?: return
        player.heldItem?.let { stack ->
            if (stack.item is ItemConfigurator) {
                ItemConfigurator.getCurrentConfigurable(player.worldObj, stack)?.render(stack, event)
            }
        }
    }
}