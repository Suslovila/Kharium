package com.suslovila.kharium.client.render


import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.client.particles.FXBase
import com.suslovila.kharium.client.particles.ParticleRenderDispatcher
import com.suslovila.kharium.client.render.tile.toSusVec3
import com.suslovila.kharium.common.item.ItemKharuNetConfigurator
import com.suslovila.kharium.utils.RotatableHandler
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.utils.Position
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
import java.awt.Color
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun renderNetMembers(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft().thePlayer ?: return
        player.heldItem?.let { stack ->
            if (stack.item is ItemKharuNetConfigurator) {
                val tag = stack.getOrCreateTag()
                if (tag.hasKey(ItemKharuNetConfigurator.CURRENT_NET_HANDLER_NBT)) {
                    val netHandler = ItemKharuNetConfigurator.getCurrentNetHandler(player.worldObj, stack) ?: return
                    val handlerPosition = netHandler.getPosition()

                    glPushMatrix()
                    SusGraphicHelper.translateFromPlayerTo(handlerPosition.toSusVec3(), event.partialTicks)

                    val time = SusGraphicHelper.getRenderGlobalTime(event.partialTicks)
                    netHandler.netSuppliers.forEach { member ->
                        glPushMatrix()
                        UtilsFX.drawFloatyLine(
                            member.position.x.toDouble(),
                            member.position.y.toDouble(),
                            member.position.z.toDouble(),

                            handlerPosition.x.toDouble(),
                            handlerPosition.y.toDouble(),
                            handlerPosition.z.toDouble(),
                            event.partialTicks,
                            Color.red.rgb,
                            "${Kharium.MOD_ID}:textures/misc/bubble.png",
                            0.1f,
                            Math.min(player.ticksExisted, 10) / 10.0f,
                            0.3F,
                        )
                        glPopMatrix()
                    }

                    netHandler.netConsumers.forEach { member ->
                        val offset = member.position - handlerPosition
                        glPushMatrix()
                        SusGraphicHelper.drawGuideArrows()
                        glTranslated(offset.x.toDouble(), offset.y.toDouble(), offset.z.toDouble())
                        SusGraphicHelper.drawGuideArrows()
                        val inverted = SusVec3(-offset.x, -offset.y, -offset.z)
                        SusGraphicHelper.drawFloatyLine(
                            inverted.x,
                            inverted.y,
                            inverted.z,
                            1,
                            ResourceLocation(Kharium.MOD_ID, "textures/misc/bubble.png"),
                            speed = 0.1f,
                            Math.min(time, 10.0f) / 10.0f,
                            width = 0.3F,
                            time = time,
                            false,
                            1.0
                        ) { glDisable(GL_BLEND) }

                        glPopMatrix()
                    }
                }

                glPopMatrix()
            }
        }
    }
}