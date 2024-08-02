package com.suslovila.kharium.api.event

import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import net.minecraftforge.client.event.RenderWorldLastEvent

interface IProcess {
    fun tick(event: WorldTickEvent)
    fun isExpired(): Boolean
    fun render(event: RenderWorldLastEvent)
}