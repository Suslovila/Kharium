package com.suslovila.kharium.common.event

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.Explosion
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import kotlin.math.sqrt

object PrimordialExplosionHandler {

    @SideOnly(Side.CLIENT)
    val clientExplosions = ArrayList<Explosion>()

    val MODEL = ResourceLocation(Kharium.MOD_ID, "models/shieldSphere.obj")
    val model: IModelCustom = AdvancedModelLoader.loadModel(MODEL)

    @SubscribeEvent
    fun primordialExplosionsTick(event: WorldTickEvent) {
        if (event.phase != TickEvent.Phase.START && !event.world.isRemote) {
            val toRemove = ArrayList<Explosion>()
            event.world.customData.explosions.forEach { expl ->
                expl.timer++
                val actualRadius = expl.timer * SusUtils.explosionSpreadSpeed
                if (actualRadius >= expl.radius && expl.remainingBlocksToDestroy.isEmpty()) {
                    toRemove.add(expl)
                }
                var blockAmountDestroyed = 0
                while (expl.remainingBlocksToDestroy.isNotEmpty() && blockAmountDestroyed <= SusUtils.blockAmountDestroyedPerTick) {
                    val iterablePos = expl.remainingBlocksToDestroy[0]
                    if (iterablePos.distanceTo(expl.pos) <= actualRadius) {
                        event.world.setBlockToAir(iterablePos.x.toInt(), iterablePos.y.toInt(), iterablePos.z.toInt())
                        blockAmountDestroyed++
                        expl.remainingBlocksToDestroy.removeAt(0)
                    } else break
                }
            }
            event.world.customData.explosions.removeAll(toRemove.toSet())
            event.world.customData.syncExplosions(event.world)
        }
    }

    fun castPrimordialExplosion(world: World, pos: SusVec3, radius: Double) {
        world.customData.addExplosion(world, pos, radius)
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun explosionRenderer(event: RenderWorldLastEvent) {
        Minecraft.getMinecraft().thePlayer ?: return
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/antinode/controller/field.png");
        clientExplosions.forEach {
            val clientTime = it.timer + event.partialTicks
            val actualRadius = (clientTime * SusUtils.explosionSpreadSpeed).coerceAtMost(it.radius)
            glPushMatrix()
            SusGraphicHelper.translateFromPlayerTo(it.pos, event.partialTicks)
            glScaled(actualRadius, actualRadius, actualRadius)
            glDepthMask(false)
            glDisable(GL_CULL_FACE)
            glDisable(GL_ALPHA_TEST)
            glEnable(GL_BLEND)
            glDisable(GL_LIGHTING)

            glColor4f(1f, 1f, 1f, 0.1f)
            model.renderAll()

            glEnable(GL_CULL_FACE)
            glEnable(GL_ALPHA_TEST)
            glDisable(GL_BLEND)
            glEnable(GL_LIGHTING)
            glDepthMask(true)

            glPopMatrix()
        }
    }
}


private fun destroyBlocks(world: World, center: SusVec3, radius: Double) {
    val radiusInt = radius.toInt()
    val (x, y, z) = center
    for (x0 in (-radiusInt)..radiusInt) {
        for (y0 in (-radiusInt)..radiusInt) {
            for (z0 in (-radiusInt)..radiusInt) {
                val distance = sqrt((x0 * x0 + y0 * y0 + z0 * z0).toDouble())
                if (distance <= radius) {
                    val xD = x + x0
                    val yD = y + y0
                    val zD = z + z0
                    world.setBlockToAir(xD, yD, zD)
                }
            }
        }
    }
}


