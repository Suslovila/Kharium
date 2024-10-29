package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.implant.PacketImplantSync
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent


// by itself it is "instant" ability

abstract class Ability(val name: String) {
    val COOLDOWN_NBT = Kharium.prefixAppender.doAndGet("$name:cooldown")
    open val texture = ResourceLocation(Kharium.MOD_ID, "textures/implants/ability_$name.png")
    // fired on both sides
    abstract fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack)

    open fun isOnCooldown(implant: ItemStack) =
        implant.getOrCreateTag().getOrCreateInteger(COOLDOWN_NBT, 0) != 0

    abstract fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite?
    abstract fun getKharuEmissionOnActivation(implant: ItemStack): Int

    abstract fun getCooldownTotal(implant: ItemStack): Int

    abstract fun isActive(implant: ItemStack): Boolean

    open fun getCooldown(implant: ItemStack) =
        implant.getOrCreateTag().getOrCreateInteger(COOLDOWN_NBT, 0)

    open fun sendToCooldown(implant: ItemStack) {
        val tag = implant.getOrCreateTag()
        tag.setInteger(COOLDOWN_NBT, getCooldownTotal(implant))
    }


    // all events have "normal" priority by default
    // if any other needed, you should implement all logic by yourself

    open fun onRenderWorldLastEvent(event: RenderWorldLastEvent, index: Int, implant: ItemStack) {}
    open fun onRenderHandEvent(event: RenderHandEvent, index: Int, implant: ItemStack) {}
    open fun onRenderPlayerEvent(event: RenderPlayerEvent.Post, index: Int, implant: ItemStack) {}
    open fun onPlayerAttackEntityEvent(event: AttackEntityEvent, index: Int, implant: ItemStack) {}
    open fun onPlayerHealEvent(event: LivingHealEvent, index: Int, implant: ItemStack) {}
    open fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, index: Int, implant: ItemStack) {
        tickCooldown(implant)
    }

    fun tickCooldown(implant: ItemStack) {
        val tag = implant.getOrCreateTag()
        val cooldown = tag.getOrCreateInteger(COOLDOWN_NBT, 0)
        if (cooldown > 0) {
            tag.setInteger(COOLDOWN_NBT, cooldown - 1)
        }
    }

    open fun onPlayerHurtEvent(event: LivingHurtEvent, index: Int, stack: ItemStack) {}
    open fun onPlayerDeathEvent(event: LivingDeathEvent, index: Int, stack: ItemStack) {}
    open fun onPlayerBeingAttackedEvent(event: LivingAttackEvent, index: Int, stack: ItemStack) {}
    open fun onPlayerSetAttackTargetEvent(event: LivingSetAttackTargetEvent, index: Int, stack: ItemStack) {}


    open fun notifyClient(player: EntityPlayer, index: Int, implant: ItemStack) {
        KhariumPacketHandler.INSTANCE.sendToAll(PacketImplantSync(player, index, implant))
    }

}