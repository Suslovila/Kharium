package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.MagicFuel
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.implant.PacketImplantSync
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent


// by itself it is "instant" ability

abstract class AbilityInstant(name: String) : Ability(name) {
    override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
        val requiredFuel = getFuelConsumeOnActivation(implant)
        if(requiredFuel != null) {
            val lack = requiredFuel.getLack(player)
            val hasEnough = lack.isEmpty()
            if(hasEnough) {
                onActivated(player, index, implant)
                requiredFuel.forceTakeFrom(player)
                player.addKharu(getKharuEmissionOnActivation(implant))
            }
        }
        else{
            onActivated(player, index, implant)
            player.addKharu(getKharuEmissionOnActivation(implant))
        }
    }

    abstract fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack)
}