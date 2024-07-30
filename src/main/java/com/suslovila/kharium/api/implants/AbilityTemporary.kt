package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.living.LivingEvent

abstract class AbilityTemporary(name: String) : Ability(name) {
    companion object {
        val TIME_LEFT_NBT = Kharium.prefixAppender.doAndGet("activeTimer")
    }

    override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
        if(player.worldObj.isRemote) return
        activateAbility(player, index, implant)
        notifyClient(player, index, implant)
    }

    abstract fun getEffectDuration(): Int

    open fun activateAbility(player: EntityPlayer, index: Int, implant: ItemStack): Boolean =
        implant.getOrCreateTag().let { tag ->
            if (isOnCooldown(implant) || isActive(implant)) return false
            val requiredFuel = getFuelConsumeOnActivation(implant)
            val lackAmount = requiredFuel?.getLack(player)
            if (lackAmount?.isEmpty() != true) {
                if(!player.worldObj.isRemote) {
                    lackAmount
                }
                return false
            }

            player.addKharu(getKharuEmissionOnActivation(implant))
            tag.setInteger(TIME_LEFT_NBT, getEffectDuration())

            return true
        }

    override fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, index: Int, implant: ItemStack) {
        val world = event.entityLiving.worldObj
        val tag = implant.getOrCreateTag()
        val timeLeftPrevious = tag.getOrCreateInteger(TIME_LEFT_NBT, getEffectDuration())
        if (timeLeftPrevious > 0) {
            tag.setInteger(TIME_LEFT_NBT, timeLeftPrevious - 1)
            if (timeLeftPrevious == 1) {
                sendToCooldown(implant)
            }
        }
        else {
            super.onPlayerUpdateEvent(event, index, implant)
        }
    }


    override fun isActive(implant: ItemStack): Boolean =
        implant.getOrCreateTag().getOrCreateInteger(TIME_LEFT_NBT, 0) != 0

}