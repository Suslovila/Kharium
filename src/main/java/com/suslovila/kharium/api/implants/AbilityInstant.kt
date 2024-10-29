package com.suslovila.kharium.api.implants

import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack


// by itself it is "instant" ability

abstract class AbilityInstant(name: String) : Ability(name) {
    override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
        println("entering button 0")
        val requiredFuel = getFuelConsumeOnActivation(implant)
        if (requiredFuel == null || requiredFuel.tryTakeFuelFromPlayerWithPacket(player)) {
            onActivated(player, index, implant)
            player.addKharu(getKharuEmissionOnActivation(implant))
        }
    }

    abstract fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack)
}