package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateBoolean
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.living.LivingEvent

abstract class AbilityPassive(name: String) : Ability(name) {
    val IS_ABILITY_ENABLED_NBT = Kharium.prefixAppender.doAndGet("$name:isEnabled")

    override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
        if(player.worldObj.isRemote) return
        switchAbilityStatus(player, index, implant)
        notifyClient(player, index, implant)
    }

    open fun switchAbilityStatus(player: EntityPlayer, index: Int, implant: ItemStack): Boolean =
        implant.getOrCreateTag().let { tag ->
            if (isOnCooldown(implant)) return false
            val isActivePrevious = tag.getOrCreateBoolean(IS_ABILITY_ENABLED_NBT, false)
            if (!isActivePrevious) {
                val hasEnoughFuel = getFuelConsumeOnActivation(implant)?.takeFrom(player)?.isEmpty() ?: true
                if (!hasEnoughFuel) {
                    return false
                }

                player.addKharu(getKharuEmissionOnActivation(implant))
            }
            tag.setBoolean(IS_ABILITY_ENABLED_NBT, !isActivePrevious)

            return true
        }
    override fun isActive(implant: ItemStack) =
        implant.getOrCreateTag().getOrCreateBoolean(IS_ABILITY_ENABLED_NBT, false)

    abstract fun getFuelConsumePerSecond(implant: ItemStack): FuelComposite?
    abstract fun getKharuEmissionPerSecond(implant: ItemStack): Int

    override fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, index: Int, implant: ItemStack) {
        super.onPlayerUpdateEvent(event, index, implant)
        val player = (event.entityLiving as EntityPlayer)
        val tag = implant.getOrCreateTag()
        if (player.worldObj.isRemote || player.ticksExisted % 20 != 0 || !isActive(implant)) return
        val hasFuel = getFuelConsumePerSecond(implant)?.takeFrom(player)?.isEmpty() ?: true
        if (!hasFuel) {
            tag.setBoolean(IS_ABILITY_ENABLED_NBT, false)
            notifyClient(player, index, implant)
        }
        player.addKharu(getKharuEmissionPerSecond(implant))
    }
}