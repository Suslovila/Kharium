package com.suslovila.kharium.common.event

import com.suslovila.kharium.api.implants.ItemImplant
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent

object ImplantEvents {
    @SubscribeEvent
    fun renderWorldLast(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onRenderWorldLastEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onRenderHandEvent(event: RenderHandEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onRenderHandEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onRenderPlayerEvent(event: RenderPlayerEvent.Specials.Post) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onRenderPlayerEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerAttackEntityEvent(event: AttackEntityEvent) {
        KhariumPlayerExtendedData.get(event.entityPlayer)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerAttackEntityEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerHealEvent(event: LivingHealEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerHealEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerUpdateEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onLivingHurtEvent(event: LivingHurtEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerHurtEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerDeathEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onPlayerBeingAttackedEvent(event: LivingAttackEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerBeingAttackedEvent(event, stack)
        }
    }

    @SubscribeEvent
    fun onLivingSetAttackTargetEvent(event: LivingSetAttackTargetEvent) {
        val player = event.entity
        if (player !is EntityPlayer) return
        KhariumPlayerExtendedData.get(player)?.implantStorage?.forEachImplant { stack ->
            (stack?.item as? ItemImplant)?.onPlayerSetAttackTargetEvent(event, stack)
        }
    }
}