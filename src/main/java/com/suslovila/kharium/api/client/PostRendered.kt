package com.suslovila.kharium.api.client

import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent

interface PostRendered {
    fun postRender(event: RenderWorldLastEvent)
}