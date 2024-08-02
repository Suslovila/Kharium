package com.suslovila.kharium.common.event

import com.suslovila.kharium.api.implants.ItemImplant
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent

object ImplantEvents {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun renderWorldLast(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onRenderWorldLastEvent(event, index, stack)
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onRenderHandEvent(event: RenderHandEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onRenderHandEvent(event, index, stack)
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onRenderPlayerEvent(event: RenderPlayerEvent.Post) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onRenderPlayerEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerAttackEntityEvent(event: AttackEntityEvent) {
        KhariumPlayerExtendedData.get(event.entityPlayer)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerAttackEntityEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerHealEvent(event: LivingHealEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerHealEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerUpdateEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onLivingHurtEvent(event: LivingHurtEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerHurtEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerDeathEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerBeingAttackedEvent(event: LivingAttackEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerBeingAttackedEvent(event, index, stack)
        }
    }

    @SubscribeEvent
    fun onLivingSetAttackTargetEvent(event: LivingSetAttackTargetEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { index, stack ->
            (stack?.item as? ItemImplant)?.onPlayerSetAttackTargetEvent(event, index, stack)
        }
    }
}