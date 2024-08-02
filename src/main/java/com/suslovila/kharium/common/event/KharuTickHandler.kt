package com.suslovila.kharium.common.event

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.render.tile.tileAntiNodeController.AntiNodeStabilizersRenderer
import com.suslovila.kharium.common.worldSavedData.AxisWrapper.center
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.Explosion
import com.suslovila.kharium.common.worldSavedData.KharuHotbed
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusVec3
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.event.world.WorldEvent
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX

object KharuTickHandler {

    val clientKharuHotbeds = ArrayList<KharuHotbed>()

    val MODEL = ResourceLocation(Kharium.MOD_ID, "models/shieldSphere.obj")
    val model: IModelCustom by lazy { AdvancedModelLoader.loadModel(MODEL) }

    @SubscribeEvent
    fun kharuHotBedTick(event: TickEvent.WorldTickEvent) {

    }

    @SubscribeEvent
    fun kharuHotBedTick(event: WorldEvent.Load) {
        with(event) {
            if (!world.isRemote) {
                world.customData.syncAllHotbeds(world)
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun explosionRenderer(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft().thePlayer ?: return
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/antinode/controller/field.png")
        clientKharuHotbeds.forEach { hotbed ->
            val clientTime = SusGraphicHelper.getRenderGlobalTime(event.partialTicks)
            GL11.glPushMatrix()
            SusGraphicHelper.translateFromPlayerTo(hotbed.zone.center, event.partialTicks)
//            GL11.glDepthMask(false)
            //GL11.glDisable(GL11.GL_CULL_FACE)
//            GL11.glDisable(GL11.GL_ALPHA_TEST)
//            GL11.glEnable(GL11.GL_BLEND)
//            GL11.glDisable(GL11.GL_LIGHTING)
            UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
            GL11.glScaled(0.5, 0.5, 0.5)
            GL11.glColor4f(1f, 1f, 1f, 1f)
            model.renderAll()

//            GL11.glEnable(GL11.GL_CULL_FACE)
//            GL11.glEnable(GL11.GL_ALPHA_TEST)
//            GL11.glDisable(GL11.GL_BLEND)
//            GL11.glEnable(GL11.GL_LIGHTING)
//            GL11.glDepthMask(true)

            GL11.glPopMatrix()
        }
    }


}

