package com.suslovila.kharium.client.clientProcess

import com.suslovila.kharium.api.event.ClientProcess
import com.suslovila.kharium.api.event.IProcess
import com.suslovila.kharium.client.render.ClientEventHandler
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import net.minecraftforge.client.event.RenderWorldEvent
import net.minecraftforge.client.event.RenderWorldLastEvent

object ClientProcessHandler {
    val processes = arrayListOf<ClientProcess>()

    @SubscribeEvent
    fun tick(event: WorldTickEvent) {
        val world = event.world
        if (world.isRemote && event.phase == TickEvent.Phase.END) {
            processes.forEach { it.tick(event) }
            processes.removeIf { it.isExpired() }
        }
    }

    @SubscribeEvent
    fun tick(event: RenderWorldLastEvent) {
        processes.forEach { it.render(event) }
    }
}